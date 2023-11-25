package vanhoang.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.NotificationEntity;

import java.util.Set;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    Set<NotificationEntity> findNotificationByUserIdAndNotRead(Long userId, Integer notRead);
}
