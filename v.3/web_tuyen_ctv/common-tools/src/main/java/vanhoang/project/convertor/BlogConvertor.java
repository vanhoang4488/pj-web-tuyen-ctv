package vanhoang.project.convertor;

import org.mapstruct.Mapper;
import vanhoang.project.dto.BlogDTO;
import vanhoang.project.entity.BlogEntity;

@Mapper
public interface BlogConvertor {

    BlogDTO convert(BlogEntity entity);
    BlogEntity convert(BlogDTO dto);
}
