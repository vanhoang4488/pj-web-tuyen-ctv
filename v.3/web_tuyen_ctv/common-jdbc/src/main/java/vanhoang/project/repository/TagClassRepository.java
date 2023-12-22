package vanhoang.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vanhoang.project.entity.TagClassEntity;
import vanhoang.project.repository.base.BaseRepository;

import java.util.List;

@Repository
public interface TagClassRepository extends BaseRepository<TagClassEntity, Long>, JpaRepository<TagClassEntity, Long> {

    List<TagClassEntity> findTagClassByTagsIdIn(List<Long> tagIds);
}
