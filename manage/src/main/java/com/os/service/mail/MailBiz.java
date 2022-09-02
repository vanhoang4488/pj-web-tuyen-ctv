package com.os.service.mail;

import com.os.result.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MailBiz {

    @Autowired
    private JavaMailSender mailSender;

    @Value("spring.mail.username")
    private String fromAcc;

    /**
     * lý do sử dụng MimeMessage là để biết gmail đã được gửi thành công hay không!
     * @param to
     * @param mailMessage
     * @param content
     * @return
     */
    public ResultEntity sendMail(String to, MailMessage mailMessage, String content){
        try{
            MimeMessage message =  mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAcc);
            helper.setTo(to);
            helper.setSubject(mailMessage.getSubject());
            helper.setText(this.createBody(to, mailMessage, content));

            mailSender.send(message);
            return ResultEntity.success().message("admin.mail.send.success").build();
        }
        catch(Exception ex){
            log.error("send mail failed: {}", ex.getMessage(), ex);
        }
        return ResultEntity.failed().message("admin.mail.send.failed").build();
    }

    private String createBody(String to, MailMessage mailMessage, String content){
        String heading = this.createHeading(mailMessage.getHeading(), to);
        String body = this.replaceContent(mailMessage.getBody(), content);
        return heading + "\n" + body + "\n" + mailMessage.getEnding();
    }

    private String createHeading(String greating, String name){
        name = name.split("@")[0];
        return greating + name + "!";
    }

    private String replaceContent(String body, String content){
        Pattern pattern = Pattern.compile("#\\{\\w+\\}");
        Matcher matcher = pattern.matcher(body);
        if(matcher.find()){
            body = body.replace(matcher.group(), content);
        }
        return body;
    }

}
