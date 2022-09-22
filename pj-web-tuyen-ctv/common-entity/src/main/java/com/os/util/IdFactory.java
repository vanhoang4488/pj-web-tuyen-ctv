package com.os.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.locks.ReentrantLock;

/**lớp tạo khóa chính cho entity*/
@Slf4j
public class IdFactory {

    private static volatile  IdFactory instance;

    private final static ReentrantLock LOCK = new ReentrantLock();

    /**bộ đếm số bản ghi đồng thời được tạo*/
    private long incrementer = 0L;

    /**thời điểm lần cuối tạo khóa chính*/
    private long lastTimestamp = -1L;

    /**Id của hệ thống*/
    private long systemId = -1L;

    private IdFactory(){
        this.systemId = this.genSystemId();
    }

    public static IdFactory getInstance(){
        if(instance == null){
            LOCK.lock();
            if(instance == null) {
                instance = new IdFactory();
            }
            LOCK.unlock();
        }
        return instance;
    }

    public synchronized long generateId() throws IOException{
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

    private long genSystemId(){
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            log.info("====> host address: {}", hostAddress);
            String[] numberStrs =  hostAddress.split("\\.");

            long[] numbers = new long[numberStrs.length];
            for(int index = 0; index < numberStrs.length; index++)
                numbers[index] = Long.parseLong(numberStrs[index]);

            long id = 427546*numbers[0] + 120088*numbers[1] + 20275*numbers[2] + 887246*numbers[3];
            return id;
        } catch (UnknownHostException e) {
            throw new RuntimeException("====> constructor systemid failed");
        }
    }
}