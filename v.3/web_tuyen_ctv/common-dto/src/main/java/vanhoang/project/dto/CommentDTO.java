package vanhoang.project.dto;

import lombok.Getter;
import lombok.Setter;
import vanhoang.project.dto.base.BaseDTOId;

@Getter
@Setter
public class CommentDTO extends BaseDTOId {

    private Long userId;
    private String userFullName;
    private Long blogId;
    private Integer commentLevel;
    private Long parentCommentId;
    private String comment;
}
