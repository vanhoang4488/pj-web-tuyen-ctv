package com.os.binlog;

import com.os.config.BeanUtils;
import com.os.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public final class TourBinLog {

    private volatile static TourBinLog instance;
    private static Map<String, BinLogListener> listeners;

    private TourBinLog(){
        listeners = new HashMap<>();
    }

    public static TourBinLog getInstance(){
        if(instance == null){
            synchronized (TourBinLog.class){
                if (instance == null){
                    instance = new TourBinLog();
                }
            }
        }

        return instance;
    }

    public void addListener(String tableName, BinLogListener listener){
        listeners.put(tableName, listener);
    }

    public static void startBinLog(){
        log.info("=====> start binlog listener...");
        ThreadPoolTaskExecutor executor = BeanUtils.getBean(ThreadPoolTaskExecutor.class);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    DataSourceConf conf = BeanUtils.getBean(DataSourceConf.class);

                    MysqlBinlogListener binlogListener = new MysqlBinlogListener(conf);

                    Set<String> tableNames = StringUtils.getIdSet(conf.getTableNames());

                    tableNames.forEach(tableName -> {
                        binlogListener.addColumnTableAndHandle(tableName, item -> {
                            if(listeners.containsKey(tableName)){
                                listeners.get(tableName).listen(item);
                            }
                        });
                    });

                    binlogListener.onConnect();
                }
                catch (Exception ex){
                    log.error("=====> start binlog listener failed: {}", ex.getMessage(), ex);
                }
            }
        });
    }
}
