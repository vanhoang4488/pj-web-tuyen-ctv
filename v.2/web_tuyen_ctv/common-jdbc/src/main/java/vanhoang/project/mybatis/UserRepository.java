package vanhoang.project.mybatis;

import org.apache.ibatis.annotations.Mapper;
import vanhoang.project.entity.UserEntity;

@Mapper
public interface UserRepository {

    public UserEntity findUser(long id);
}
