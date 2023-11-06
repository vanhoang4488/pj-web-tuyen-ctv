package vanhoang.project.convert;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.springframework.boot.context.event.SpringApplicationEvent;

import java.util.ArrayList;
import java.util.List;

public class SqlParser {

    public static String convertNewSql(String oldSql) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(oldSql);
        if (statement instanceof Insert) {
            return convertInsertSql((Insert) statement);
        }
        else if (statement instanceof Update) {
            return convertUpdateSql((Update) statement);
        }
        else {
            return oldSql;
        }
    }

    private static String convertInsertSql(Insert insert) {
        ItemsList itemsList = insert.getItemsList();
        itemsList.accept(new ItemsListVisitorAdapter() {
            @Override
            public void visit(ExpressionList expressionList) {
                System.out.println(expressionList.toString());
            }
        });
        return insert.toString();
    }

    private static String convertUpdateSql(Update update) {
        return update.toString();
    }
}
