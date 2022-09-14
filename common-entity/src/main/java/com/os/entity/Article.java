package com.os.entity;

import com.os.annotaion.Column;
import com.os.annotaion.Id;
import com.os.annotaion.Table;
import lombok.Builder;
import lombok.Data;

@Table
@Data
public class Article {

    @Id
    private String articleId;
    /**tác giả của bài báo*/
    @Column
    private String authId;
    @Column
    private String title;
    @Column(defalutValue = "<img src=\"/img_2399a76416787acdcbee1618c550ea68.svg\"/>")
    private String majorImg;
    @Column
    private String thumbnail;
    @Column
    private String content;
}
