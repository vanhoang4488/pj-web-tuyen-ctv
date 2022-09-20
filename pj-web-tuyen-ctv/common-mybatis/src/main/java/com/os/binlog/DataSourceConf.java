package com.os.binlog;

import com.os.util.StringUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "binlog.config")
public class DataSourceConf {

    private String host;
    private int port;
    private String username;
    private String password;

    private String database;
    /**
     * danh sách các bảng cần ghi lại thông tin binlog,
     * được phân cách bởi dấu ","
     */
    private String tableNames;

    //@Value("${binlog.config.threadpool.core.size}")
    private int nThread;
    //@Value("${binlog.config.threadpool.queue.size}")
    private int queueSize;

    public String getTableNames(){
        if(!StringUtils.isNotEmpty(tableNames)) return "";
        return tableNames.toLowerCase();
    }
}
