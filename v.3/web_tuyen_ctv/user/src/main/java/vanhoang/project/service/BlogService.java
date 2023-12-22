package vanhoang.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vanhoang.project.convertor.BlogConvertor;
import vanhoang.project.dto.BlogDTO;
import vanhoang.project.entity.*;
import vanhoang.project.entity.statistic.BlogKeyEntity;
import vanhoang.project.repository.BlogRepository;
import vanhoang.project.repository.TagClassRepository;
import vanhoang.project.repository.statistic.BlogKeyRepository;
import vanhoang.project.service.base.AbstractService;
import vanhoang.project.service.base.BaseService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogService extends AbstractService<BlogDTO, BlogEntity> implements BaseService {

    private final BlogRepository blogRepository;
    private final TagClassRepository tagClassRepository;
    private final BlogKeyRepository blogKeyRepository;

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
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        blogEntity.setAuthor(userEntity);

        try {
            // cần bổ sung thêm phần chỉnh sửa domain cho majorImgUrl để tất cả hình ảnh đều chỉ trỏ về 1 địa chỉ
            // cái này có thể cho FE làm
            blogEntity.setBlogKey(this.createBlogKey(blogEntity));
            blogRepository.persist(blogEntity);
            return null;
        } catch (IOException e) {
            log.error("====> add blog failed: {}", e.getMessage(), e);
        }
        return null; // tạm thời chưa biết nên trả thêm message gì vì phần validate ở controller làm hết rồi
    }

    /**
     * phức tạp quá, cần nghiên cứu lại thêm
     */
    private String createBlogKey(BlogEntity blogEntity) throws IOException {
        if (blogEntity.getTags() == null || blogEntity.getTags().isEmpty())
            throw new IOException("====> blogEntity must at least 1 tag");
        List<BlogTagEntity> tagIdSet = blogEntity.getTags();
        List<Long> tagIds =
                tagIdSet.stream().map(blogTagEntity -> blogTagEntity.getTag().getId()).collect(Collectors.toList());
        List<TagClassEntity> tagClassEntities = tagClassRepository.findTagClassByTagsIdIn(tagIds);
        if (!tagClassEntities.isEmpty()) {
            StringBuilder blogKey = new StringBuilder();
            List<String> chosenKeys = new ArrayList<>();
            tagClassEntities.forEach(tagClass -> {
                List<TagEntity> tagSet = tagClass.getTags();
                Optional<TagEntity> optionalTagEntity = tagSet.stream().findAny();
                chosenKeys.add(tagClass.getPriority(), optionalTagEntity.orElseThrow().getKey());
            });
            for (String key : chosenKeys) {
                blogKey.append(key);
            }
            Optional<BlogKeyEntity> optionalBlogKeyEntity = blogKeyRepository.findBlogKeyByBlogKey(blogKey.toString());
            if (optionalBlogKeyEntity.isPresent()) {
                blogKey.append(optionalBlogKeyEntity.get().getFrequency() + 1);
            }
            else {
                blogKey.append(1);
            }
            return blogKey.toString();
        }
        else
            throw new IOException("====> don't find tag");
    }
}
