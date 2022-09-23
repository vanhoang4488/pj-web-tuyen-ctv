package com.os.manage.userblognum;

import com.os.BaseService;
import com.os.config.FeignAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "${manage.application.name}",
            configuration = FeignAuthConfig.class,
            fallback = UserBlogNumFallbackImpl.class,
            path = "${manage.context-path}")
public interface UserBlogNumService extends BaseService {

    /** lấy bảng thông tin blog của người dùng*/
    @RequestMapping("/sys/log/downloadUserBlogExcel")
    feign.Response downloadUserBlogExcel();
}
