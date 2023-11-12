package vanhoang.project.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import vanhoang.project.convertor.UserConvertor;
import vanhoang.project.dto.UserDTO;
import vanhoang.project.entity.UserEntity;
import vanhoang.project.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService extends AbstractService<UserConvertor> implements BaseService{
    private final UserRepository userRepository;

    public UserDTO findUserById(Long id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (optionalUserEntity.isPresent()) {
            UserConvertor userConvertor = Mappers.getMapper(UserConvertor.class);
            return userConvertor.convert(optionalUserEntity.get());
        }
        return new UserDTO();
    }
}
