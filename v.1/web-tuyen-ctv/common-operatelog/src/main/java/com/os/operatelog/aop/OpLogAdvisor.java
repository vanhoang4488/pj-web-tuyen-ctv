package com.os.operatelog.aop;

import com.os.operatelog.annotation.OpLog;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

@Component
public class OpLogAdvisor extends AbstractPointcutAdvisor {

    @Autowired
    private MethodInterceptor opLogInterceptor;

    private final Pointcut pointcut = new StaticMethodMatcherPointcut() {

        /**
         * chỉ những lớp có @RestController hoặc @Controller mới được sử dụng pointcut.
         */
        @Override
        public void setClassFilter(ClassFilter classFilter) {
            super.setClassFilter(new ClassFilter() {
                @Override
                public boolean matches(Class<?> clazz) {
                    return clazz.isAnnotationPresent(RestController.class) ||
                            clazz.isAnnotationPresent(Controller.class);
                }
            });
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass) {

            return method.isAnnotationPresent(OpLog.class);
        }
    };

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.opLogInterceptor;
    }
}
