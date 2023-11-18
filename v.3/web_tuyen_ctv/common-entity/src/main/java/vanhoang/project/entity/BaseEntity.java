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
    public void prePresist() {
        this.isNew = false;
        this.id = this.createId(); // cải tiến lại cơ chế tạo khóa chính
        this.createTime = new Date(); // cần điều chỉnh theo timezone
        this.updateTime = new Date(); // cần điều chỉnh theo timezone
    }

    @PreUpdate
    public void preUpdate() {
        this.updateTime = new Date();
    }

    @PostLoad
    private void postLoad() {
        this.isNew = false;
    }

    private Long createId() {
        return 8890L;
    }
}
