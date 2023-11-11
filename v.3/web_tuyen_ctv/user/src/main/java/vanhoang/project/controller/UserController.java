package vanhoang.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import vanhoang.project.dto.UserDTO;
import vanhoang.project.dto.base.ResponseResult;

@RestController
@RequiredArgsConstructor
public class UserController {

    /**
     *  Tìm kiếm thông tin người dùng qua id
     */
    @GetMapping("/users/#{id}")
    public ResponseResult<UserDTO> findUserById(@PathVariable(value = "id") Long id) {

    }
}
