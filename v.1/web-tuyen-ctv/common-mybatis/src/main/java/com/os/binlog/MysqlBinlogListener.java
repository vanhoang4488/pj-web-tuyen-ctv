package com.os.binlog;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Lớp lắng nghe các binlog trong mysql
 */

@Slf4j
public class MysqlBinlogListener {

    private final DataSourceConf conf;

    /**Thread pool xử lý BinLogItem*/
    private final ThreadPoolTaskExecutor executor;
    private final Map<Long, Map<String, Column>> tableMap;
    private final Map<String, HandleBinLog> handles;

    public MysqlBinlogListener(DataSourceConf conf) {
        this.conf = conf;
        this.tableMap = new HashMap<>();
        this.handles = new HashMap<>();

        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(conf.getNThread());
        executor.setCorePoolSize(conf.getNThread());
        executor.setQueueCapacity(conf.getQueueSize());
        executor.setThreadNamePrefix("binlog-task-");

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
    }

    public void onConnect() throws IOException {
        BinaryLogClient client =
                new BinaryLogClient(conf.getHost(),
                                    conf.getPort(),
                                    conf.getUsername(),
                                    conf.getPassword());

        client.registerEventListener(new EventListener(conf, executor, tableMap, handles));
        client.connect();
    }

    public void addColumnTableAndHandle(String tableName, HandleBinLog handle){
        String dbTable = BinLogUtil.getDbTable(conf.getDatabase(), tableName);
        BinLogUtil.setColumn(tableMap, conf, dbTable);
        log.info("=====> add all columns to {}", tableName);
        handles.put(dbTable, handle);
        log.info("=====> add handle for {} successfully", dbTable);
    }
}
