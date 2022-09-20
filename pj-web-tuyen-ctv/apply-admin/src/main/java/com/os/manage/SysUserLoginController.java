package com.os.manage;

import com.os.Indempotency.Idempotency;
import com.os.entity.SysUser;
import com.os.manage.login.SysUserLoginService;
import com.os.manage.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.os.result.ResultEntity;

@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
public class SysUserLoginController {

    private final SysUserLoginService sysUserLoginService;

    private final MailService mailService;

    @RequestMapping("/login")
    public ResultEntity<Object> loginByLoginName(@RequestParam String loginName){

        return sysUserLoginService.loginByLoginName(loginName);
    }

    @RequestMapping("/register")
    public ResultEntity createAccount(@RequestBody SysUser sysUser){
        return  mailService.sendToken(sysUser.getMail());
    }
}
