package vanhoang.project.repository.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.statistic.UserNotitficationEntity;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotitficationEntity, Long> {
}
