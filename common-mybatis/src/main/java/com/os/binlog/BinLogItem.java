package com.os.binlog;

import com.github.shyiko.mysql.binlog.event.EventHeader;
import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class BinLogItem {

    private String database;
    private String table;
    private String type;
    private long serverId;
    private long timestamp;
    private Map<String, Serializable> before;
    private Map<String, Serializable> after;

    private BinLogItem() {};

    public static BinLogItem fromItemByWriteOrDelete(Serializable[] row,
                                                     Map<String, Column> columnMap,
                                                     EventHeader eventHeader){
        BinLogItem item = new BinLogItem();
        item.setType(eventHeader.getEventType().name());
        item.setServerId(eventHeader.getServerId());
        item.setTimestamp(eventHeader.getTimestamp());

        Map<String, Serializable> beOrAfter = new HashMap<>();
        columnMap.forEach((key, value) -> {
            if(item.getTable() == null) item.setTable(value.getTable());

            beOrAfter.put(key, row[value.getIndex()]);
        });

        if(EventType.isWrite(eventHeader.getEventType())) item.setAfter(beOrAfter);

        if(EventType.isDelete(eventHeader.getEventType())) item.setBefore(beOrAfter);

        return item;
    }

    public static BinLogItem fromItemByUpdate(Map.Entry<Serializable[], Serializable[]> row,
                                              Map<String, Column> columnMap,
                                              EventHeader eventHeader){
        BinLogItem item = new BinLogItem();
        item.setType(eventHeader.getEventType().name());
        item.setServerId(eventHeader.getServerId());
        item.setTimestamp(eventHeader.getTimestamp());

        Map<String, Serializable> before = new HashMap<>();
        Map<String, Serializable> after = new HashMap<>();
        columnMap.forEach((key, value) -> {
            if(item.getTable() == null) item.setTable(value.getTable());

            Serializable befValue = row.getKey()[value.getIndex()];
            before.put(key, befValue);

            Serializable afValue = row.getValue()[value.getIndex()];
            after.put(key, afValue);
        });

        item.setBefore(before);
        item.setAfter(after);
        
        return item;
    }
}
