package vanhoang.project.convertor;

import org.mapstruct.Mapper;
import vanhoang.project.dto.UserDTO;
import vanhoang.project.entity.UserEntity;

@Mapper
public interface UserConvertor extends BaseConvertor<UserDTO, UserEntity> {

}
