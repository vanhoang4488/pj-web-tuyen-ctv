package com.os.dao;

import com.os.entity.UserBlogNum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserBlogNumDao {

    @Select(value = "SELECT * FROM userblognum")
    List<UserBlogNum> getAllUserBlogNum();
}
