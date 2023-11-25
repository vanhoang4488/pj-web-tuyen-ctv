package vanhoang.project.repository.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.base.BaseEntity;
import vanhoang.project.utils.GenerationID;
import vanhoang.project.utils.LocalDateTimeUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BaseRepositoryImpl<T extends BaseEntity, ID> implements  BaseRepository<T, ID> {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private GenerationID generationId;

    @Override
    public List<T> findAll() {
        throw new UnsupportedOperationException(
                "Fetching all records from a given database table is a terrible idea!"
        );
    }

    @Override
    public T save(T entity) {
        throw new UnsupportedOperationException(
                "There's no such thing as a save method in JPA, so don't use this hack!"
        );
    }

    @Override
    public T persist(T entity) {
        entity.setId(generationId.generationUUID());
        entity.setCreateTime(LocalDateTimeUtils.getNow());
        entity.setUpdateTime(LocalDateTimeUtils.getNow());
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T merge(T entity) {
        entity.setUpdateTime(LocalDateTimeUtils.getNow());
        entityManager.merge(entity);
        return entity;
    }
}
