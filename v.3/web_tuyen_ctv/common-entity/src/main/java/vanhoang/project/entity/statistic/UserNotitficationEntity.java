package vanhoang.project.entity.statistic;

import lombok.*;
import vanhoang.project.entity.UserEntity;
import vanhoang.project.entity.base.BaseEntity;

import javax.persistence.*;


/**
 * Bảng thống kê số lượng thông báo chưa đọc.
 */
@Data
@Entity
@Table(name = "user_notifications")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class UserNotitficationEntity extends BaseEntity {

    @Column(name = "not_read_total")
    private Integer notReadTotal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;
}
