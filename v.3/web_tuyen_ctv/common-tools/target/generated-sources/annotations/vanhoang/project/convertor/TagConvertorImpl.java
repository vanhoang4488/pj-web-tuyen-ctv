package vanhoang.project.convertor;

import javax.annotation.processing.Generated;
import vanhoang.project.dto.TagDTO;
import vanhoang.project.entity.TagEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-03T01:21:17+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.14 (Oracle Corporation)"
)
public class TagConvertorImpl implements TagConvertor {

    @Override
    public TagDTO convert(TagEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TagDTO tagDTO = new TagDTO();

        tagDTO.setCreateTime( entity.getCreateTime() );
        tagDTO.setUpdateTime( entity.getUpdateTime() );
        tagDTO.setTagKey( entity.getTagKey() );
        tagDTO.setDetail( entity.getDetail() );

        return tagDTO;
    }
}
