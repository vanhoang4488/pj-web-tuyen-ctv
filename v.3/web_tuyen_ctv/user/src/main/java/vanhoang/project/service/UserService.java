package vanhoang.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import vanhoang.project.convertor.UserConvertor;
import vanhoang.project.dto.UserDTO;
import vanhoang.project.entity.UserEntity;
import vanhoang.project.repository.UserRepository;
import vanhoang.project.service.base.AbstractService;
import vanhoang.project.service.base.BaseService;
import vanhoang.project.utils.CheckEntity;
import vanhoang.project.utils.StringUtils;

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

    public String insertUser(String loginName, String password, UserDTO userDTO) {
        UserConvertor userConvertor = Mappers.getMapper(UserConvertor.class);
        UserEntity userEntity = userConvertor.convert(userDTO);
        userEntity.setLoginName(loginName);
        userEntity.setPassword(password);
        String message = CheckEntity.checkUser(userEntity);
        if (!StringUtils.isNoneEmpty(message)) {
            try {
                userRepository.persist(userEntity);
                return null;
            }
            catch (IllegalArgumentException ex1) {
                log.error("======> save user was failed because entity was null");
            }
            catch (OptimisticLockingFailureException ex2) {
                log.error("======> save user was failed: {}", ex2.getMessage(), ex2);
            }
        }
        return message;
    }
}
