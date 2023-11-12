package vanhoang.project.convertor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vanhoang.project.dto.UserDTO;
import vanhoang.project.entity.UserEntity;

@Mapper
public interface UserConvertor{
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "fullName", target = "fullName")
//    @Mapping(source = "entity.email", target = "email")
//    @Mapping(source = "entity.birthDay", target = "birthDay")
//    @Mapping(source = "entity.gender", target = "gender")
    UserDTO convert(UserEntity entity);
    UserEntity convert(UserDTO dtO);
}
