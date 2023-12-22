package vanhoang.project.config;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import vanhoang.project.annotation.BinlogEntityListener;
import vanhoang.project.binlog.handle.base.BinLogHandle;
import vanhoang.project.entity.base.BaseEntity;
import vanhoang.project.utils.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mysql-binlog.db")
public class BinLogClientConfig {
    public final static String PACKAGE_ENTITY_NAME = "vanhoang.project.binlog.handle";
    public final static String BINLOG_INFO_FILE =
            System.getProperty("user.dir") +  File.separator + "binlog_info_file.txt";
    public final static Map<String, BinLogHandle> BINLOG_ENTITY_LISTENER_MAP = new HashMap<>();
    public final static Map<String, Class<? extends BaseEntity>> BINLOG_ENTITY_MAP = new HashMap<>();
    public final static Map<String, List<String>> BINLOG_ENTITY_FIELD_MAP = new HashMap<>();
    private String hostName;
    private Integer port;
    private String username;
    private String password;
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer queueCapacity;
    private Integer keepAliveSeconds;

    @Bean
    @SuppressWarnings("unused")
    public BinaryLogClient getBinaryLogClient() {
        BinaryLogClient binaryLogClient =
                new BinaryLogClient(this.hostName, this.port, this.username, this.password);
        binaryLogClient.registerEventListener(new CustomBinLogEventListener());
        binaryLogClient.registerLifecycleListener(new CustomBinlogLifecycleListener());
        return binaryLogClient;
    }

    @Bean
    @SuppressWarnings("unused")
    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(this.corePoolSize);
        executor.setMaxPoolSize(this.maxPoolSize);
        executor.setQueueCapacity(this.queueCapacity);
        executor.setKeepAliveSeconds(this.keepAliveSeconds);
        executor.setThreadGroupName("binlog-task-executor--");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @SuppressWarnings("unused")
    @EventListener(classes = {ApplicationStartedEvent.class})
    public void scanBinLogEntity() {
        Set<File> files = new HashSet<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try{
            Enumeration<URL> resources =
                    classLoader.getResources(PACKAGE_ENTITY_NAME.replaceAll("\\.", "/"));
            while(resources.hasMoreElements()) {
                URL url = resources.nextElement();
                files.add(new File(url.getFile()));
            }
            for (File directory : files)
                this.scan(directory, PACKAGE_ENTITY_NAME);

            this.connectBinlogMysql();
        } catch (ClassNotFoundException e) {
            log.error("====> scan file contain annotation binlogEntity: not find file", e);
        } catch (IOException e) {
            log.error("====> scan file contain annotation binlogEntity: not find package", e);
        }
    }

    private void scan(File directory, String packageName) throws ClassNotFoundException {
        if(!directory.exists()) return;
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile()) {
                Class<?> clazz =
                        Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6));
                if (clazz.isAnnotationPresent(BinlogEntityListener.class)) {
                    Class<? extends BaseEntity> entityClazz =
                            clazz.getAnnotation(BinlogEntityListener.class).listen();
                    String tableName = entityClazz.getAnnotation(Table.class).name();
                    BINLOG_ENTITY_MAP.put(tableName, entityClazz);
                    BINLOG_ENTITY_LISTENER_MAP.put(tableName, (BinLogHandle) BeanUtils.getBean(clazz));
                    List<String> fieldNames = new ArrayList<>();
                    this.getAllColumnName(fieldNames, entityClazz);
                    BINLOG_ENTITY_FIELD_MAP.put(tableName, fieldNames);
                }
            }
            else {
                this.scan(file, packageName + "." + file.getName());
            }
        }
    }

    /**
     * Có lẽ hiện tại cách lấy thông tin cột theo kiểu sắp xếp:
     *  super class: lấy theo thứ tự khai báo
     *  class: sắp xếp cột --> sắp xếp khóa ngoại.
     *  Chứ lấy theo kiểu metadata thì câu lệnh trở lên rất phức tạp và chưa cần thiết.
     *  Ngày 2023/12/07
     * */
    private void getAllColumnName(List<String> fields, Class<?> type) {
        if (type.getSuperclass() != null) {
            this.getAllSuperClassColumnName(fields, type.getSuperclass());
        }
        else return;

        this.getAllClassColumnName(fields, type);
    }

    private void getAllSuperClassColumnName(List<String> fields, Class<?> type) {
        for (Field field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) ||
                    field.isAnnotationPresent(Id.class)) {
                fields.add(field.getName());
            }
        }
    }

    private void getAllClassColumnName(List<String> fields, Class<?> type) {
        Map<String, String> sortedFieldNameMap = new TreeMap<>();
        Map<String, String> sortedJoinColumnMap = new TreeMap<>();
        for (Field field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) ||
                    field.isAnnotationPresent(Id.class)) {
                sortedFieldNameMap.put(field.getName(), field.getName());
            }
            else if (field.isAnnotationPresent(JoinColumn.class)) {
                JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                sortedJoinColumnMap.put(joinColumn.name(), joinColumn.name());
            }
        }
        fields.addAll(sortedFieldNameMap.values());
        fields.addAll(sortedJoinColumnMap.values());
    }

    private void connectBinlogMysql() {
        log.info("====> connecting binlog slave mysql ...");
        Thread thread = new Thread(() -> {
            BinaryLogClient binaryLogClient = BeanUtils.getBean(BinaryLogClient.class);
            try {
                File file = new File(BINLOG_INFO_FILE);
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String binlogFileName = null;
                Long binlogFilePos = null;
                for (int pos = bufferedReader.read(); pos != -1; pos = bufferedReader.read()) {
                    String line = bufferedReader.readLine();
                    if (line != null) {
                        String value = line.split("=")[1];
                        if (binlogFileName == null) binlogFileName = value;
                        else if (binlogFilePos == null) binlogFilePos = Long.parseLong(value);
                    }
                }
                if (binlogFileName != null)
                    binaryLogClient.setBinlogFilename(binlogFileName);
                if (binlogFilePos != null)
                    binaryLogClient.setBinlogPosition(binlogFilePos);
                log.info("====> connected bin log slave mysql with binlog_info_file: {}, {}",
                        binlogFileName, binlogFilePos);
                binaryLogClient.connect();
            } catch (IOException e) {
                try {
                    log.info("====> connected bin log slave mysql with auto config");
                    binaryLogClient.connect();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        this.getThreadPoolTaskExecutor().submit(thread);
    }

    @SuppressWarnings("unused")
    @EventListener(value = ContextClosedEvent.class)
    public void stopBinaryClient() throws IOException {
        log.info("====> starting disconnect bin log slave mysql ...");
        BinaryLogClient binaryLogClient = this.getBinaryLogClient();
        binaryLogClient.disconnect();
    }
}
