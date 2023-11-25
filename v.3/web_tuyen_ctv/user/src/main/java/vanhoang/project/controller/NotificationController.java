package vanhoang.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import vanhoang.project.controller.base.AbstractController;
import vanhoang.project.controller.base.BaseController;
import vanhoang.project.dto.NotificationDTO;
import vanhoang.project.dto.base.ResponseResult;
import vanhoang.project.service.NotificationService;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class NotificationController extends AbstractController implements BaseController {

    private final NotificationService notificationService;

    @GetMapping("/notifications/users/{userId}")
    public ResponseResult<Set<NotificationDTO>>
                findNotificationByUserIdAndNotRead(@PathVariable(name = "userId") Long userId) {
        return this.getResponseResult(notificationService.findNotificationByUserIdAndNotRead(userId));
    }

    @GetMapping("/notifications/total/users/{userId}")
    public ResponseResult<Integer> findUserNotificationCount(@PathVariable(name = "userId") Long userId) {
        return this.getResponseResult(notificationService.findUserNotificationCount(userId));
    }
}
