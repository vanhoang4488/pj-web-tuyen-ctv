package vanhoang.project.config;

import org.hibernate.dialect.MySQLDialect;

public class CustomMysqlDialect extends MySQLDialect {

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    }
}
