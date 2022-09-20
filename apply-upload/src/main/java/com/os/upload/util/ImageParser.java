package com.os.upload.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.Message;
import org.apache.poi.util.IOUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * class xử lý ảnh, lưu ảnh vào thư mục.
 * đang xung đột với W3cNodeutil.xml2
 */
@Slf4j
public class ImageParser {

    private String targetDir;
    private String baseUrl;

    public ImageParser(String targetDir, String baseUrl){
        this.targetDir = targetDir;
        this.baseUrl = baseUrl;
    }

    public String parse(byte[] data, String fullname){
        return parse(new ByteArrayInputStream(data), fullname);
    }

    public String parse(InputStream in, String fullname){
        // mặc đinh, định dạng svg.
        String extName = "svg";
        // nếu fullName có chứa định dạng theo kèm thì theo định dạng của fullname
        int pos = fullname.lastIndexOf(".");
        if(pos > -1){
            extName = fullname.substring(pos + 1);
            fullname = fullname.substring(0, pos);
        }

        try{
            //mã hóa fullname theo md5
            String filename = this.getFilename("", fullname, extName);

            File target = new File(targetDir + baseUrl);
            if(!target.exists()) target.mkdirs();

            // lưu file vào thư mục.
            IOUtils.copy(in, new FileOutputStream(new File(target, filename)));

            // todo lưu thông tin các file vào 1 bảng trong database.
            return baseUrl + filename;
        }
        catch (Exception e){
            log.error("=====> save file into folder failed: {}", e.getMessage(), e);
        }
        return null;
    }

    private String getFilename(String loginName, String fullname, String extName) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        fullname = loginName+ "&" + fullname;
        md5.update(fullname.getBytes());
        byte[] bytes = md5.digest();

        StringBuilder str = new StringBuilder();
        for(int i = 0; i < bytes.length; i++){
            str.append(Integer.toString((bytes[i]&0xff) + 0x100, 16).substring(1));
        }
        fullname = str.toString();
        return "\\image_" + fullname + "." + extName;
    }
}
