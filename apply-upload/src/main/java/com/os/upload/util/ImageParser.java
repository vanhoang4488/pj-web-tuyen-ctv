package com.os.upload.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;

import java.io.*;
import java.security.MessageDigest;

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
        if(fullname.lastIndexOf(".") > -1){
            extName = fullname.substring(fullname.lastIndexOf(".") + 1);
        }

        try{
            //mã hóa fullname theo md5
            MessageDigest md5 = MessageDigest.getInstance("md5", fullname);
            fullname = new String(md5.digest());

            String filename = "image_" + fullname + "." + extName;
            File target = new File(targetDir);
            if(!target.exists()) target.mkdirs();

            // lưu file vào thư mục.
            IOUtils.copy(in, new FileOutputStream(new File(target, filename)));

            // todo lưu thông tin các file vào 1 bảng trong database.

            log.info("=====> path save file: {}", baseUrl + filename);
            return baseUrl + filename;
        }
        catch (Exception e){
            log.error("=====> save file into folder failed: {}", e.getMessage(), e);
        }

        return "";
    }
}
