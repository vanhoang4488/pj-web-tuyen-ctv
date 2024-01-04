package vanhoang.project.entity.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Lớp cung cấp id chung cho các entity con
 */
@Data
@MappedSuperclass
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class BaseEntityId extends BaseEntity{

    @Id
    private Long id;
}
