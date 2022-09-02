package com.os.controller.login;

import com.os.result.ResultEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/user")
public class SysUserLoginController {

    @RequestMapping("/findSysUserByLoginName")
    public ResultEntity findSysUserByLoginName(@RequestParam("loginName") String loginName){

        return ResultEntity.success().message("admin.login.success").build();
    }

}
