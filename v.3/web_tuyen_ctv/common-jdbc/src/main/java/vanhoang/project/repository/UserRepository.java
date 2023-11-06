package vanhoang.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
