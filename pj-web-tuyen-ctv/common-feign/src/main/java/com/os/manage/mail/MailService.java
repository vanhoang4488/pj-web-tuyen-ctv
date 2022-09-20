package com.os.manage.mail;

import com.os.BaseService;
import com.os.config.FeignAuthConfig;
import com.os.result.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${manage.application.name}",
             configuration = FeignAuthConfig.class,
             fallback = MailFallbackImpl.class,
             path = "${manage.context-path}")
public interface MailService extends BaseService {

    @RequestMapping("/sys/mail/sendToken")
    public ResultEntity sendToken(@RequestParam String mail);
}
