package vanhoang.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import vanhoang.project.convertor.BlogConvertor;
import vanhoang.project.dto.BlogDTO;
import vanhoang.project.entity.*;
import vanhoang.project.entity.statistic.BlogKeyEntity;
import vanhoang.project.repository.BlogRepository;
import vanhoang.project.repository.TagClassRepository;
import vanhoang.project.repository.TagRepository;
import vanhoang.project.repository.statistic.BlogKeyRepository;
import vanhoang.project.service.base.AbstractService;
import vanhoang.project.service.base.BaseService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogService extends AbstractService<BlogDTO, BlogEntity> implements BaseService {

    private final BlogRepository blogRepository;
    private final TagRepository tagRepository;
    private final TagClassRepository tagClassRepository;
    private final BlogKeyRepository blogKeyRepository;

    public BlogDTO findBlogById(Long id) {
        Optional<BlogEntity> optionalBlogEntity = blogRepository.findById(id);
        return this.convertToDTO(optionalBlogEntity, BlogConvertor.class);
    }

    /**
     * Ngày: 01/01/2024
     * hiện tại ta chỉ tiến hành tìm kiếm bằng like còn tìm kiếm bằng elasticsearch sẽ xử lý sau.
     * Giai đoạn này tập trung vào tìm hiểu tìm kiếm phân rõ a, á, à, ... của tiếng việt
     */
    public Page<BlogDTO> search(String search, Integer currentPage, Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        BlogEntity matchingBlogEntity = new BlogEntity();
        matchingBlogEntity.setTitle(search);
        Page<BlogEntity> blogEntityPage =
                blogRepository.findAll(
                        Example.of(matchingBlogEntity,
                                ExampleMatcher.matching().withStringMatcher(
                                        ExampleMatcher.StringMatcher.CONTAINING)), pageable);
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

    public String updateBlog(BlogEntity blogEntity) {
        Long blogId = blogEntity.getId();
        if (blogId == null || blogId < 0) {
            return "blog.id.notexists";
        }
        Optional<BlogEntity> jdbcBlogEntityOptional = blogRepository.findById(blogId);
        if (jdbcBlogEntityOptional.isPresent()) {
            BlogEntity jdbcBlogEntity = jdbcBlogEntityOptional.get();
            // gán các trường không null hoặc rỗng của blogEntity(FE) sang jdbcBlogEntity
            // ==> để cập nhật blog.
            // không, không sai rồi! blogEntity gửi từ FE thì kiểu gì các thông tin liên quan như:
            // id, create_time, update_time, blogKey, majorImgUrl, title, thumbnail, content, views, rate
            // cũng đã có và các giá trị: title, thumbnail, content sẽ validate ở tầng controller, majorImgUrl thì không validate.
            // ==> nhiệm vụ ở BE là đảm bảo các thông tin như:
            // id, create_time, update_time, blogKey, views, rate không bị update
            BlogConvertor blogConvertor = Mappers.getMapper(BlogConvertor.class);
            blogConvertor.updateBlogEntity(blogEntity, jdbcBlogEntity);
            blogRepository.merge(jdbcBlogEntity);
            return null;
        }
        else {
            return "blog.id.notexists";
        }
    }

    /**
     * Tạo blogKey:
     * phức tạp quá, cần nghiên cứu lại thêm
     */
    private String createBlogKey(BlogEntity blogEntity) throws IOException {
        if (blogEntity.getTags() == null || blogEntity.getTags().isEmpty())
            throw new IOException("====> blogEntity must at least 1 tag");
        List<BlogTagEntity> tagIdSet = blogEntity.getTags();
        List<String> tagKeys =
                tagIdSet.stream().map(blogTagEntity -> blogTagEntity.getTag().getTagKey()).collect(Collectors.toList());
        List<TagEntity> tagEntities = tagRepository.findAllById(tagKeys);
        if (!tagEntities.isEmpty()) {
            StringBuilder blogKey = new StringBuilder();

            // lấy ra danh sách id của tagclassentity nhằm tối giảm số câu query
            // còn nếu không sử dụng luôn trườn priority của tagClass làm key thì sẽ dẫn đến với
            // mỗi tag ta sẽ thực hiện truy vấn tìm kiếm tagClassEntity 1 lần dẫn đến lặp lại nhiều query.
            Map<Long, String> chosenKeyMap = tagEntities.stream()
                    .collect(Collectors.toMap(tagEntity -> tagEntity.getClazz().getId(), TagEntity::getTagKey));

            List<TagClassEntity> tagClassEntities = tagClassRepository.findAllById(chosenKeyMap.keySet());

            for (TagClassEntity tagClassEntity : tagClassEntities) {
                if (chosenKeyMap.containsKey(tagClassEntity.getId())) {
                    blogKey.append(chosenKeyMap.get(tagClassEntity.getId()));
                }
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
