package vanhoang.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vanhoang.project.convertor.NotificationConvertor;
import vanhoang.project.dto.NotificationDTO;
import vanhoang.project.entity.NotificationEntity;
import vanhoang.project.entity.statistic.UserNotitficationEntity;
import vanhoang.project.repository.NotificationRepository;
import vanhoang.project.repository.statistic.UserNotificationRepository;
import vanhoang.project.service.base.AbstractService;
import vanhoang.project.service.base.BaseService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService extends AbstractService<NotificationDTO, NotificationEntity> implements BaseService {

    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;

    public Set<NotificationDTO> findNotificationByUserIdAndNotRead(Long userId) {
        Set<NotificationEntity> notificationEntities =
                notificationRepository.findNotificationByUserIdAndNotRead(userId, NotificationEntity.NOT_READ);
        return notificationEntities.stream()
                .map(entity -> this.convertToDTO(entity, NotificationConvertor.class))
                .collect(Collectors.toSet());
    }

    public Integer findUserNotificationCount(Long userId) {
        Optional<UserNotitficationEntity> optionalUserNotitficationEntity  =
                userNotificationRepository.findById(userId);
        return optionalUserNotitficationEntity.map(UserNotitficationEntity::getNotReadTotal).orElse(null);
    }
}
