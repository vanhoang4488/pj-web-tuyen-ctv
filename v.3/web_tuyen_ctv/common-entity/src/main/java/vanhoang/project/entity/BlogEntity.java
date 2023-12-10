package vanhoang.project.entity;

import lombok.*;
import vanhoang.project.entity.base.BaseEntity;

import javax.persistence.*;
import java.util.Set;
@Data
@Entity
@Table(name = "blogs")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class BlogEntity extends BaseEntity {

    @Column
    private String blogKey;
    @Column
    private String majorImgUrl; // đường dẫn của hình ảnh chính của bài viết.
    @Column
    private String title;
    @Column
    private String thumbnail;
    @Column
    private String content;
    @Column
    private Integer views; // số lượt xem
    @Column
    private Double rate; // đánh giá mức độ hay của bài viết

    /**ràng buộc: nhiều một với User*/
    @JoinColumn(name = "authorId")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity author;

    /**ràng buộc: n to n: User -> Comment*/
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE,
                mappedBy = "blog")
    private Set<CommentEntity> comments;
}
