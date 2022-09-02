package com.os.manage.mail;

import com.os.result.ResultEntity;
import org.springframework.stereotype.Component;

@Component
public class MailFallbackImpl implements MailService{

    public ResultEntity sendToken(String mail){
        return ResultEntity.failed().message("common.request.timeout").build();
    }
}
