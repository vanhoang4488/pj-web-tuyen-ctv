package vanhoang.project.annotation;

import vanhoang.project.entity.base.BaseEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BinlogEntityListener {

    Class<? extends BaseEntity> listen();
}
