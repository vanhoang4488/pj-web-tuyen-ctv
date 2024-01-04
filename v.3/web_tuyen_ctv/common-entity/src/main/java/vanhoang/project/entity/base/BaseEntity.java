package vanhoang.project.entity.base;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable{

    @Column
    private Date createTime;
    @Column
    private Date updateTime;
}
