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
import vanhoang.project.repository.BlogRepository;
import vanhoang.project.service.base.AbstractService;
import vanhoang.project.service.base.BaseService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService extends AbstractService<BlogDTO, BlogEntity> implements BaseService {

    private final BlogRepository blogRepository;

    public BlogDTO findBlogById(Long id) {
        Optional<BlogEntity> optionalBlogEntity = blogRepository.findById(id);
        return this.convertToDTO(optionalBlogEntity, BlogConvertor.class);
    }

    public Page<BlogDTO> getBlogPage(int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<BlogEntity> blogEntityPage = blogRepository.findBlogByIdIsNotNull(pageable);
        if (blogEntityPage.getTotalPages() > 0) {
            BlogConvertor blogConvertor = Mappers.getMapper(BlogConvertor.class);
            return blogEntityPage.map(blogConvertor::convert);
        }
        else
            return null;
    }
}
