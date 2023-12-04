package vanhoang.project.entity;

import lombok.Getter;
import lombok.Setter;
import vanhoang.project.entity.base.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class NotificationEntity extends BaseEntity {

    public final static Integer NOT_READ = 0;
    public final static Integer READ = 1;

    @Column
    private String title;
    @Column
    private String content;
    /**not read : 0, read: 1*/
    @Column
    private Integer isRead;
    /**trường cho biết thông báo gửi đến ai hoặc những ai: không bao gồm các thông tin khác
     * ==> giá trị là mảng chứa Long chứa danh sách userId chuyển sang json.
     */
    @Column
    private String args;

    @JoinColumn(name = "sourceId")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity source;
}
