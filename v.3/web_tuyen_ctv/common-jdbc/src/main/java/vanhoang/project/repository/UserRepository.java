package vanhoang.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanhoang.project.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
