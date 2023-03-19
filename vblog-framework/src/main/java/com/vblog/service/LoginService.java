package com.vblog.service;

import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.Menu;
import com.vblog.domain.entity.User;
import org.springframework.stereotype.Service;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();

}
