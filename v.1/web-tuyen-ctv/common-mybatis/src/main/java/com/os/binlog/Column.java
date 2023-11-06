package com.os.binlog;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Column {
    /**Tên bảng schema*/
    private String schema;
    /**mã id của bảng*/
    private String table;
    /**tên cột*/
    private String columnName;
    /**loại cột*/
    private String columnType;
    /**vị trí của cột trong bảng từ 0 -> n*/
    private int index;
}
