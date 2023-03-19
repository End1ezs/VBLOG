package com.vblog.service.impl;

import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.LoginUser;
import com.vblog.domain.entity.Menu;
import com.vblog.domain.entity.User;
import com.vblog.service.LoginService;

import com.vblog.service.MenuService;
import com.vblog.utils.JwtUtil;
import com.vblog.utils.RedisCache;
import com.vblog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Service
public class SystemLoginServiceImpl implements LoginService {
    @Autowired
    private MenuService menuService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        // 获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        // 把用户信息存入redis
        redisCache.setCacheObject("login" + userId, loginUser);
        // 把User转换成UserInfoVo
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);

    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("login"+userId);
        return ResponseResult.okResult();
    }

}
