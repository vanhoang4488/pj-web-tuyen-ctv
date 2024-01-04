package vanhoang.project.convertor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vanhoang.project.dto.CommentDTO;
import vanhoang.project.entity.CommentEntity;

@Mapper
public interface CommentConvertor {

    @Mapping(source = "entity.commentor.id", target = "userId")
    @Mapping(source = "entity.commentor.fullName", target = "userFullName")
    @Mapping(source = "entity.parentComment.id", target = "parentCommentId")
    CommentDTO convert(CommentEntity entity);
}
