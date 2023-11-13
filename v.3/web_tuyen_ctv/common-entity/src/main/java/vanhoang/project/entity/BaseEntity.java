package vanhoang.project.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable, Persistable<Long> {

    @Transient
    private boolean isNew = true;

    @Id
    private Long id;
    @Column
    private Date createTime;
    @Column
    private Date updateTime;

    @PrePersist
    @PostLoad
    public void markNotNew() {
        this.isNew = false;
    }
}
