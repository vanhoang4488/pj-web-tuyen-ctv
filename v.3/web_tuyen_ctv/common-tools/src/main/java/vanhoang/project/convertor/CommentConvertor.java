package vanhoang.project.convertor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vanhoang.project.dto.CommentDTO;
import vanhoang.project.entity.BlogEntity;
import vanhoang.project.entity.CommentEntity;
import vanhoang.project.entity.UserEntity;

@Mapper
public interface CommentConvertor {

    @Mapping(source = "entity.commentor.id", target = "userId")
    @Mapping(source = "entity.commentor.fullName", target = "userFullName")
    @Mapping(source = "entity.parentComment.id", target = "parentCommentId")
    CommentDTO convert(CommentEntity entity);


    @Mapping(source = "dto", target = "commentor", qualifiedByName = "convertCommentor")
    @Mapping(source = "dto.blogId", target = "blog", qualifiedByName = "convertBlog")
    @Mapping(source = "dto.parentCommentId", target = "parentComment", qualifiedByName = "convertParentComment")
    CommentEntity convert(CommentDTO dto);

    @Named("convertCommentor")
    default UserEntity convertCommentor(CommentDTO dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(dto.getUserId());
        userEntity.setFullName(dto.getUserFullName());
        return userEntity;
    }

    @Named("convertBlog")
    default BlogEntity convertBlog(Long blogId) {
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setId(blogId);
        return blogEntity;
    }

    @Named("convertParentComment")
    default CommentEntity convertParentComment(Long parentCommentId) {
        CommentEntity parentComment = new CommentEntity();
        parentComment.setId(parentCommentId);
        return parentComment;
    }
}
