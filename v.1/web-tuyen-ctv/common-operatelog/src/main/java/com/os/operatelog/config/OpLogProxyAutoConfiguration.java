package com.os.operatelog.config;

import com.os.operatelog.annotation.EnableOpLog;
import com.os.operatelog.aop.OpLogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

@Slf4j
@Configuration
public class OpLogProxyAutoConfiguration implements ImportAware {

    private AnnotationAttributes opLogRecord;

    @Bean
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    public OpLogInterceptor opLogRecord(){
        return new OpLogInterceptor();
    }

    @Override
    public void setImportMetadata(AnnotationMetadata annotationMetadata) {
        this.opLogRecord =
                AnnotationAttributes.fromMap(
                        annotationMetadata
                                .getAnnotationAttributes(EnableOpLog.class.getName(), false));
        if(opLogRecord == null)
            log.info("=====> {}", "@EnableOpLog is not present in importing annotation");
    }
}
