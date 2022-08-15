package com.os.operatelog.annotation;

import com.os.operatelog.config.OpLogConfigurationSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(OpLogConfigurationSelector.class)
public @interface EnableOpLog {

    AdviceMode mode() default AdviceMode.PROXY;
}
