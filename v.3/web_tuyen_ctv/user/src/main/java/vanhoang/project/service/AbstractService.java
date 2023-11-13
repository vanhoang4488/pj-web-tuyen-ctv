package vanhoang.project.service;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import vanhoang.project.dto.base.BaseDTO;
import vanhoang.project.entity.BaseEntity;

import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
public abstract class AbstractService<O extends BaseDTO, I extends BaseEntity>{

    public O convertToDTO(Optional<I> optionalEntity, Class<?> clazz) {
        if (optionalEntity.isPresent()) {
            Class<?> implConvertClazz = Mappers.getMapperClass(clazz);
            if (implConvertClazz != null) {
                Class<? extends BaseEntity> argClazz = optionalEntity.get().getClass();
                try {
                    Method convertToDtoMethod = implConvertClazz.getDeclaredMethod("convert", argClazz);
                    Object dtoObject = convertToDtoMethod.invoke(Mappers.getMapper(clazz), optionalEntity.get());
                    return (O) dtoObject;
                } catch (Exception e) {
                    log.error("-----> API in @Service convert failed entity -> dto, {}", e.getMessage(), e);
                }
            }
        }
        return null;
    }
}
