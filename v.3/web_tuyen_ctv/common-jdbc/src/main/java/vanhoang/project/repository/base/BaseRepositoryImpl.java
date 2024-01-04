package vanhoang.project.repository.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.base.BaseEntity;
import vanhoang.project.entity.base.BaseEntityId;
import vanhoang.project.utils.GenerationID;
import vanhoang.project.utils.LocalDateTimeUtils;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Repository
public class BaseRepositoryImpl<T extends BaseEntity, ID> implements  BaseRepository<T, ID> {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private GenerationID generationId;

    @Override
    public T save(T entity) {
        return this.unsupportedSave();
    }

    @Override
    public List<T> saveAll(List<T> etities) {
        return Collections.singletonList(this.unsupportedSave());
    }

    @Override
    public T persist(T entity) {
        if (entity instanceof BaseEntityId) {
            ((BaseEntityId) entity).setId(generationId.generationUUID());
        }
        entity.setCreateTime(LocalDateTimeUtils.getNow());
        entity.setUpdateTime(LocalDateTimeUtils.getNow());
        this.setRelationTableId(entity);
        this.entityManager.persist(entity);
        return entity;
    }

    @Override
    public List<T> persistAll(List<T> entities) {
        for (T entity : entities) {
            this.entityManager.persist(entity);
        }
        return entities;
    }

    @Override
    public T merge(T entity) {
        entity.setUpdateTime(LocalDateTimeUtils.getNow());
        this.entityManager.merge(entity);
        return entity;
    }

    private T unsupportedSave() {
        throw new UnsupportedOperationException(
                "There's no such thing as a save method in JPA, so don't use this hack!"
        );
    }

    @SuppressWarnings("unchecked")
    private void setRelationTableId(T entity) {
        Class<?> entityClazz = entity.getClass();
        for (Field field : entityClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(OneToOne.class)) {
                OneToOne oneToOne = field.getAnnotation(OneToOne.class);
                CascadeType[] cascadeTypes = oneToOne.cascade();
                boolean check = this.checkCascadeType(cascadeTypes);
                if (check) {
                    field.setAccessible(true);
                    String methodName = this.getMethodName(oneToOne.mappedBy());
                    try {
                        this.setValueForFeignObject(entity, field.get(entity), methodName);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            else if (field.isAnnotationPresent(OneToMany.class)) {
                OneToMany oneToMany = field.getAnnotation(OneToMany.class);
                CascadeType[] cascadeTypes = oneToMany.cascade();
                boolean check = this.checkCascadeType(cascadeTypes);
                if (check) {
                    field.setAccessible(true);
                    String methodName = this.getMethodName(oneToMany.mappedBy());
                    List<Object> feignObjects;
                    try {
                        feignObjects = (List<Object>) field.get(entity);
                        for (Object feignObject : feignObjects) {
                            this.setValueForFeignObject(entity, feignObject, methodName);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private boolean checkCascadeType(CascadeType[] cascadeTypes) {
        boolean check = false;
        for (CascadeType cascadeType : cascadeTypes) {
            if (CascadeType.PERSIST == cascadeType || CascadeType.ALL == cascadeType) {
                check = true;
                break;
            }
        }
        return check;
    }

    private String getMethodName(String mappedBy) {
        return "set" +
                mappedBy.substring(0,1).toUpperCase() +
                mappedBy.substring(1);
    }

    private void setValueForFeignObject(T entity, Object feignObject, String methodName) {
        Class<?> entityClazz = entity.getClass();
        try {
            Class<?> feignClazz = feignObject.getClass();
            Method mappedByMethod = feignClazz.getMethod(methodName, entityClazz);
            mappedByMethod.invoke(feignObject, entity);

            // set id
            if (entity instanceof BaseEntityId) {
                Method setIdMethod = feignClazz.getMethod("setId", Long.class);
                setIdMethod.invoke(feignObject, this.generationId.generationUUID());
            }

            // set create_time
            Method setCreateTimeMethod = feignClazz.getMethod("setCreateTime", Date.class);
            setCreateTimeMethod.invoke(feignObject, LocalDateTimeUtils.getNow());

            // set create_time
            Method setUpdateTimeMethod = feignClazz.getMethod("setUpdateTime0", Date.class);
            setUpdateTimeMethod.invoke(feignObject, LocalDateTimeUtils.getNow());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            throw new RuntimeException(e);
        }
    }
}
