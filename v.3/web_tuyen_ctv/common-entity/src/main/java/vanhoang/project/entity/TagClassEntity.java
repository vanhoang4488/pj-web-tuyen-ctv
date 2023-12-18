package vanhoang.project.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vanhoang.project.entity.base.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "tag_classes")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class TagClassEntity extends BaseEntity {
    /**cho biết phân loại tag này là phân loại theo tiêu chí nào*/
    @Column
    private String detail;
    /**mức độ ưu tiên phân loại: Ví dụ: mục đích đăng bài > phân loại theo hoạt động tình nguyện*/
    @Column
    private Byte priority;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE,
                mappedBy = "clazz")
    private Set<TagEntity> tags;
}
