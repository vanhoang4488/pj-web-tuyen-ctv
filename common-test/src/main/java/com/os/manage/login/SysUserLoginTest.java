package com.os.manage.login;

import com.os.RequestUtils;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

public class SysUserLoginTest {

    @Test
    public void loginByLoginName(){
        Map<String, String> params = new TreeMap<>();
        String loginName = "hoangnv";
        params.put("loginName", loginName);
        System.out.println(loginName);
        String result = RequestUtils.execute("/admin/sys/user/login", params);
        System.out.println("Result: " + result);
    }
}
