package vanhoang.controller;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vanhoang.project.dto.base.ResponseResult;
import vanhoang.project.entity.UserEntity;
import vanhoang.project.mybatis.UserRepository;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final StringRedisTemplate stringRedisTemplate;
    private final UserRepository userRepository;
    @GetMapping("/users")
    public ResponseResult<UserEntity> findUsers() {
        UserEntity userEntity = userRepository.findUser(8888);
        return ResponseResult.success(userEntity);
    }

    @PostMapping("/users")
    public ResponseEntity<String> addUsers() {
        return ResponseEntity.ok("added");
    }

    @PutMapping("/users")
    public ResponseEntity<String> editUsers() {
        String value = stringRedisTemplate.opsForValue().get("key1");
        return ResponseEntity.ok(value);
    }

    @DeleteMapping("/users")
    public ResponseEntity<String> deleteUsers() {
        return ResponseEntity.ok("deleted");
    }
}
