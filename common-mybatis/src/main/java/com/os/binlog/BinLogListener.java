package com.os.binlog;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.*;

@Component
public class BinLogListener {

    private final DataSourceConf sourceConf;

    @Value("${binlog.config.threadpool.nThread}")
    private int nThread;

    @Value("${binlog.config.threadpool.queueSize}")
    private int queueSize;

    public BinLogListener(DataSourceConf sourceConf){
        this.sourceConf = sourceConf;
    }

    public void startBinLogEventListener() throws IOException {
        BinaryLogClient client =
                new BinaryLogClient(
                        sourceConf.getHost(),
                        sourceConf.getPort(),
                        sourceConf.getUsername(),
                        sourceConf.getPassword());
        client.registerEventListener(new EventListener(nThread, queueSize));
        client.connect();
    }

}
