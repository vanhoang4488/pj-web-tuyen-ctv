package com.os.manage.login;

import com.os.result.ResultEntity;
import org.springframework.stereotype.Component;

@Component
public class UserLoginFallbackImpl implements UserLoginService {

    @Override
    public ResultEntity<Object> loginByLoginName(String loginName) {
        return ResultEntity.failed().build();
    }
}
