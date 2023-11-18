package vanhoang.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vanhoang.project.controller.base.AbstractController;
import vanhoang.project.controller.base.BaseController;
import vanhoang.project.dto.CommentDTO;
import vanhoang.project.dto.base.ResponseResult;
import vanhoang.project.service.CommentService;

@RestController
@RequiredArgsConstructor
public class CommentController extends AbstractController implements BaseController {

    private final CommentService commentService;

    @PostMapping("/blogs/{blogId}/users/{userId}/comments")
    public ResponseResult<Object> insertComment(@PathVariable(value = "blogId") Long blogId,
                                                @PathVariable(value = "userId") Long userId,
                                                @RequestBody CommentDTO commentDTO) {
        return this.getResponseResult(commentService.insertComment(blogId, userId, null, commentDTO));
    }

    @PostMapping("/blogs/{blogId}/users/{userId}/comments/{parentCommentId}")
    public ResponseResult<Object> insertReply(@PathVariable(value = "blogId") Long blogId,
                                                @PathVariable(value = "userId") Long userId,
                                                @PathVariable(value = "parentCommentId") Long parentCommentId,
                                                @RequestBody CommentDTO commentDTO) {
        return this.getResponseResult(commentService.insertComment(blogId, userId, parentCommentId, commentDTO));
    }
}
