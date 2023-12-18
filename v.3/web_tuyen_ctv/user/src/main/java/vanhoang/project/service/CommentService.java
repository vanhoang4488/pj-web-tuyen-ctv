package vanhoang.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vanhoang.project.dto.CommentDTO;
import vanhoang.project.entity.BlogEntity;
import vanhoang.project.entity.CommentEntity;
import vanhoang.project.entity.UserEntity;
import vanhoang.project.repository.BlogRepository;
import vanhoang.project.repository.CommentRepository;
import vanhoang.project.service.base.AbstractService;
import vanhoang.project.service.base.BaseService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService extends AbstractService<CommentDTO, CommentEntity> implements BaseService {
    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final NotificationService notificationService;

    public String insertComment(Long blogId, Long userId, Long parentCommentId, CommentEntity commentEntity) {
        // do ta có khóa ngoại liên kết giữa bảng comments và blogs
        // ==> nếu không kiểm tra blog có tồn tại hay không thì lúc thêm comment sẽ dẫn đến lỗi.
        // ==> phải kiểm tra bài viết có tồn tại hay không
        if (!blogRepository.existsById(blogId)) {
            return "blog.id.notexists";
        }
        else if (parentCommentId != null && !commentRepository.existsById(parentCommentId)) {
            return "comment.parentComment.notexists";
        }
        else {
            BlogEntity blogEntity = new BlogEntity();
            blogEntity.setId(blogId);
            commentEntity.setBlog(blogEntity);

            UserEntity userEntity = new UserEntity();
            userEntity.setId(userId);
            commentEntity.setCommentor(userEntity);

            if (parentCommentId != null) {
                commentEntity.setCommentLevel(CommentEntity.CHILD_LEVEL);
                CommentEntity parentComment = new CommentEntity();
                parentComment.setId(parentCommentId);
                commentEntity.setParentComment(parentComment);
            }
            else {
                commentEntity.setCommentLevel(CommentEntity.PARENT_LEVEL);
            }

            commentRepository.persist(commentEntity);
            this.addNotification(commentEntity);
            return null;
        }
    }

    private void addNotification(CommentEntity commentEntity) {
        // thêm thông báo:
        // 1. thông báo đến người đăng bài viết - trừ người viết blog.
        // 2. nếu là trả lời bình luận thì thông báo thêm đến người được trả lời bình luận
        // - trừ người chính bản thân người bình luận đó.
        // 3. bình luận con trả lời bình luận con - trùng với trường hợp 2.
        // 4. đến những người trong đoạn hội thoại bình luận đó (để sau).
        notificationService.addNotification(commentEntity);
    }
}
