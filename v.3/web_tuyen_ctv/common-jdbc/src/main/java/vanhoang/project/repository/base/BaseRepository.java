package vanhoang.project.repository.base;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vanhoang.project.entity.base.BaseEntity;

import java.util.List;

@Repository
public interface BaseRepository <T extends BaseEntity, ID>{

    @Deprecated
    List<T> findAll();

    @Deprecated
    T save(T entity);

    @Deprecated
    List<T> saveAll(List<T> entities);

    @Transactional
    T persist(T entity);

    @Transactional
    List<T> persistAll(List<T> entities);

    @Transactional
    T merge(T entity);
}
