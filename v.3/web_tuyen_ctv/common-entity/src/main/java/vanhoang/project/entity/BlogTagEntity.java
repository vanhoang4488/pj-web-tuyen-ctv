package vanhoang.project.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vanhoang.project.entity.base.BaseEntityId;

import javax.persistence.*;

@Data
@Entity
@Table(name = "blogs_tags")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class  BlogTagEntity extends BaseEntityId {

    @JoinColumn(name = "blog_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private BlogEntity blog;

    @JoinColumn(name = "tag_key", columnDefinition = "varchar(8) not null")
    @ManyToOne(fetch = FetchType.LAZY)
    private TagEntity tag;
}
