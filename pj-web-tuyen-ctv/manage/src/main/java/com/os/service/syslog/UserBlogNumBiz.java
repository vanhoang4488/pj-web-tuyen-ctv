package com.os.service.syslog;

import com.os.dao.UserBlogNumDao;
import com.os.entity.UserBlogNum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBlogNumBiz {

    private final UserBlogNumDao userBlogNumDao;

    public List<UserBlogNum> getAllUserBlogNum(){
        return userBlogNumDao.getAllUserBlogNum();
    }
}
