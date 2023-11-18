package vanhoang.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vanhoang.project.controller.base.AbstractController;
import vanhoang.project.controller.base.BaseController;
import vanhoang.project.dto.BlogDTO;
import vanhoang.project.dto.base.ResponseResult;
import vanhoang.project.service.BlogService;

@RestController
@RequiredArgsConstructor
public class BlogController extends AbstractController implements BaseController {

    private final BlogService blogService;

    @GetMapping("/blogs/{id}")
    public ResponseResult<BlogDTO> findBlogById(@PathVariable(value = "id") Long id) {
        return this.getResponseResult(blogService.findBlogById(id));
    }

    @GetMapping("/blogs")
    public ResponseResult<Page<BlogDTO>> getBlogPage(@RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage,
                                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return this.getResponseResult(blogService.getBlogPage(currentPage, pageSize));
    }
}
