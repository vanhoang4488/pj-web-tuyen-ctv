package vanhoang.project.convertor;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import vanhoang.project.dto.BlogDTO;
import vanhoang.project.entity.BlogEntity;
import vanhoang.project.entity.BlogTagEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-03T01:21:18+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.14 (Oracle Corporation)"
)
public class BlogConvertorImpl implements BlogConvertor {

    @Override
    public BlogDTO convert(BlogEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BlogDTO blogDTO = new BlogDTO();

        blogDTO.setTags( convertTags( entity.getTags() ) );
        blogDTO.setCreateTime( entity.getCreateTime() );
        blogDTO.setUpdateTime( entity.getUpdateTime() );
        blogDTO.setId( entity.getId() );
        blogDTO.setBlogKey( entity.getBlogKey() );
        blogDTO.setMajorImgUrl( entity.getMajorImgUrl() );
        blogDTO.setTitle( entity.getTitle() );
        blogDTO.setThumbnail( entity.getThumbnail() );
        blogDTO.setContent( entity.getContent() );
        blogDTO.setViews( entity.getViews() );
        blogDTO.setRate( entity.getRate() );

        return blogDTO;
    }

    @Override
    public void updateBlogEntity(BlogEntity feBlogEntity, BlogEntity jdbcBlogEntity) {
        if ( feBlogEntity == null ) {
            return;
        }

        jdbcBlogEntity.setMajorImgUrl( feBlogEntity.getMajorImgUrl() );
        if ( vanhoang.project.utils.StringUtils.isNoneEmpty(feBlogEntity.getTitle()) ) {
            jdbcBlogEntity.setTitle( feBlogEntity.getTitle() );
        }
        else {
            jdbcBlogEntity.setTitle( null );
        }
        if ( vanhoang.project.utils.StringUtils.isNoneEmpty(feBlogEntity.getThumbnail()) ) {
            jdbcBlogEntity.setThumbnail( feBlogEntity.getThumbnail() );
        }
        else {
            jdbcBlogEntity.setThumbnail( null );
        }
        if ( vanhoang.project.utils.StringUtils.isNoneEmpty(feBlogEntity.getContent()) ) {
            jdbcBlogEntity.setContent( feBlogEntity.getContent() );
        }
        else {
            jdbcBlogEntity.setContent( null );
        }
        if ( jdbcBlogEntity.getTags() != null ) {
            if ( feBlogEntity.getTags() != null && !feBlogEntity.getTags().isEmpty() ) {
                jdbcBlogEntity.getTags().clear();
                jdbcBlogEntity.getTags().addAll( feBlogEntity.getTags() );
            }
        }
        else {
            if ( feBlogEntity.getTags() != null && !feBlogEntity.getTags().isEmpty() ) {
                List<BlogTagEntity> list = feBlogEntity.getTags();
                jdbcBlogEntity.setTags( new ArrayList<BlogTagEntity>( list ) );
            }
        }
    }
}
