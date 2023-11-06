package com.os.manage.login;

import com.os.BaseService;
import com.os.config.FeignAuthConfig;
import com.os.result.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${manage.application.name}",
            configuration = FeignAuthConfig.class,
            fallback = UserLoginFallbackImpl.class,
            path = "${manage.context-path}")
public interface UserLoginService extends BaseService {

    @RequestMapping("/sys/user/login")
    public ResultEntity<Object> loginByLoginName(@RequestParam String loginName);
}
