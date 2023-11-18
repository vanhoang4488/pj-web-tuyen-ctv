package vanhoang.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity{

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
}
