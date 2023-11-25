package vanhoang.project.entity.statistic;

import lombok.Getter;
import lombok.Setter;
import vanhoang.project.entity.UserEntity;
import vanhoang.project.entity.base.BaseEntity;

import javax.persistence.*;


/**
 * Bảng thống kê số lượng thông báo chưa đọc.
 */
@Getter
@Setter
@Entity
@Table(name = "user_notifications")
public class UserNotitficationEntity extends BaseEntity {

    @Column(name = "not_read_total")
    private Integer notReadTotal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;
}
