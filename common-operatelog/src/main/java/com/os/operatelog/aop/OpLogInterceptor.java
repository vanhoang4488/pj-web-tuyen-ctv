package com.os.operatelog.aop;

import com.google.gson.Gson;
import com.os.entity.OpLogRecord;
import com.os.listener.QueueName;
import com.os.listener.QueueSender;
import com.os.operatelog.annotation.OpLog;
import com.os.operatelog.util.ParameterUtils;
import com.os.result.ResultEntity;
import com.os.util.CommonConstants;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

@Component
public class OpLogInterceptor implements MethodInterceptor {

    @Autowired
    private QueueSender queueSender;

    @Autowired
    private Gson gson;

    private static final ThreadLocal<String>  SYS_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUserId(String userId){
        SYS_THREAD_LOCAL.set(userId);
    }

    public static String getUserId(){
        String userId = SYS_THREAD_LOCAL.get();
        SYS_THREAD_LOCAL.remove();
        return userId;
    }


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        return executor(invocation, invocation.getThis(), method, invocation.getArguments());
    }

    private Object executor(MethodInvocation invocation, Object target, Method method, Object[] args){
        Class<?> targetClass = target.getClass();
        Object result = null;
        Throwable throwable = null;
        try {
            result = invocation.proceed();
        } catch (Throwable e) {
            throwable = e;
        }

        if(!Objects.isNull(result)){
            ResultEntity entity = (ResultEntity) result;
            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = null;
            if(!Objects.isNull(requestAttributes))
                request = requestAttributes.getRequest();

            // nếu trạng thái request trả về là true thì mới tiến hành xử lý.
            // và request còn tồn tại.
            if(entity.isOk() && !Objects.isNull(request)){
                String requestClient = request.getHeader(CommonConstants.REQUEST_CLIENT.getKey());

                // chỉ yêu cầu với vai trò là admin mới được xét.
                if(requestClient.equals("admin")){
                    //danh sách tên tham số của phương thức.
                    Map<String, Object> parameters = getParametersMap(method, args);
                    OpLog opLog = method.getAnnotation(OpLog.class);
                    OpLogRecord opLogRecord = ParameterUtils.analyze(opLog, parameters, request);
                    queueSender.onMessage(QueueName.SYS_ADMIN, gson.toJson(opLogRecord));
                }
            }
        }

        if(!Objects.isNull(throwable))
            return throwable;
        return result;
    }

    private Map<String, Object> getParametersMap(Method method, Object[] args){
        Map<String, Object> parameters = new HashMap<>();
        String[] parameterNames = new DefaultParameterNameDiscoverer().getParameterNames(method);
        if(!Objects.isNull(parameterNames) && parameterNames.length > 0){
            IntStream.range(0, parameterNames.length).forEach(index -> {
                parameters.put(parameterNames[index], args[index]);
            });
        }
        return parameters;
    }

}
