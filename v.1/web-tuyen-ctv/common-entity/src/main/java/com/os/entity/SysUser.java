package com.os.entity;

import com.os.annotaion.Column;
import com.os.annotaion.Id;
import com.os.annotaion.Table;
import lombok.Data;

@Table
@Data
public class SysUser {

    @Id
    private String userId;
    @Column
    private String loginName;
    @Column
    private String userName;
    @Column
    private String password;
    @Column
    private String mail;

    /**trường đại diện giá trị của "xác nhận lại mật khẩu"*/
    private String confrimPassword;
}
