package vanhoang.project.interceptor;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import vanhoang.project.convert.SqlParser;

import java.lang.reflect.Field;
import java.util.concurrent.Executor;

@Component
@Intercepts(
        value = {
                @Signature(type = Executor.class, method = "query", args={MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
        }
)
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        BoundSql boundSql = mappedStatement.getBoundSql(invocation.getArgs()[1]);
        String oldSql = boundSql.getSql();
        String newSql = SqlParser.convertNewSql(oldSql);
        Class<? extends BoundSql> clazz = boundSql.getClass();
        Field field = clazz.getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, newSql);
        return invocation.proceed();
    }
}
