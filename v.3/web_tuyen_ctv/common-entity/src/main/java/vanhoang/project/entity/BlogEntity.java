package vanhoang.project.entity;

import lombok.*;
import vanhoang.project.entity.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
@Data
@Entity
@Table(name = "blogs")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class BlogEntity extends BaseEntity {

    @Column
    @NotNull(message = "blog.blogKey.empty")
    private String blogKey;
    @Column
    private String majorImgUrl; // đường dẫn của hình ảnh chính của bài viết.
    @Column
    @NotEmpty(message = "blog.title.empty")
    @Max(value = 255, message = "blog.title.max-length")
    private String title;
    @Column
    @NotEmpty(message = "blog.thumbnail.empty")
    @Max(value = 255, message = "blog.thumbnail.max-length")
    private String thumbnail;
    @Column
    @NotEmpty(message = "blog.content.empty")
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

    /** ràng buộc: n to n: Tag -> Blogs_Tags*/
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE,
                mappedBy = "blog")
    private Set<BlogTagEntity> tags;
}
