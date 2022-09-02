package com.os.service.mail;

public abstract class HandleMail {

    public static String createSubject(){
        return "mail test";
    }

    public static String createText(String extraContent){
        return "Đây là nội dung: " + extraContent;
    }
}
