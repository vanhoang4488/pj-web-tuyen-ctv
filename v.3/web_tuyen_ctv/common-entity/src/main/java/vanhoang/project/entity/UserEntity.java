package vanhoang.project.entity;

import lombok.*;
import vanhoang.project.entity.base.BaseEntity;
import vanhoang.project.entity.statistic.UserNotitficationEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends BaseEntity {

    @Column
    private String loginName;
    @Column
    private String password;
    @Column
    private String fullName;
    @Column
    private Date birthDay;
    @Column
    private Integer gender;
    @Column
    private String email;

    /**ràng buộc: 1 to n: Blog*/
    @OneToMany(fetch = FetchType.LAZY, cascade =  CascadeType.MERGE,
                mappedBy = "author")
    private Set<BlogEntity> blogs;

    /**ràng buộc: n to n: Blog -> Comment*/
    @OneToMany(fetch = FetchType.LAZY,
                mappedBy = "commentor")
    private Set<CommentEntity> comments;

    @OneToMany(fetch = FetchType.LAZY,
                mappedBy = "source")
    private Set<NotificationEntity> notifications;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserNotitficationEntity userNotitfication;
}
