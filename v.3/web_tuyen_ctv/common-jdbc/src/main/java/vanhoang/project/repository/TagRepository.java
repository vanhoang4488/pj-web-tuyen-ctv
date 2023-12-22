package vanhoang.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.TagEntity;
import vanhoang.project.repository.base.BaseRepository;

import java.util.List;

@Repository
public interface TagRepository extends BaseRepository<TagEntity, Long>, JpaRepository<TagEntity, Long> {
}
