package vanhoang.project.convertor;

import org.mapstruct.*;
import vanhoang.project.dto.BlogDTO;
import vanhoang.project.entity.BlogEntity;
import vanhoang.project.entity.BlogTagEntity;

import java.util.*;

@Mapper
public interface BlogConvertor {

    @Mapping(source = "tags", target = "tags", qualifiedByName = "convertTags")
    BlogDTO convert(BlogEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "majorImgUrl", target = "majorImgUrl")
    @Mapping(source = "title", target = "title", conditionExpression = "java(vanhoang.project.utils.StringUtils.isNoneEmpty(feBlogEntity.getTitle()))")
    @Mapping(source = "thumbnail", target = "thumbnail", conditionExpression = "java(vanhoang.project.utils.StringUtils.isNoneEmpty(feBlogEntity.getThumbnail()))")
    @Mapping(source = "content", target = "content", conditionExpression = "java(vanhoang.project.utils.StringUtils.isNoneEmpty(feBlogEntity.getContent()))")
    @Mapping(source = "tags", target = "tags", conditionExpression = "java(feBlogEntity.getTags() != null && !feBlogEntity.getTags().isEmpty())")
    void updateBlogEntity(BlogEntity feBlogEntity, @MappingTarget BlogEntity jdbcBlogEntity);

    @Named("convertTags")
    default List<String> convertTags(List<BlogTagEntity> blogTags) {
        if (blogTags != null && !blogTags.isEmpty()) {
            List<String> tags = new ArrayList<>();
            for (BlogTagEntity blogTagEntity : blogTags) {
                if (blogTagEntity.getTag() != null) {
                    tags.add(blogTagEntity.getTag().getTagKey());
                }
            }
            return tags;
        }
        else
            return Collections.emptyList();
    }

}
