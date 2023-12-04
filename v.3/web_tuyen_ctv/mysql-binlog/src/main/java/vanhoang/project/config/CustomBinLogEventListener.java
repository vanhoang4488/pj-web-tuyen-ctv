package vanhoang.project.config;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import vanhoang.project.annotation.BinlogEntityListener;
import vanhoang.project.binlog.bean.BinlogItem;
import vanhoang.project.binlog.handle.base.BinLogHandle;
import vanhoang.project.entity.base.BaseEntity;
import vanhoang.project.utils.BeanUtils;

import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Slf4j
@Component
public class CustomBinLogEventListener implements BinaryLogClient.EventListener {
    private final static String PACKAGE_ENTITY_NAME = "vanhoang.project.binlog.handle";
    public final static Map<String, BinLogHandle> BINLOG_ENTITY_LISTENER_MAP = new HashMap<>();
    public final static Map<String, Class<? extends BaseEntity>> BINLOG_ENTITY_MAP = new HashMap<>();
    private final HashMap<Long, String> tableMap = new HashMap<>();

    @Override
    public void onEvent(Event event) {
        EventType eventType = event.getHeader().getEventType();
        if (eventType == EventType.TABLE_MAP) {
            TableMapEventData tableMapEventData = event.getData();
            Long tableId = tableMapEventData.getTableId();
            if (!tableMap.containsKey(tableId)) {
                tableMap.put(tableId, tableMapEventData.getTable());
            }
        }
        else if (EventType.isWrite(eventType)) {
            WriteRowsEventData writeRowsEventData = event.getData();
            String tableName = tableMap.get(writeRowsEventData.getTableId());
            BinlogItem writeBinlogItem = new BinlogItem();
            writeBinlogItem.setInsertOrDeleteRows(eventType, tableName, writeRowsEventData.getRows());
            this.submit(writeBinlogItem);
        }
        else if (EventType.isDelete(eventType)) {
            DeleteRowsEventData deleteRowsEventData = event.getData();
            String tableName = tableMap.get(deleteRowsEventData.getTableId());
            BinlogItem deleteBinlogItem = new BinlogItem();
            deleteBinlogItem.setInsertOrDeleteRows(eventType, tableName, deleteRowsEventData.getRows());
            this.submit(deleteBinlogItem);
        }
        else if (EventType.isUpdate(eventType)) {
            UpdateRowsEventData updateRowsEventData = event.getData();
            String tableName = tableMap.get(updateRowsEventData.getTableId());
            BinlogItem updateBinlogItem = new BinlogItem();
            updateBinlogItem.setUpdateRows(eventType, tableName, updateRowsEventData.getRows());
            this.submit(updateBinlogItem);
        }
    }

    private void submit(BinlogItem binlogItem) {
        String tableName = binlogItem.getTableName();
        BinLogHandle binLogHandle = BINLOG_ENTITY_LISTENER_MAP.get(tableName);
        ThreadPoolTaskExecutor threadPoolTaskExecutor = BeanUtils.getBean(ThreadPoolTaskExecutor.class);
        threadPoolTaskExecutor.submit(() -> binLogHandle.handle(binlogItem));
    }

    @EventListener(classes = {ApplicationStartedEvent.class})
    public void scanBinLogEntity() {
        Set<File> files = new HashSet<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try{
            Enumeration<URL> resources = classLoader.getResources(PACKAGE_ENTITY_NAME.replaceAll("\\.", "/"));
            while(resources.hasMoreElements()) {
                URL url = resources.nextElement();
                files.add(new File(url.getFile()));
            }
            for (File directory : files)
                this.scan(directory, PACKAGE_ENTITY_NAME);
            Thread runnable = new Thread(() -> {
                BinaryLogClient binaryLogClient = BeanUtils.getBean(BinaryLogClient.class);
                try {
                    binaryLogClient.connect();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            runnable.start();
            log.info("====> connected binlog slave mysql success: {}, {}", BINLOG_ENTITY_MAP, BINLOG_ENTITY_LISTENER_MAP);
        } catch (ClassNotFoundException e) {
            log.error("====> scan file contain annotation binlogEntity: not find file", e);
        } catch (IOException e) {
            log.error("====> scan file contain annotation binlogEntity: not fin package", e);
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
                }
            }
            else {
                scan(file, packageName + "." + file.getName());
            }
        }
    }
}
