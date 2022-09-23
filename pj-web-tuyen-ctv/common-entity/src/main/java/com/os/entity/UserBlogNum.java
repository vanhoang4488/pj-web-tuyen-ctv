package com.os.entity;

import com.os.annotaion.Column;
import com.os.annotaion.Id;
import com.os.annotaion.Table;
import lombok.Data;

@Data
@Table
public class UserBlogNum {

    @Id
    private long userId;
    @Column
    private String userName;
    @Column
    private long num;
    @Column
    private long total_views;
    @Column
    private String interestest;
    @Column
    private String key_interestest;
    @Column
    private long views_interestest;
}
