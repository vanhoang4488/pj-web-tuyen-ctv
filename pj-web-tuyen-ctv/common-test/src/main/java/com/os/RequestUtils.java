package com.os;

import cn.hutool.crypto.SecureUtil;
import com.os.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class RequestUtils {

    public static String execute(String path, Map<String, String> params){
        String randomStr = StringUtils.genRanNum(32);
        String timestamp = "" + System.currentTimeMillis();
        params.put("randomStr", randomStr);
        params.put("timestamp", timestamp);
        StringBuilder stringBuilder = new StringBuilder();
        params.forEach((key, value) -> {
            stringBuilder.append(key).append("=").append(value).append("&");
        });

        String sign = SecureUtil.md5(stringBuilder.toString());
        params.put("sign", sign);
        return sendPost(path, params);
    }

    private static String sendPost(String path, Map<String, String> params){
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        StringBuilder content = new StringBuilder();
        params.forEach((key, value) -> {
            content.append(key).append("=").append(value).append("&");
        });
        try{
            URL url = new URL(Common.path+path+"?"+content.toString());
            System.out.println(url.toString());
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null)
                result += line;
        }
        catch (Exception e){
            System.out.println("Post error: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                if(out!=null)
                    out.close();
                if (in!=null)
                    in.close();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
