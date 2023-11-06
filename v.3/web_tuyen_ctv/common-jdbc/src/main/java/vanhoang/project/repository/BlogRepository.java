package vanhoang.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.BlogEntity;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {
}
