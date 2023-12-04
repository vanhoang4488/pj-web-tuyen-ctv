package vanhoang.project.binlog.bean;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Getter;
import vanhoang.project.config.CustomBinLogEventListener;

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
        Class<?> clazz = CustomBinLogEventListener.BINLOG_ENTITY_MAP.get(tableName);
        List<String> fields = new ArrayList<>();
        this.getAllColumnField(fields, clazz);
        for (Serializable[] row : rows) {
            Map<String, Serializable> rowMap = new HashMap<>();
            for(int index = 0; index < row.length; index++) {
                rowMap.put(fields.get(index), row[index]);
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
        Class<?> clazz = CustomBinLogEventListener.BINLOG_ENTITY_MAP.get(tableName);
        List<String> fields = new ArrayList<>();
        this.getAllColumnField(fields, clazz);
        for (Map.Entry<Serializable[], Serializable[]> updateRow : updateRows) {
            Map<String, Serializable> beforeMap = new HashMap<>();
            Map<String, Serializable> afterMap = new HashMap<>();
            for(int index = 0; index < updateRow.getKey().length; index++) {
                beforeMap.put(fields.get(index), updateRow.getKey()[index]);
                afterMap.put(fields.get(index), updateRow.getValue()[index]);
            }
            this.beforeRows = new ArrayList<>();
            this.afterRows = new ArrayList<>();
            this.beforeRows.add(beforeMap);
            this.afterRows.add(afterMap);
        }
    }

    private void getAllColumnField(List<String> fields, Class<?> type) {
        if (type.getSuperclass() != null) {
            this.getAllColumnField(fields, type.getSuperclass());
        }
        else return;

        for (Field field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) ||
                    field.isAnnotationPresent(Id.class)) {
                fields.add(field.getName());
            }
            else if (field.isAnnotationPresent(JoinColumn.class)) {
                JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                fields.add(joinColumn.name());
            }
        }
    }
}
