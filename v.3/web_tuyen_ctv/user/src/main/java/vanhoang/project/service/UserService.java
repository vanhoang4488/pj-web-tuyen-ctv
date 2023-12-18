package vanhoang.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vanhoang.project.convertor.UserConvertor;
import vanhoang.project.dto.UserDTO;
import vanhoang.project.entity.UserEntity;
import vanhoang.project.repository.UserRepository;
import vanhoang.project.service.base.AbstractService;
import vanhoang.project.service.base.BaseService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends AbstractService<UserDTO, UserEntity> implements BaseService {
    private final UserRepository userRepository;

    public UserDTO findUserById(Long id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        return this.convertToDTO(optionalUserEntity, UserConvertor.class);
    }

    public String addUser(UserEntity userEntity) {
        if (userRepository.existsByLoginName(userEntity.getLoginName())) {
            return "user.loginName.exists";
        }
        else if (userRepository.existsByEmail(userEntity.getEmail())) {
            return "user.email.exists";
        }
        else {
            userRepository.persist(userEntity);
            return null;
        }
    }
}
