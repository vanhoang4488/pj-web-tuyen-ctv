package vanhoang.project.entity.statistic;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vanhoang.project.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**Bảng thống kê số lượng blogKey: Ví dụ: blogKey lặp lại 1 lần, 0 lần là chưa xuất hiện*/
@Data
@Entity
@Table(name="blogKeys")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class BlogKeyEntity extends BaseEntity {
    @Column
    private String key;
    @Column
    private Short frequency;
}
