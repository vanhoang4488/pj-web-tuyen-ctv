package vanhoang.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/users")
    public ResponseEntity<String> findUsers() {
        return ResponseEntity.ok("ok");
    }
}
