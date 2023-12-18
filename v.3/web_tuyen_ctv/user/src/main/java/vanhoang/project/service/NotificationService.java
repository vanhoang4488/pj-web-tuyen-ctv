package vanhoang.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vanhoang.project.convertor.NotificationConvertor;
import vanhoang.project.dto.NotificationDTO;
import vanhoang.project.entity.BlogEntity;
import vanhoang.project.entity.CommentEntity;
import vanhoang.project.entity.NotificationEntity;
import vanhoang.project.entity.UserEntity;
import vanhoang.project.entity.statistic.UserNotitficationEntity;
import vanhoang.project.redis.RedisTemplateHandle;
import vanhoang.project.repository.BlogRepository;
import vanhoang.project.repository.CommentRepository;
import vanhoang.project.repository.NotificationRepository;
import vanhoang.project.repository.statistic.UserNotificationRepository;
import vanhoang.project.service.base.AbstractService;
import vanhoang.project.service.base.BaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService extends AbstractService<NotificationDTO, NotificationEntity> implements BaseService {
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;
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

    public NotificationDTO findNotificationByNotificationIdAndUserId(Long notificationId, Long userId) {
        Optional<NotificationEntity> optionalNotificationEntity =
                notificationRepository.findNotificationByIdAndTargetId(notificationId, userId);
        // cập nhật trạng thái đã đọc của notification
        if (optionalNotificationEntity.isPresent()) {
            NotificationEntity notificationEntity = optionalNotificationEntity.get();
            notificationEntity.setIsRead(NotificationEntity.READ);
            notificationRepository.merge(notificationEntity);
        }
        return this.convertToDTO(optionalNotificationEntity, NotificationConvertor.class);
    }

    public void addNotification(CommentEntity commentEntity) {
        Long commentorId = commentEntity.getCommentor().getId();
        UserEntity source = new UserEntity();
        source.setId(commentorId);
        List<NotificationEntity> addList = new ArrayList<>();

        //1. thông báo đến người viết bài viết - trừ chính tác giả bình luận
        Long blogId = commentEntity.getBlog().getId();
        Optional<BlogEntity> optionalBlogEntity = blogRepository.findById(blogId);
        if (optionalBlogEntity.isPresent() &&
                !commentorId.equals(optionalBlogEntity.get().getAuthor().getId())) {
            BlogEntity blogEntity = optionalBlogEntity.get();
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setSource(source);
            notificationEntity.setTargetId(blogEntity.getAuthor().getId());
            notificationEntity.setTitle(blogEntity.getTitle());
            notificationEntity.setContent(commentEntity.getComment());
            addList.add(notificationEntity);
        }
        //2. thêm thông báo đến người bình luận
        CommentEntity parentComment = commentEntity.getParentComment();
        if (parentComment != null && !commentorId.equals(parentComment.getId())) {
            Optional<CommentEntity> optionalCommentEntity = commentRepository.findById(parentComment.getId());
            if (optionalCommentEntity.isPresent()) {
                parentComment = optionalCommentEntity.get();
                Long targetId = parentComment.getCommentor().getId();
                NotificationEntity notificationEntity = new NotificationEntity();
                notificationEntity.setSource(source);
                notificationEntity.setTargetId(targetId);
                notificationEntity.setTitle(parentComment.getComment());
                notificationEntity.setContent(commentEntity.getComment());
                addList.add(notificationEntity);
            }
        }
        notificationRepository.persistAll(addList);
    }
}
