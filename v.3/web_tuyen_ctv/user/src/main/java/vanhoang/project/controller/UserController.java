package vanhoang.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vanhoang.project.controller.base.AbstractController;
import vanhoang.project.controller.base.BaseController;
import vanhoang.project.dto.UserDTO;
import vanhoang.project.dto.base.ResponseResult;
import vanhoang.project.entity.UserEntity;
import vanhoang.project.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController extends AbstractController implements BaseController {

    private final UserService userService;

    /**
     *  Tìm kiếm thông tin người dùng qua id
     */
    @GetMapping("/users/{id}")
    @SuppressWarnings("unused")
    public ResponseResult<UserDTO> findUserById(@PathVariable(value = "id") Long id) {
        return this.getResponseResult(userService.findUserById(id));
    }

    @PostMapping("/users")
    @SuppressWarnings("unused")
    public ResponseResult<Object> addUser(@Valid @RequestBody UserEntity userEntity) {
        return this.getResponseResult(userService.addUser(userEntity));
    }
}
