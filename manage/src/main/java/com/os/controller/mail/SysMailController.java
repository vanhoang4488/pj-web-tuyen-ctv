package com.os.controller.mail;

import com.os.result.ResultEntity;
import com.os.service.mail.MailBiz;
import com.os.service.mail.MailMessage;
import com.os.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/mail")
public class SysMailController {

    @Autowired
    private MailBiz mailBiz;

    @RequestMapping("/sendToken")
    public ResultEntity sendToken(@RequestParam("mail") String mail){
        String token = StringUtils.createToken();
        return mailBiz.sendMail(mail, MailMessage.TOKEN, token);
    }
}
