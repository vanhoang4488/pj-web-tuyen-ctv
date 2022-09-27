package com.os;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class IdFactory {

    private static long incrementer = 0L;

    private static long lastTimestamp = -1L;

    private static long systemId = -1L;

    public static void main(String[] args) throws Exception {
        systemId = genSystemId();
        System.out.println(generateId());
    }

    private static long generateId() throws Exception{
        long timestamp = System.currentTimeMillis();

        if(systemId < 0)
            throw  new IOException("====> systemId not constructor");

        if(timestamp < lastTimestamp)
            throw new IOException("=====> timestamp not vaild, become < lastTimestamp");


        if(timestamp == lastTimestamp) {
            incrementer = incrementer + 1;
        }

        lastTimestamp = timestamp;

        return timestamp | systemId | incrementer;
    }

    private static int genSystemId() throws UnknownHostException {
        String hostAddress = Inet4Address.getLocalHost().getHostAddress();
        System.out.println("====> host address: " + hostAddress);
        String[] numberStrs =  hostAddress.split("\\.");

        int[] numbers = new int[numberStrs.length];
        for(int index = 0; index < numberStrs.length; index++)
            numbers[index] = Integer.parseInt(numberStrs[index]);

        int id = 427546*numbers[0] + 120088*numbers[1] + 20275*numbers[2] + 887246*numbers[3];
        return id;
    }
}

