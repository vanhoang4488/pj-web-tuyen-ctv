package vanhoang.project.config;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import vanhoang.project.binlog.bean.BinlogItem;
import vanhoang.project.binlog.handle.base.BinLogHandle;
import vanhoang.project.utils.BeanUtils;

import java.util.*;

@Slf4j
public class CustomBinLogEventListener implements BinaryLogClient.EventListener {
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
            if (BinLogClientConfig.BINLOG_ENTITY_MAP.containsKey(tableName)) {
                BinlogItem writeBinlogItem = new BinlogItem();
                writeBinlogItem.setInsertOrDeleteRows(eventType, tableName, writeRowsEventData.getRows());
                this.submit(writeBinlogItem);
            }
        }
        else if (EventType.isDelete(eventType)) {
            DeleteRowsEventData deleteRowsEventData = event.getData();
            String tableName = tableMap.get(deleteRowsEventData.getTableId());
            if (BinLogClientConfig.BINLOG_ENTITY_MAP.containsKey(tableName)) {
                BinlogItem deleteBinlogItem = new BinlogItem();
                deleteBinlogItem.setInsertOrDeleteRows(eventType, tableName, deleteRowsEventData.getRows());
                this.submit(deleteBinlogItem);
            }
        }
        else if (EventType.isUpdate(eventType)) {
            UpdateRowsEventData updateRowsEventData = event.getData();
            String tableName = tableMap.get(updateRowsEventData.getTableId());
            if (BinLogClientConfig.BINLOG_ENTITY_MAP.containsKey(tableName)) {
                BinlogItem updateBinlogItem = new BinlogItem();
                updateBinlogItem.setUpdateRows(eventType, tableName, updateRowsEventData.getRows());
                this.submit(updateBinlogItem);
            }
        }
    }

    private void submit(BinlogItem binlogItem) {
        String tableName = binlogItem.getTableName();
        BinLogHandle binLogHandle = BinLogClientConfig.BINLOG_ENTITY_LISTENER_MAP.get(tableName);
        ThreadPoolTaskExecutor threadPoolTaskExecutor = BeanUtils.getBean(ThreadPoolTaskExecutor.class);
        threadPoolTaskExecutor.submit(() -> binLogHandle.handle(binlogItem));
    }
}
