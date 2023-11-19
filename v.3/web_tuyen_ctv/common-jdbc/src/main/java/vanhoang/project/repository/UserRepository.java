package vanhoang.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.UserEntity;
import vanhoang.project.repository.base.BaseRepository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long>, JpaRepository<UserEntity, Long> {
}
