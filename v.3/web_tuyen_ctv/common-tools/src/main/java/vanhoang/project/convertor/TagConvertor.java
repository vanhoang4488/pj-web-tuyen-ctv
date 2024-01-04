package vanhoang.project.convertor;

import org.mapstruct.Mapper;
import vanhoang.project.dto.TagDTO;
import vanhoang.project.entity.TagEntity;

@Mapper
public interface TagConvertor {

    TagDTO convert(TagEntity entity);
}
