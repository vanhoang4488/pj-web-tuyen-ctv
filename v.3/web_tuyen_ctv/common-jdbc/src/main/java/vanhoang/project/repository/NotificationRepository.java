package vanhoang.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.NotificationEntity;
import vanhoang.project.repository.base.BaseRepository;

import java.util.Set;

@Repository
public interface NotificationRepository extends
        BaseRepository<NotificationEntity, Long>, JpaRepository<NotificationEntity, Long> {

    Set<NotificationEntity> findNotificationBySourceIdAndIsRead(Long userId, Integer isRead);

    Integer countBySourceIdAndIsRead(Long userId, Integer isRead);
    Set<NotificationEntity> findNotificationByTargetIdAndIsRead(Long userId, Integer isRead);
    Integer countByTargetIdAndIsRead(Long userId, Integer isRead);
}
