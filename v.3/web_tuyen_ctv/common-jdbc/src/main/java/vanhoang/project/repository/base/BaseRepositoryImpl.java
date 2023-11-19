package vanhoang.project.repository.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Repository
public class BaseRepositoryImpl<T extends BaseEntity, ID> implements  BaseRepository<T, ID> {

    @Value("${serverTimeZone}")
    private String serverTimeZone;

    @PersistenceContext
    private EntityManager entityManager;

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
        entity.setCreateTime(this.getNow());
        entity.setUpdateTime(this.getNow());
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T merge(T entity) {
        entity.setUpdateTime(this.getNow());
        entityManager.merge(entity);
        return entity;
    }

    private Date getNow() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(serverTimeZone));
        return calendar.getTime();
    }
}
