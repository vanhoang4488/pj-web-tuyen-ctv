package com.os.manage.syslog;

import com.os.BaseService;
import com.os.config.FeignAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${manage.application.name}",
            configuration = FeignAuthConfig.class,
            fallback = SysLogFallbackImpl.class,
            path = "${manage.context-path}")
public interface SysLogService extends BaseService {

    /** lấy bảng thông tin blog của người dùng*/
    feign.Response downloadUserBlogExcel();
}
