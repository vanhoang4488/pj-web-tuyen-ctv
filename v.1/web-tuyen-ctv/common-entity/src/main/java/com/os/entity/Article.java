package com.os.entity;

import com.os.annotaion.Column;
import com.os.annotaion.Id;
import com.os.annotaion.Table;
import lombok.Data;

@Table
@Data
public class Article {

    @Id
    private String articleId;
    @Column
    private String userId;
    @Column
    private String title;
    @Column
    private String thumbnail;
    @Column
    private String body;
}
