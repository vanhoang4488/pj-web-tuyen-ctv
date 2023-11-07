package vanhoang.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final DiscoveryClient discoveryClient;
    private final Environment environment;

    @GetMapping("/users")
    public ResponseEntity<String> findUsers() {
        System.out.println(discoveryClient.getServices());
        System.out.println(environment.get);
        return ResponseEntity.ok("ok");
    }
}
