package vanhoang.project.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import vanhoang.project.controller.base.AbstractController;
import vanhoang.project.controller.base.BaseController;
import vanhoang.project.dto.TagDTO;
import vanhoang.project.dto.base.ResponseResult;
import vanhoang.project.service.TagService;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequiredArgsConstructor
public class TagController extends AbstractController implements BaseController {
    private final TagService tagService;

    @GetMapping("/tags")
    public ResponseResult<List<TagDTO>> findAll() {
        return this.getResponseResult(tagService.findAll());
    }

    @GetMapping("/tags/{tagKey}")
    public ResponseResult<TagDTO> findTagById(@PathVariable(value = "id") String id) {
        return this.getResponseResult(tagService.findTagById(id));
    }
}
