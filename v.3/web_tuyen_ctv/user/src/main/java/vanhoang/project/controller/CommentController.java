package vanhoang.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vanhoang.project.controller.base.AbstractController;
import vanhoang.project.controller.base.BaseController;
import vanhoang.project.dto.base.ResponseResult;
import vanhoang.project.entity.CommentEntity;
import vanhoang.project.redis.RedisTemplateHandle;
import vanhoang.project.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class CommentController extends AbstractController implements BaseController {
    private final CommentService commentService;
    private final RedisTemplateHandle redisTemplateHandle;

    @PostMapping("/blogs/{blogId}/comments")
    public ResponseResult<Object> insertComment(HttpServletRequest request,
                                                @PathVariable(value = "blogId") Long blogId,
                                                @Valid @RequestBody CommentEntity commentEntity) {
        Long userId = redisTemplateHandle.getUserId(request);
        return this.getResponseResult(commentService.insertComment(blogId, userId, null, commentEntity));
    }

    @PostMapping("/blogs/{blogId}/comments/{parentCommentId}")
    public ResponseResult<Object> insertReply(HttpServletRequest request,
                                              @PathVariable(value = "blogId") Long blogId,
                                              @PathVariable(value = "parentCommentId") Long parentCommentId,
                                              @Valid @RequestBody CommentEntity commentEntity) {
        Long userId = redisTemplateHandle.getUserId(request);
        return this.getResponseResult(commentService.insertComment(blogId, userId, parentCommentId, commentEntity));
    }
}
