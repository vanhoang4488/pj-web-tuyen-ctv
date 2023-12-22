package vanhoang.project.entity;

import lombok.*;
import vanhoang.project.entity.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Entity
@Table(name = "comments")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class CommentEntity extends BaseEntity {
    public static final Integer PARENT_LEVEL = 0;
    public static final Integer CHILD_LEVEL = 1;

    @Column
    @NotEmpty(message = "comment.comment.empty")
    @Size(max = 255, message = "comment.comment.max-length")
    private String comment;
    @Column
    private Integer commentLevel; // 0 hoặc 1 -> chỉ có trả lời comment không có trả lời của trả lời của comment.
    /**self join: thể hiện mối comment này là câu trả lời của comment khác*/
    @JoinColumn(name = "parentId")
    @ManyToOne(fetch = FetchType.LAZY)
    private CommentEntity parentComment;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<CommentEntity> childenComments;
    /**Kết thúc self join*/

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity commentor;

    @JoinColumn(name = "blogId")
    @ManyToOne(fetch = FetchType.LAZY)
    private BlogEntity blog;
}
