package vanhoang.project.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vanhoang.project.convertor.BlogConvertor;
import vanhoang.project.dto.BlogDTO;
import vanhoang.project.entity.BlogEntity;
import vanhoang.project.entity.BlogTagEntity;
import vanhoang.project.entity.TagEntity;
import vanhoang.project.repository.BlogRepository;
import vanhoang.project.repository.TagRepository;
import vanhoang.project.service.base.AbstractService;
import vanhoang.project.service.base.BaseService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogService extends AbstractService<BlogDTO, BlogEntity> implements BaseService {

    private final BlogRepository blogRepository;
    private final TagRepository tagRepository;

    public BlogDTO findBlogById(Long id) {
        Optional<BlogEntity> optionalBlogEntity = blogRepository.findById(id);
        return this.convertToDTO(optionalBlogEntity, BlogConvertor.class);
    }

    public Page<BlogDTO> getBlogPage(int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<BlogEntity> blogEntityPage = blogRepository.findAll(pageable);
        if (blogEntityPage.getTotalPages() > 0) {
            BlogConvertor blogConvertor = Mappers.getMapper(BlogConvertor.class);
            return blogEntityPage.map(blogConvertor::convert);
        }
        else
            return null;
    }

    public String addBlog(Long userId, BlogEntity blogEntity) {

    }

    private String createBlogKey(BlogEntity blogEntity) throws IOException {
        if (blogEntity.getTags() == null || !blogEntity.getTags().isEmpty())
            throw new IOException("====> blogEntity must at least 1 tag");
        Set<BlogTagEntity> tagIdSet = blogEntity.getTags();
        List<Long> tagIds =
                tagIdSet.stream().map(blogTagEntity -> blogTagEntity.getTag().getId()).collect(Collectors.toList());
        List<TagEntity> tagEntities = tagRepository.findTagByIdIn(tagIds);
        if (!tagIds.isEmpty()) {

        }
        else
            throw new IOException("====> don't find tag");
    }
}
