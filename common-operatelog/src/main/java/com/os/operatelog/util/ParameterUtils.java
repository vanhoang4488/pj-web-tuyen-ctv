package com.os.operatelog.util;

import com.os.entity.OpLogRecord;
import com.os.operatelog.annotation.OpLog;
import com.os.operatelog.aop.OpLogInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParameterUtils {

    private static final Pattern pattern = Pattern.compile("\\{[a-zA-Z0-9.]+(\\(\\))?\\}");

    public static OpLogRecord analyze(OpLog annotation,
                               Map<String, Object> params,
                               HttpServletRequest request) {
        String describe = analyzeProperty(annotation.describe(), params);
        String detail = analyzeProperty(annotation.detail(), params);
        String bizId = analyzeProperty(annotation.bizId(), params);
        String bizTable = analyzeProperty(annotation.bizTable(), params);
        String opType = analyzeProperty(annotation.opType(), params);

        OpLogRecord opLogRecord = new OpLogRecord();
        opLogRecord.setDescribe(describe);
        opLogRecord.setDetail(detail);
        opLogRecord.setBizId(bizId);
        opLogRecord.setBizTable(bizTable);
        opLogRecord.setOpType(opType);
        opLogRecord.setCreateTime(new Date());

        // set userId.
        opLogRecord.setUserId(OpLogInterceptor.getUserId());

        // requestIp
        opLogRecord.setRequestIp(request.getRemoteAddr());
        return opLogRecord;
    }

    private static String analyzeProperty(String value, Map<String, Object> params){
        final StringBuilder content = new StringBuilder(value);
        Matcher matcher = pattern.matcher(value);
        List<String> expressions = new ArrayList<>();
        while(matcher.find()){
            String expression = matcher.group();
            expression = replaceSpaceAndBrace(expression);
            expressions.add(expression);
        }
        Map<Boolean, List<String>> expressionMap =
                expressions.stream().collect(Collectors.groupingBy(e -> e.indexOf(".") > -1));

        // với trường hợp @RequestParam. Ví dụ:  ${sysId}
        List<String> paramExpressions = expressionMap.get(false);
        if(!paramExpressions.isEmpty()){
            paramExpressions.stream().forEach(e -> {
                Object paramValue = params.get(e);
                replaceExpression(content, e, paramValue);
            });
        }

        // với trường hợp @RequestBody. Ví dụ: ${sysUser.sysId}
        List<String> bodyExpressions = expressionMap.get(true);
        if(!bodyExpressions.isEmpty()){
            bodyExpressions.stream().forEach(e -> {
                int index = e.indexOf(".");
                String objectKey = e.substring(0, index);
                String valueOfObject = e.substring(index + 1);
                Object object = params.get(objectKey);
                Object paramValue = null;
                try{
                    if(valueOfObject.endsWith("()")){
                        Method method =
                                object.getClass()
                                        .getMethod(
                                                valueOfObject.replaceAll("\\(\\)", ""));
                        method.setAccessible(true);
                        paramValue = method.invoke(object);
                    }
                    else {
                        Field field = object.getClass().getField(valueOfObject);
                        field.setAccessible(true);
                        paramValue = field.get(object);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                replaceExpression(content, e, paramValue);
            });
        }
        return content.toString();
    }

    private static String replaceSpaceAndBrace(String expression){
        expression = expression.replaceAll("\\s+", "");
        return expression.substring(1, expression.length()-1);
    }

    private static void replaceExpression(StringBuilder content, String expression, Object paramValue){
        expression = "{" + expression + "}";
        String value = "";
        if(!Objects.isNull(paramValue))
            value = paramValue.toString();

        int i = 0;
        while((i = content.indexOf(expression)) > -1){
            content.replace(i, i + expression.length(), value);
        }
    }
}
