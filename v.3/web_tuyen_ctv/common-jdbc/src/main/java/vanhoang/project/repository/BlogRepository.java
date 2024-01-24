package vanhoang.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.BlogEntity;
import vanhoang.project.repository.base.BaseRepository;

@Repository
public interface BlogRepository extends BaseRepository<BlogEntity, Long>, JpaRepository<BlogEntity, Long> {
}
