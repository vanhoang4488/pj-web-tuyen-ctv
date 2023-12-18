package vanhoang.project.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vanhoang.project.entity.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Entity
@Table(name = "tags")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class TagEntity extends BaseEntity {

    /**
     * keyword của từng tag = id nhưng hiển thị ngắn gọn hơn để thuận tiện cho việc xem nhiều tag
     */
    @Column
    @NotEmpty(message = "tag.key.empty")
    @Max(value = 8, message = "tag.key.size")
    private String key;

    /**
     * thông tin chi tiết của tag
     */
    @Column
    @NotEmpty(message = "tag.detail.empty")
    @Max(value = 255, message = "tag.detail.size")
    private String detail;

    /** ràng buộc: n to n: Blog -> Blogs_Tags*/
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE,
                mappedBy = "tag")
    private Set<BlogTagEntity> blogs;

    /** ràng buộc: 1 to n: TagClass*/
    @JoinColumn(name = "classId")
    @ManyToOne(fetch = FetchType.LAZY)
    private TagClassEntity clazz;
}
