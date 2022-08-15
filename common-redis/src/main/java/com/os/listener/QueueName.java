package com.os.listener;

public enum QueueName {

    SYS_ADMIN("admin");

    private String key;

    private QueueName(String key){
        this.key = key;
    }

    public String getKey(){
        return this.key;
    }
}
