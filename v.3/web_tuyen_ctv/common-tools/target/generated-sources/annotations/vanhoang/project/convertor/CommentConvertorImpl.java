package vanhoang.project.convertor;

import javax.annotation.processing.Generated;
import vanhoang.project.dto.CommentDTO;
import vanhoang.project.entity.CommentEntity;
import vanhoang.project.entity.UserEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-03T01:21:18+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.14 (Oracle Corporation)"
)
public class CommentConvertorImpl implements CommentConvertor {

    @Override
    public CommentDTO convert(CommentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setUserId( entityCommentorId( entity ) );
        commentDTO.setUserFullName( entityCommentorFullName( entity ) );
        commentDTO.setParentCommentId( entityParentCommentId( entity ) );
        commentDTO.setCreateTime( entity.getCreateTime() );
        commentDTO.setUpdateTime( entity.getUpdateTime() );
        commentDTO.setId( entity.getId() );
        commentDTO.setCommentLevel( entity.getCommentLevel() );
        commentDTO.setComment( entity.getComment() );

        return commentDTO;
    }

    private Long entityCommentorId(CommentEntity commentEntity) {
        if ( commentEntity == null ) {
            return null;
        }
        UserEntity commentor = commentEntity.getCommentor();
        if ( commentor == null ) {
            return null;
        }
        Long id = commentor.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityCommentorFullName(CommentEntity commentEntity) {
        if ( commentEntity == null ) {
            return null;
        }
        UserEntity commentor = commentEntity.getCommentor();
        if ( commentor == null ) {
            return null;
        }
        String fullName = commentor.getFullName();
        if ( fullName == null ) {
            return null;
        }
        return fullName;
    }

    private Long entityParentCommentId(CommentEntity commentEntity) {
        if ( commentEntity == null ) {
            return null;
        }
        CommentEntity parentComment = commentEntity.getParentComment();
        if ( parentComment == null ) {
            return null;
        }
        Long id = parentComment.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
