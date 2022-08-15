package com.os.admin;

import com.os.Indempotency.Idempotency;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.os.result.ResultEntity;

@RestController
@RequestMapping("/sys/user")
public class SysUserLoginBiz {

    @RequestMapping("/login")
    @Idempotency
    public ResultEntity<String> loginByLoginName(@RequestParam String loginName){
        return ResultEntity.builder().message("success").build();
    }
}
