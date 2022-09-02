package com.os.binlog;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class BinLogUtil {

    private static final String DELEMITER = ",";

    public  static String getDbTable(String dbName, String tableName){
        return dbName + DELEMITER + tableName;
    }

    /**
     * Lấy ra danh sách cột của bảng.
     * @param conf
     * @param tableName
     * @return
     */
    public static void setColumn(Map<Long, Map<String, Column>> tableMap,
                                DataSourceConf conf, String tableName){
        Connection connection = null;
        PreparedStatement pstm = null;
        try{
            Class.forName("com.mysl.cj.jdbc.Driver");
            String url = String.format("jabc:mysql://%s:%s/%s",
                                        conf.getHost(), conf.getPort(), conf.getDatabase());
            connection = DriverManager.getConnection(url, conf.getUsername(), conf.getPassword());

            String sql = "SELECT TABLE_SCHEMA, TABLE_ID, TABLE_NAME, COLUMN_NAME, DATA_TYPE, ORDINARY_POSITION " +
                    "FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
            pstm = connection.prepareStatement(sql);

            pstm.setString(1, conf.getDatabase());
            pstm.setString(2, tableName);

            Map<String, Column> columnMap = new HashMap<>();
            ResultSet result = pstm.executeQuery();
            Long tableId = -1L;
            while(result.next()){
                String schema = result.getString(1);
                tableId = result.getLong(2);
                String table = result.getString(3);
                String columnName = result.getString(4);
                String dataType = result.getString(5);
                int index = result.getInt(6);

                columnMap.put(columnName, new Column(schema, table, columnName, dataType, index));
            }

            if(columnMap.isEmpty()) log.info("------>>> {} add columns failed", tableName);
            else tableMap.put(tableId, columnMap);
        }
        catch (Exception ex){
            log.error("====> set Column failed: {}", ex.getMessage(), ex);
        }
        finally {
            try{
                if(connection != null) connection.close();
                if(pstm != null) pstm.close();
            }catch (Exception ex){
                log.error("=====> connection or preparedStatement closed failed: {}", ex.getMessage(), ex);
            }
        }
    }

}
