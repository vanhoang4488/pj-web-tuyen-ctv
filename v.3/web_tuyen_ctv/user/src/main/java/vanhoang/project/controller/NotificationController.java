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
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class NotificationController extends AbstractController implements BaseController {

    private final NotificationService notificationService;

    /**
     * Lấy danh sách thông báo của user
     */
    @GetMapping("/notifications/users/{userId}")
    public ResponseResult<Set<NotificationDTO>>
                findNotificationByUserIdAndNotRead(@PathVariable(name = "userId") Long userId) {
        return this.getResponseResult(notificationService.findNotificationByUserIdAndNotRead(userId));
    }

    /**
     * Lấy số lượng thông báo của user.
     */
    @GetMapping("/notifications/users/{userId}/count")
    public ResponseResult<Integer> findUserNotificationCount(@PathVariable(name = "userId") Long userId) {
        return this.getResponseResult(notificationService.findUserNotificationCount(userId));
    }

    /**
     * Lấy thông báo cụ thể của một user
     */
    @GetMapping("/notifications/{notificationId}/users/{userId}")
    public ResponseResult<NotificationDTO>
                findNotificationByNotificationIdAndUserId (@PathVariable(name = "notificationId") Long notificationId,
                                                           @PathVariable(name= "userId") Long userId) {
        return this.getResponseResult(
                notificationService.findNotificationByNotificationIdAndUserId(notificationId, userId));
    }
}
