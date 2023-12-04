package vanhoang.project.repository.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.statistic.UserNotitficationEntity;
import vanhoang.project.repository.base.BaseRepository;

@Repository
public interface UserNotificationRepository extends
        JpaRepository<UserNotitficationEntity, Long>, BaseRepository<UserNotitficationEntity, Long> {


}
