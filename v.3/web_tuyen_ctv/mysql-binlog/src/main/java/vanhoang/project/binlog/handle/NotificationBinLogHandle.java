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
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
@BinlogEntityListener(listen = NotificationEntity.class)
public class NotificationBinLogHandle implements BinLogHandle {
    private final static String TARGET_ID = "target_id";
    private final UserNotificationRepository userNotificationRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public void handle(BinlogItem binLogItem) {
        log.info("====> update user_notifications table when notifications table update: {}", binLogItem);
        List<Map<String, Serializable>> afterRows = binLogItem.getAfterRows();
        if (afterRows != null && !afterRows.isEmpty()) {
            // lấy danh sách user được cập nhật số lượng thông báo
            Set<Long> userIdSet = new HashSet<>();
            for (Map<String, Serializable> row : afterRows) {
                Serializable targetIdJsonSerializable = row.get(TARGET_ID);
                Long targetId = (Long) targetIdJsonSerializable;
                userIdSet.add(targetId);
            }
            for (Long userId : userIdSet) {
                // đếm số lượng thông báo chưa đọc của user
                int notReadTotal =
                        notificationRepository.countByTargetIdAndIsRead(userId, NotificationEntity.NOT_READ);
                UserNotitficationEntity userNotitficationEntity = new UserNotitficationEntity();
                userNotitficationEntity.setNotReadTotal(notReadTotal);
                UserEntity userEntity = new UserEntity();
                userEntity.setId(userId);
                userNotitficationEntity.setUser(userEntity);
                if (userNotificationRepository.existsByUserId(userId)) {
                    userNotificationRepository.merge(userNotitficationEntity);
                    log.info("====> merge user_notifications succeed: {}", userNotitficationEntity);
                }
                else {
                    userNotificationRepository.persist(userNotitficationEntity);
                    log.info("====> add user_notifications succeed: {}", userNotitficationEntity);
                }
            }
        }
    }
}
