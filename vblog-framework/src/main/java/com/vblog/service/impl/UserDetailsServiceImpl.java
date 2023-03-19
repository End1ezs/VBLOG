package com.vblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vblog.constants.SystemConstants;
import com.vblog.domain.entity.LoginUser;
import com.vblog.domain.entity.User;
import com.vblog.mapper.MenuMapper;
import com.vblog.mapper.UserMapper;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        // 判断是否查到用户 如果没查到抛出异常
        // 查到用户 返回用户信息
        LambdaQueryWrapper<User> lqm = new LambdaQueryWrapper<>();
        lqm.eq(User::getUserName, username);

        User user = userMapper.selectOne(lqm);
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }

        // 查询权限信息封装
        // 如果是后台用户，才需要查询权限封装
        if (user.getType().equals(SystemConstants.ADMAIN)) {
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user, list);
        }

        return new LoginUser(user, null);
    }
}
