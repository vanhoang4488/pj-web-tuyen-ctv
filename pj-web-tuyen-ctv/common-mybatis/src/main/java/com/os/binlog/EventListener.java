package com.os.binlog;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class EventListener implements BinaryLogClient.EventListener {

    private final DataSourceConf conf;
    private final ThreadPoolTaskExecutor executor;
    private final Map<Long, Map<String, Column>> tableMap;
    private final Map<String, HandleBinLog> handles;

    @Override
    public void onEvent(Event event) {
        EventType type = event.getHeader().getEventType();

        EventData data = event.getData();
        if(type == EventType.TABLE_MAP){
            TableMapEventData tableMapEventData = (TableMapEventData) data;
            log.info("------>>> binlog tableId: {}, table name: {}",
                    tableMapEventData.getTableId(), tableMapEventData.getTable());
        }

        if(EventType.isWrite(type)){
            WriteRowsEventData writeData = (WriteRowsEventData) data;
            this.setupInsertOrDelete(writeData.getRows(), writeData.getTableId(), event.getHeader());
        }
        else if(EventType.isDelete(type)){
            DeleteRowsEventData delData = (DeleteRowsEventData) data;
            this.setupInsertOrDelete(delData.getRows(), delData.getTableId(), event.getHeader());
        }
        else if(EventType.isUpdate(type)){
            UpdateRowsEventData updateData = (UpdateRowsEventData) data;
            this.setupUpdate(updateData.getRows(), updateData.getTableId(), event.getHeader());
        }
    }

    private void setupInsertOrDelete(List<Serializable[]> rows, Long tableId, EventHeader eventHeader){
        for(Serializable[] row : rows){
            BinLogItem item = BinLogItem.fromItemByWriteOrDelete(row, tableMap.get(tableId), eventHeader);
            item.setDatabase(conf.getDatabase());
            this.execute(item);
        }
    }

    private void setupUpdate(List<Map.Entry<Serializable[], Serializable[]>> rows, Long tableId, EventHeader eventHeader){
        for(Map.Entry<Serializable[], Serializable[]> row : rows){
            BinLogItem item = BinLogItem.fromItemByUpdate(row, tableMap.get(tableId), eventHeader);
            item.setDatabase(conf.getDatabase());
            this.execute(item);
        }
    }

    private void execute(BinLogItem item){
        executor.submit(new Runnable() {
            @Override
            public void run() {
                String dbTable = BinLogUtil.getDbTable(item.getDatabase(), item.getTable());
                handles.get(dbTable).handle(item);
            }
        });
    }
}
