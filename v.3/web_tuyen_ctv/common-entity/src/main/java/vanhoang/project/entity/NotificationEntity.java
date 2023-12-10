package vanhoang.project.entity;

import lombok.*;
import vanhoang.project.entity.base.BaseEntity;

import javax.persistence.*;
@Data
@Entity
@Table(name = "notifications")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class NotificationEntity extends BaseEntity {

    public final static Integer NOT_READ = 0;
    public final static Integer READ = 1;

    @Column
    private String title;
    @Column
    private String content;
    /**not read : 0, read: 1*/
    @Column
    private Integer isRead = NOT_READ;
    /**
     * trường cho biết thông báo gửi đến ai: không bao gồm các thông tin khác
     * trường này sẽ được thiết lập là trường đa trị.
     */
    @Column
    private Long targetId;

    @JoinColumn(name = "sourceId")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity source;
}
