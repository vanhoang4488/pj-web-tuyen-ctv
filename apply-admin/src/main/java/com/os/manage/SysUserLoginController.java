package com.os.manage;

import com.os.Indempotency.Idempotency;
import com.os.manage.login.SysUserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.os.result.ResultEntity;

@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
public class SysUserLoginController {

    private final SysUserLoginService sysUserLoginService;

    @RequestMapping("/login")
    public ResultEntity<Object> loginByLoginName(@RequestParam String loginName){

        return sysUserLoginService.loginByLoginName(loginName);
    }
}
