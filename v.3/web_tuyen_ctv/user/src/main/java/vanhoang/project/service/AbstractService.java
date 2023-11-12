package vanhoang.project.service;

//import org.mapstruct.factory.Mappers;
import vanhoang.project.dto.base.BaseDTO;
import vanhoang.project.entity.BaseEntity;

import java.util.Optional;

public class AbstractService<C>{

    public BaseDTO convertToDTO(Optional<? extends BaseEntity> optionalEntity, Class<C> clazz) {
//        if (optionalEntity.isPresent()) {
//            Class<? extends  C> implementClazz = Mappers.getMapperClass(clazz);
//            try {
//                Method convertMethod = implementClazz.getDeclaredMethod("convert", BaseEntity.class);
//                Object object = convertMethod.invoke(Mappers.getMapper(clazz), optionalEntity.get());
//                return (BaseDTO) object;
//            } catch (NoSuchMethodException e) {
//                throw new RuntimeException(e);
//            } catch (InvocationTargetException e) {
//                throw new RuntimeException(e);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        }
        return new BaseDTO();
    }
}
