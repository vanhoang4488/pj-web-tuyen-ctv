package vanhoang.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vanhoang.project.aop.convertor.UserConvertor;
import vanhoang.project.dto.UserDTO;
import vanhoang.project.entity.UserEntity;
import vanhoang.project.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService extends AbstractService<UserDTO, UserEntity> implements BaseService{
    private final UserRepository userRepository;

    public UserDTO findUserById(Long id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        return this.convertToDTO(optionalUserEntity, UserConvertor.class);
    }
}
