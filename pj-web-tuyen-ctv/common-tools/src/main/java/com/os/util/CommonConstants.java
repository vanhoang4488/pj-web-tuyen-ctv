package com.os.util;

public enum CommonConstants {
    REQUEST_CLIENT("requestClient"),
    ADMIN("admin");
    private String key;

    private CommonConstants(String key) { this.key = key;}

    public String getKey(){
        return this.key;
    }
}
