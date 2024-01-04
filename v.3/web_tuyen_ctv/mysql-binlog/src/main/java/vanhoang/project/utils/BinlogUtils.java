package vanhoang.project.utils;

import lombok.extern.slf4j.Slf4j;
import vanhoang.project.entity.base.BaseEntity;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class BinlogUtils {

    public static void setColumnInfo
            (Map<String, List<String>> entityFieldMap, Map<String, Class<? extends BaseEntity>> entities) throws IOException, ClassNotFoundException, SQLException {
        String driver_class_name = BeanUtils.getEnvironmentProperty("spring.datasource.driver-class-name");
        if (!StringUtils.isNoneEmpty(driver_class_name))
            throw new IOException("driver-class-name could not null or empty");
        Class.forName(driver_class_name);
        String username = BeanUtils.getEnvironmentProperty("spring.datasource.username");
        String password = BeanUtils.getEnvironmentProperty("spring.datasource.password");
        String url = "jdbc:mysql://" +
                BeanUtils.getEnvironmentProperty("common.db.hostname") +
                ":" +
                BeanUtils.getEnvironmentProperty("common.db.port");
        Connection connection = DriverManager.getConnection(url, username, password);

        StringBuilder sql = new StringBuilder("select TABLE_NAME, COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS " +
                "where table_schema = ? and table_name in (");
        Set<String> tableNameSet = entities.keySet();
        int index = 0;
        for (String tableName : tableNameSet) {
            sql.append("'").append(tableName).append("'");
            if (index < tableNameSet.size() - 1) sql.append(",");
            index++;
        }
        sql.append(") " + "order by ordinal_position");
        PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
        preparedStatement.setString(1, BeanUtils.getEnvironmentProperty("common.db.database"));
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String table_name = resultSet.getString(1);
            String column_name = resultSet.getString(2);
            if (!entityFieldMap.containsKey(table_name)) {
                List<String> fieldNames = new ArrayList<>();
                fieldNames.add(column_name);
                entityFieldMap.put(table_name, fieldNames);
            }
            else {
                List<String> fieldNames = entityFieldMap.get(table_name);
                fieldNames.add(column_name);
            }
        }
        connection.close();
        log.info("====> disconnected to information_schema table");
    }
}
