package vanhoang.project.repository.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.statistic.BlogKeyEntity;
import vanhoang.project.repository.base.BaseRepository;

import java.util.Optional;

@Repository
public interface BlogKeyRepository extends BaseRepository<BlogKeyEntity, Long>, JpaRepository<BlogKeyEntity, Long> {

    Optional<BlogKeyEntity> findBlogKeyByBlogKey(String key);
}
