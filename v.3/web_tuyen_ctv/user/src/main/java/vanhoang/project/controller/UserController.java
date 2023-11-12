package vanhoang.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import vanhoang.project.dto.UserDTO;
import vanhoang.project.dto.base.ResponseResult;
import vanhoang.project.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     *  Tìm kiếm thông tin người dùng qua id
     */
    @GetMapping("/users/{id}")
    public ResponseResult<UserDTO> findUserById(@PathVariable(value = "id") Long id) {
        UserDTO userDTO = userService.findUserById(id);
        if (userDTO != null) return ResponseResult.success(userDTO);
        else return ResponseResult.fail();
    }
}
