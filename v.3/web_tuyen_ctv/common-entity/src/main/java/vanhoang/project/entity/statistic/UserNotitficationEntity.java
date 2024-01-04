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

    /**
     * ta phải khai báo thừa 1 trường id là vì nếu ta khai báo trường khóa ngoại UserEntity
     * với @Id thì lúc này entity UserNotificationEntity sẽ có khóa chính là UserEntity và coi nó là một lớp
     * cung cấp khóa tổng hợp (tìm hiểu thêm về @IdClass).
     * mặc dù lúc tạo bảng ta vẫn thu được cột `user_id` là khóa chính nhưng
     * UserNotificationRepository sẽ báo lỗi là: does not define an IdClass.
     */
    @Id
    private Long id;

    @Column(name = "not_read_total")
    private Integer notReadTotal;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
