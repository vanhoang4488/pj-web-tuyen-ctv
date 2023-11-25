package vanhoang.project.entity;

import lombok.Getter;
import lombok.Setter;
import vanhoang.project.entity.base.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @Column
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
