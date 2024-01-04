package vanhoang.project.entity;

import lombok.*;
import vanhoang.project.entity.base.BaseEntityId;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends BaseEntityId {
    public static final Integer MALE_GENDER = 0;
    public static final Integer FEMALE_GENDER = 1;
    @Column
    @NotEmpty(message = "user.loginName.empty")
    @Size(min = 8, message = "user.loginName.size")
    @Size(max = 16, message = "user.loginName.size")
    private String loginName;
    @Column
    @NotEmpty(message = "user.password.empty")
    @Size(min = 8, message = "user.password.size")
    @Size(max = 16, message = "user.password.size")
    private String password;
    @Column
    @NotEmpty(message = "user.fullName.empty")
    private String fullName;
    @Column
    @NotNull(message = "user.birthday.null")
    private Date birthDay;
    @Column
    @Min(value = 0, message = "user.gender.size")
    @Max(value = 1, message = "user.gender.size")
    private Integer gender;
    @Column
    @NotEmpty(message = "user.email.empty")
    @Email(message = "user.email.pattern")
    private String email;
    /**ràng buộc: 1 to n: Blog*/
    @OneToMany(fetch = FetchType.LAZY, cascade =  CascadeType.MERGE,
                mappedBy = "author")
    private List<BlogEntity> blogs;

    /**ràng buộc: n to n: Blog -> Comment*/
    @OneToMany(fetch = FetchType.LAZY,
                mappedBy = "commentor")
    private List<CommentEntity> comments;

    @OneToMany(fetch = FetchType.LAZY,
                mappedBy = "source")
    private List<NotificationEntity> notifications;

    /*
      Ở đây ta sẽ tiến hành loại bỏ liên kết 2 chiều giữa user và user_notifications ở thực thể cha
      Vì:
      1. Khi ta lần đầu thêm 1 đối tượng user ta cũng không cần bổ sung 1 đối tượng user_notifications
      2. khi ta update user ta cũng không cần update user_notifications
      3. Ta sẽ không bao giờ xóa user --> không bao giờ xóa user_notifications;
      4. khi ta truy vấn user ta cũng không cần user_notifications. vì những nơi cần thông tin người dùng
      thì không cần số lượng thông báo chưa đọc.
        @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
        private UserNotitficationEntity userNotitfication;
    */
}
