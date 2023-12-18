package vanhoang.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.NotificationEntity;
import vanhoang.project.repository.base.BaseRepository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface NotificationRepository extends
        BaseRepository<NotificationEntity, Long>, JpaRepository<NotificationEntity, Long> {
    Set<NotificationEntity> findNotificationByTargetIdAndIsRead(Long userId, Integer isRead);
    Integer countByTargetIdAndIsRead(Long userId, Integer isRead);
    Optional<NotificationEntity> findNotificationByIdAndTargetId(Long noficationId, Long userId);
}
