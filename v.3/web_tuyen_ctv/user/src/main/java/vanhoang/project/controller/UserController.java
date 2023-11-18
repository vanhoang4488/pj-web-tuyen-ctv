package vanhoang.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vanhoang.project.controller.base.AbstractController;
import vanhoang.project.controller.base.BaseController;
import vanhoang.project.dto.UserDTO;
import vanhoang.project.dto.base.ResponseResult;
import vanhoang.project.service.UserService;
import vanhoang.project.utils.BeanUtils;
import vanhoang.project.utils.StringUtils;

@RestController
@RequiredArgsConstructor
public class UserController extends AbstractController implements BaseController {

    private final UserService userService;

    /**
     *  Tìm kiếm thông tin người dùng qua id
     */
    @GetMapping("/users/{id}")
    public ResponseResult<UserDTO> findUserById(@PathVariable(value = "id") Long id) {
        return this.getResponseResult(userService.findUserById(id));
    }

    @PostMapping("/users")
    public ResponseResult<Object> insertUser(@RequestParam(value = "loginName") String loginName,
                                             @RequestParam(value = "password") String password,
                                             @RequestBody UserDTO userDTO) {
        return this.getResponseResult(userService.insertUser(loginName, password, userDTO));
    }
}
