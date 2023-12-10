package vanhoang.project.repository.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.statistic.UserNotitficationEntity;
import vanhoang.project.repository.base.BaseRepository;

import java.util.Optional;

@Repository
public interface UserNotificationRepository extends
        BaseRepository<UserNotitficationEntity, Long>, JpaRepository<UserNotitficationEntity, Long> {

    boolean existsByUserId(Long userId);
    Optional<UserNotitficationEntity> findUserNotificationByUserId(Long userId);
}
