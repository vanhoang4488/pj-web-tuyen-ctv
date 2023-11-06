package com.os.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

public class StringUtils extends org.apache.commons.lang3.StringUtils{

    public static String genRanNum(int size){
        Random random = new Random();
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < size; i++)
            str.append(random.nextInt(9));
        return str.toString();
    }

    public static Set<String> getIdSet(String ids){
        String[] idArr = ids.split(",");
        Set<String> idSet = new HashSet<>();
        IntStream.range(0, idArr.length).forEach(index -> idSet.add(idArr[index]));
        return idSet;
    }

    public static String createToken(){
        return UUID.randomUUID().toString();
    }
}
