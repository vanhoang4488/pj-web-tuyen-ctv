package vanhoang.project.binlog.bean;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Getter;
import vanhoang.project.config.BinLogClientConfig;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class BinlogItem {

    private EventType eventType;
    private String tableName;
    private List<Map<String, Serializable>> beforeRows;
    private List<Map<String, Serializable>> afterRows;

    public void setInsertOrDeleteRows (EventType eventType, String tableName, List<Serializable[]> rows) {
        this.eventType = eventType;
        this.tableName = tableName;
        List<String> fieldNames = BinLogClientConfig.BINLOG_ENTITY_FIELD_MAP.get(tableName);
        for (Serializable[] row : rows) {
            Map<String, Serializable> rowMap = new HashMap<>();
            for(int index = 0; index < row.length; index++) {
                rowMap.put(fieldNames.get(index), row[index]);
            }
            if (EventType.isWrite(this.eventType)) {
                afterRows = new ArrayList<>();
                this.afterRows.add(rowMap);
            }
            else if (EventType.isDelete(this.eventType)) {
                beforeRows = new ArrayList<>();
                this.beforeRows.add(rowMap);
            }
        }
    }

    public void setUpdateRows(EventType eventType,
                      String tableName,
                      List<Map.Entry<Serializable[], Serializable[]>> updateRows) {
        this.eventType = eventType;
        this.tableName = tableName;
        List<String> fieldNames = BinLogClientConfig.BINLOG_ENTITY_FIELD_MAP.get(tableName);
        for (Map.Entry<Serializable[], Serializable[]> updateRow : updateRows) {
            Map<String, Serializable> beforeMap = new HashMap<>();
            Map<String, Serializable> afterMap = new HashMap<>();
            for(int index = 0; index < updateRow.getKey().length; index++) {
                beforeMap.put(fieldNames.get(index), updateRow.getKey()[index]);
                afterMap.put(fieldNames.get(index), updateRow.getValue()[index]);
            }
            this.beforeRows = new ArrayList<>();
            this.afterRows = new ArrayList<>();
            this.beforeRows.add(beforeMap);
            this.afterRows.add(afterMap);
        }
    }

    @Override
    public String toString() {
        return "EventType: " + this.eventType.name() + "--"
                + "TableName: " + this.tableName + "--"
                + "BeforeRows: " + this.beforeRows + "--"
                + "AfterRows: " + this.afterRows;
    }
}
