package vanhoang.project.binlog.handle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vanhoang.project.annotation.BinlogEntityListener;
import vanhoang.project.binlog.bean.BinlogItem;
import vanhoang.project.binlog.handle.base.BinLogHandle;
import vanhoang.project.entity.NotificationEntity;
import vanhoang.project.entity.UserEntity;
import vanhoang.project.entity.statistic.UserNotitficationEntity;
import vanhoang.project.repository.NotificationRepository;
import vanhoang.project.repository.statistic.UserNotificationRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@BinlogEntityListener(listen = NotificationEntity.class)
public class NotificationBinLogHandle implements BinLogHandle {

    private final UserNotificationRepository userNotificationRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public void handle(BinlogItem binLogItem) {
        log.info("====> update user_notifications table when notifications table update: {}", binLogItem);
        List<Map<String, Serializable>> afterRows = binLogItem.getAfterRows();
        for (Map<String, Serializable> row : afterRows) {
            Serializable sourceIdSerializable = row.get("sourceId");
            Long sourceId = (Long) sourceIdSerializable;
            Integer count =
                    notificationRepository.countBySourceIdAndIsRead(sourceId, NotificationEntity.NOT_READ);
            UserNotitficationEntity userNotitficationEntity = new UserNotitficationEntity();
            userNotitficationEntity.setNotReadTotal(count);
            UserEntity userEntity = new UserEntity();
            userEntity.setId(sourceId);
            userNotitficationEntity.setUser(userEntity);

        }
    }
}
