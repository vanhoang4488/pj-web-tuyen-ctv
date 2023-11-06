package com.os.upload.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;

import java.io.*;

@Slf4j
public class ImageParser {

    int number = 0;
    private String targetDir;
    private String baseUrl;

    public ImageParser(String targetDir, String baseUrl){
        this.targetDir = targetDir;
        this.baseUrl = baseUrl;
    }

    public String parse(byte[] data, String extName){
        return parse(new ByteArrayInputStream(data), extName);
    }

    public String parse(InputStream in, String extName){
        if(extName.lastIndexOf(".") > -1){
            extName = extName.substring(extName.lastIndexOf(".") + 1);
        }

        String filename = "image_" + (number++) + "." + extName;
        File target = new File(targetDir);
        if(!target.exists()) target.mkdirs();

        try{
            IOUtils.copy(in, new FileOutputStream(new File(target, filename)));
        }
        catch (IOException e){
            log.error("=====> save file into folder failed: {}", e.getMessage(), e);
        }
        log.info("=====> path save file: {}", baseUrl + filename);
        return baseUrl + filename;
    }
}
