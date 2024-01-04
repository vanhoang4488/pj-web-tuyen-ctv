package vanhoang.project.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vanhoang.project.entity.base.BaseEntityId;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tag_classes")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class TagClassEntity extends BaseEntityId {
    /**cho biết phân loại tag này là phân loại theo tiêu chí nào*/
    @Column
    private String detail;
    /**
     * mức độ ưu tiên phân loại: Ví dụ: mục đích đăng bài > phân loại theo hoạt động tình nguyện
     * giá trị sẽ lần 0 -> n
     * */
    @Column
    private Byte priority;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE,
                mappedBy = "clazz")
    private List<TagEntity> tags;
}
