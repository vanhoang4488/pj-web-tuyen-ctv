package com.os.service.mail;

public enum MailMessage {
    TOKEN("Xác nhận gamil", "Nhấn vào đường link sau: \n #{url} \n để xác nhận gmail!");

    private String subject;
    private static final String HEADING = "Kính gửi: ";
    private String body;
    private static final String ENDING = "Trân trọng!!! \n Ban cán bộ dự án";

    private MailMessage(String subject, String body){
        this.subject = subject;
        this.body = body;
    }

    public String getSubject() { return this.subject; }

    public String getHeading() { return HEADING;}

    public String getBody() { return this.body;}

    public String getEnding() { return ENDING;}
}
