package vanhoang.project.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable{

    @Id
    private Long id;
    @Column
    private Date createTime;
    @Column
    private Date updateTime;
}
