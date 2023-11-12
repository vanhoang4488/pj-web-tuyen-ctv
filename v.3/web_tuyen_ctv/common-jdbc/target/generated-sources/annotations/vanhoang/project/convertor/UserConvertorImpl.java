package vanhoang.project.convertor;

import javax.annotation.processing.Generated;
import vanhoang.project.dto.UserDTO;
import vanhoang.project.entity.UserEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-12T22:05:16+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.14 (Oracle Corporation)"
)
public class UserConvertorImpl implements UserConvertor {

    @Override
    public UserDTO convert(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( entity.getId() );
        userDTO.setFullName( entity.getFullName() );
        userDTO.setBirthDay( entity.getBirthDay() );
        userDTO.setGender( entity.getGender() );
        userDTO.setEmail( entity.getEmail() );

        return userDTO;
    }

    @Override
    public UserEntity convert(UserDTO dtO) {
        if ( dtO == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( dtO.getId() );
        userEntity.setFullName( dtO.getFullName() );
        userEntity.setBirthDay( dtO.getBirthDay() );
        userEntity.setGender( dtO.getGender() );
        userEntity.setEmail( dtO.getEmail() );

        return userEntity;
    }
}
