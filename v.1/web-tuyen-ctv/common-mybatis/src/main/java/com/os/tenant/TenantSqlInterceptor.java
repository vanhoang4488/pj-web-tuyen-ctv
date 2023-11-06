package com.os.tenant;

import com.os.util.StatementParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Statement;

@Slf4j
@Component
@Intercepts(value = {
        @Signature(type = StatementHandler.class, method = "query",
                args = {Statement.class, ResultHandler.class})
})
public class TenantSqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // lấy ra được boundSql.
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject =
                MetaObject.forObject(
                        statementHandler,
                        new DefaultObjectFactory(),
                        new DefaultObjectWrapperFactory(),
                        new DefaultReflectorFactory());
        MappedStatement mappedStatement =
                (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        SqlCommandType commandType = mappedStatement.getSqlCommandType();
        log.info("=====> intercept sql command type: {}", commandType);

        BoundSql boundSql = statementHandler.getBoundSql();

        // thay đổi giá trị sql.
        Statement statement = (Statement) invocation.getArgs()[0];
        String new_sql = StatementParser.parse(statement);

        // gán lại sql vào boundSql.
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, new_sql);
        return invocation.proceed();
    }

}
