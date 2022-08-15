package com.os;

import lombok.Data;
import java.util.Date;

@Data
public class OpLogRecord {

    private String detail;
    private String describe;
    private String bizId;
    private String bizTable;
    private String opType;
    private Date createTime;
    private String userId;
    private String requestIp;
}
