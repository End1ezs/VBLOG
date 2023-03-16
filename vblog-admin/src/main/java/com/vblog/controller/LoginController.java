package com.vblog.controller;

import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.User;
import com.vblog.enums.AppHttpCodeEnum;
import com.vblog.exception.SystemException;
import com.vblog.service.SystemLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private SystemLoginService systemLoginService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return systemLoginService.login(user);
    }
}
