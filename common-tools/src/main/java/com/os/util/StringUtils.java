package com.os.util;

import java.util.Random;

public class StringUtils {

    public static String genRanNum(int size){
        Random random = new Random();
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < size; i++)
            str.append(random.nextInt(9));
        return str.toString();
    }
}
