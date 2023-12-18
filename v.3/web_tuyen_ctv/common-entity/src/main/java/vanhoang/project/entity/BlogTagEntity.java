package vanhoang.project.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vanhoang.project.entity.base.BaseEntity;

import javax.persistence.*;

@Data
@Entity
@Table(name = "blogs_tags")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BlogTagEntity extends BaseEntity {

    @JoinColumn(name = "blogId")
    @ManyToOne(fetch = FetchType.LAZY)
    private BlogEntity blog;

    @JoinColumn(name = "tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    private TagEntity tag;
}
