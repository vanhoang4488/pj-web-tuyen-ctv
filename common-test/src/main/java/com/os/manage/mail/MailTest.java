package com.os.manage.mail;

import com.os.RequestUtils;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

public class MailTest {

    @Test
    public void sendToken(){
        Map<String, String> params = new TreeMap<>();
        String loginName = "hoangnv";
        params.put("loginName", loginName);
        System.out.println(loginName);
        String result = RequestUtils.execute("/admin/sys/mail", params);
        System.out.println("Result: " + result);
    }
}
