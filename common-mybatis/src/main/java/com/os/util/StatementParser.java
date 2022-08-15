package com.os.util;

import com.os.tenant.TenantConstants;
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

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class StatementParser {

    public static String parse(Statement statement) throws JSQLParserException {

        if(statement instanceof Insert)
            return parseInsert((Insert) statement);
        else if(statement instanceof Update)
            return parseUpdate((Update) statement);
        else if(statement instanceof Delete)
            return parseDelete((Delete) statement);
        else
            return parseSelect((Select) statement);
    }

    private static String parseInsert(Insert insert){
        // thêm cột tenantId.
        Column column = new Column();
        column.setColumnName(TenantConstants.TENANT_ID);
        insert.addColumns(column);

        ItemsList itemList = insert.getItemsList();
        List<ExpressionList> expressionLists = new ArrayList<>(50);
        setExpressionList(itemList, expressionLists);

        String tenantId = TenantConstants.TENANT_THREAD_LOCAL.get();
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

    private static String parseUpdate(Update update) throws JSQLParserException {
        Expression expression = update.getWhere();
        expression = addExpressionOnWhere(expression);
        update.setWhere(expression);
        return update.toString();
    }

    private static String parseDelete(Delete delete) throws JSQLParserException {
        Expression expression = delete.getWhere();
        expression = addExpressionOnWhere(expression);
        delete.setWhere(expression);
        return delete.toString();
    }

    private static String parseSelect(Select select) throws JSQLParserException {
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        Expression expression = plainSelect.getWhere();
        expression = addExpressionOnWhere(expression);
        plainSelect.setWhere(expression);
        return select.toString();
    }

    private static Expression addExpressionOnWhere(Expression expression) throws JSQLParserException {
        String tenantId = TenantConstants.TENANT_THREAD_LOCAL.get();
        String extraSql = String.format("`%s` = %s", TenantConstants.TENANT_ID, tenantId);
        Expression extraExpression = CCJSqlParserUtil.parseExpression(extraSql);
        AndExpression andExpression = new AndExpression(expression, extraExpression);
        return andExpression;
    }
}
