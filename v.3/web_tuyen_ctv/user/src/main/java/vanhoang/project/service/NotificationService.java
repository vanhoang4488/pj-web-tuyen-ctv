package vanhoang.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vanhoang.project.convertor.NotificationConvertor;
import vanhoang.project.dto.NotificationDTO;
import vanhoang.project.entity.NotificationEntity;
import vanhoang.project.entity.statistic.UserNotitficationEntity;
import vanhoang.project.redis.RedisTemplateHandle;
import vanhoang.project.repository.NotificationRepository;
import vanhoang.project.repository.statistic.UserNotificationRepository;
import vanhoang.project.service.base.AbstractService;
import vanhoang.project.service.base.BaseService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService extends AbstractService<NotificationDTO, NotificationEntity> implements BaseService {
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final RedisTemplateHandle redisTemplateHandle;

    public Set<NotificationDTO> findNotificationByUserIdAndNotRead(Long userId) {
        Set<NotificationEntity> notificationEntities =
                notificationRepository.findNotificationByTargetIdAndIsRead(userId, NotificationEntity.NOT_READ);
        return notificationEntities.stream()
                .map(entity -> this.convertToDTO(entity, NotificationConvertor.class))
                .collect(Collectors.toSet());
    }

    public Integer findUserNotificationCount(Long userId) {
        Integer notReadTotal =
                redisTemplateHandle.getCache(NotificationEntity.class, "" + userId, Integer.class);
        if (notReadTotal == null) {
            Optional<UserNotitficationEntity> optionalUserNotitficationEntity  =
                    userNotificationRepository.findUserNotificationByUserId(userId);
            notReadTotal =
                    optionalUserNotitficationEntity.map(UserNotitficationEntity::getNotReadTotal).orElse(null);
            if (notReadTotal != null) {
                redisTemplateHandle.addCache(NotificationEntity.class, "" + userId, notReadTotal);
            }
        }
        return notReadTotal;
    }

    public Boolean addNotification(NotificationEntity notificationEntity) {
        try {
            notificationRepository.persist(notificationEntity);
            return true;
        } catch (Exception e) {
            log.error("====> add notification failed:", e);
            return false;
        }
    }
}
