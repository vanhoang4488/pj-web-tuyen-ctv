package com.os.util;

import com.os.tenant.TenantConstants;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class StatementParser {

    public static String parse(Statement statement) throws JSQLParserException {
        String tenantId = TenantConstants.TENANT_THREAD_LOCAL.get();
        if(!StringUtils.isNotEmpty(tenantId)){
            ServletRequestAttributes requestAttributes  =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(!Objects.isNull(requestAttributes)){
                HttpServletRequest request = requestAttributes.getRequest();
                tenantId = request.getParameter(TenantConstants.TENANT_ID_NAME);
                log.info("------>>> is request tenantId: {}", tenantId);
            }
        }
        else{
            log.info("------>>> is thread local tenantId: {}", tenantId);
        }

        if(!StringUtils.isNotEmpty(tenantId)) return statement.toString();

        if(statement instanceof Insert)
            return parseInsert((Insert) statement, tenantId);
        else if(statement instanceof Update)
            return parseUpdate((Update) statement, tenantId);
        else if(statement instanceof Delete)
            return parseDelete((Delete) statement, tenantId);
        else
            return parseSelect((Select) statement, tenantId);
    }

    private static String parseInsert(Insert insert, String tenantId){
        // thêm cột tenantId.
        Column column = new Column();
        column.setColumnName(TenantConstants.TENANT_ID_NAME);
        insert.addColumns(column);

        ItemsList itemList = insert.getItemsList();
        List<ExpressionList> expressionLists = new ArrayList<>(50);
        setExpressionList(itemList, expressionLists);

        itemList.accept(new ItemsListVisitorAdapter() {
            @Override
            public void visit(ExpressionList expressionList){

                expressionList.addExpressions(new StringValue(tenantId));
            }
        });

        return insert.toString();
    }

    private static void setExpressionList(ItemsList itemList,
                                                  List<ExpressionList> expressionLists){
        if(itemList instanceof MultiExpressionList){
            MultiExpressionList multiExpressionList = (MultiExpressionList) itemList;
            expressionLists.addAll(multiExpressionList.getExpressionLists());
        }
        else{
            ExpressionList expressions = (ExpressionList) itemList;
            expressionLists.add(expressions);
        }
    }

    private static String parseUpdate(Update update, String tenantId) throws JSQLParserException {
        Expression expression = update.getWhere();
        expression = addExpressionOnWhere(expression, tenantId);
        update.setWhere(expression);
        return update.toString();
    }

    private static String parseDelete(Delete delete, String tenantId) throws JSQLParserException {
        Expression expression = delete.getWhere();
        expression = addExpressionOnWhere(expression, tenantId);
        delete.setWhere(expression);
        return delete.toString();
    }

    private static String parseSelect(Select select, String tenantId) throws JSQLParserException {
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        Expression expression = plainSelect.getWhere();
        expression = addExpressionOnWhere(expression, tenantId);
        plainSelect.setWhere(expression);
        return select.toString();
    }

    private static Expression addExpressionOnWhere(Expression expression, String tenantId) throws JSQLParserException {
        String extraSql = String.format("`%s` = %s", TenantConstants.TENANT_ID_NAME, tenantId);
        Expression extraExpression = CCJSqlParserUtil.parseExpression(extraSql);
        AndExpression andExpression = new AndExpression(expression, extraExpression);
        return andExpression;
    }
}
