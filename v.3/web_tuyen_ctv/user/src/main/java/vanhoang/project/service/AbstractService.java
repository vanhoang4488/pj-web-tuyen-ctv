package vanhoang.project.service;

import org.mapstruct.factory.Mappers;
import vanhoang.project.convertor.BaseConvertor;
import vanhoang.project.dto.base.BaseDTO;
import vanhoang.project.entity.BaseEntity;

import java.util.Optional;

public class AbstractService{

    public BaseDTO convertToDTO(Optional<? extends BaseEntity> optionalEntity, Class<? extends BaseConvertor> clazz) {
        if (optionalEntity.isPresent()) {
            return (BaseDTO) Mappers.getMapper(clazz).<BaseDTO, BaseEntity>convertToDTO(optionalEntity.get());
        }
        return new BaseDTO();
    }
}
