package vanhoang.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vanhoang.project.controller.base.AbstractController;
import vanhoang.project.controller.base.BaseController;
import vanhoang.project.dto.BlogDTO;
import vanhoang.project.dto.base.ResponseResult;
import vanhoang.project.entity.BlogEntity;
import vanhoang.project.redis.RedisTemplateHandle;
import vanhoang.project.service.BlogService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class BlogController extends AbstractController implements BaseController {

    private final BlogService blogService;
    private final RedisTemplateHandle redisTemplateHandle;

    /**
     * Lấy nội dung bài viết
     */
    @GetMapping("/blogs/{id}")
    public ResponseResult<BlogDTO> findBlogById(@PathVariable(value = "id") Long id) {
        return this.getResponseResult(blogService.findBlogById(id));
    }

    /**
     * Lấy danh sách bài viết và phân trang,
     * tìm kiếm danh sách bài viết dựa trên 'từ tìm kiếm' được nhập trong ô tìm kiếm
     * từ tìm kiếm này sẽ được tiến hành so sánh với từng `title` của mỗi thực thể blog.
     * Ngày 01/01/2024
     */
    @GetMapping("/blogs")
    public ResponseResult<Page<BlogDTO>> search(@RequestParam(value = "search", required = false) String search,
                                                @RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return this.getResponseResult(blogService.search(search, currentPage, pageSize));
    }

    /**
     * Thêm bài viết
     */
    @PostMapping("/blogs")
    public ResponseResult<Object> addBlog(HttpServletRequest request,
                                          @Valid @RequestBody BlogEntity blogEntity) {
        Long userId = redisTemplateHandle.getUserId(request);
        return this.getResponseResult(blogService.addBlog(userId, blogEntity));
    }

    /**
     * Cập nhật bài viết
     */
    @PutMapping("/blogs")
    public ResponseResult<Object> updateBlog(HttpServletRequest request,
                                             @Valid @RequestBody BlogEntity blogEntity) {
        return this.getResponseResult(blogService.updateBlog(blogEntity));
    }
}
