package com.vblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-03-15 09:51:54
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getUserlist(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);
}

