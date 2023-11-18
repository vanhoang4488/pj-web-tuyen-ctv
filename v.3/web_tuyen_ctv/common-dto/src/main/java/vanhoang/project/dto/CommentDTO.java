package vanhoang.project.dto;

import lombok.Getter;
import lombok.Setter;
import vanhoang.project.dto.base.BaseDTO;

@Getter
@Setter
public class CommentDTO extends BaseDTO {

    private Long userId;
    private String userFullName;
    private Long blogId;
    private Integer commentLevel;
    private Long parentCommentId;
    private String comment;
}
