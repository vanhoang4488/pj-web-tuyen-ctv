package vanhoang.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.CommentEntity;
import vanhoang.project.repository.base.BaseRepository;

@Repository
public interface CommentRepository extends BaseRepository<CommentEntity, Long>, JpaRepository<CommentEntity, Long> {
}
