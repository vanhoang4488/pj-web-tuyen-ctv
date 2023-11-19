package vanhoang.project.repository.base;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vanhoang.project.entity.BaseEntity;

import java.util.List;

@Repository
public interface BaseRepository <T extends BaseEntity, ID>{

    @Deprecated
    List<T> findAll();

    @Deprecated
    T save(T entity);

    @Transactional
    T persist(T entity);

    @Transactional
    T merge(T entity);
}
