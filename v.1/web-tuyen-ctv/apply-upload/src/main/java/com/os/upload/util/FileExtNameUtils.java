package com.os.upload.util;

public abstract class FileExtNameUtils {

    public static String replace(String wmfPath, String wmf, String jpg){
        int index = wmfPath.lastIndexOf("wmf");
        if(index > -1)
            return wmfPath.substring(0, index) + jpg + wmfPath.substring(index + 3);

        return wmfPath;
    }
}
