package vanhoang.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import vanhoang.project.convertor.CommentConvertor;
import vanhoang.project.dto.CommentDTO;
import vanhoang.project.entity.CommentEntity;
import vanhoang.project.repository.CommentRepository;
import vanhoang.project.service.base.AbstractService;
import vanhoang.project.service.base.BaseService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService extends AbstractService<CommentDTO, CommentEntity> implements BaseService {

    private final CommentRepository commentRepository;

    public Boolean insertComment(Long blogId, Long userId, Long parentCommentId, CommentDTO commentDTO) {
        commentDTO.setBlogId(blogId);
        commentDTO.setUserId(userId);
        commentDTO.setParentCommentId(parentCommentId);
        if (parentCommentId != null) commentDTO.setCommentLevel(1);
        else commentDTO.setCommentLevel(0);
        CommentConvertor commentConvertor = Mappers.getMapper(CommentConvertor.class);
        CommentEntity commentEntity = commentConvertor.convert(commentDTO);
        try {
            commentRepository.persist(commentEntity);
            return true;
        }
        catch (IllegalArgumentException ex1) {
            log.error("======> save comment was failed because entity was null");
        }
        catch (OptimisticLockingFailureException ex2) {
            log.error("======> save comment was failed: {}", ex2.getMessage(), ex2);
        }
        return false;
    }

}
