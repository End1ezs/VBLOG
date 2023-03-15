package com.vblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vblog.domain.entity.User;
import com.vblog.mapper.UserMapper;
import com.vblog.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-15 09:51:56
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
