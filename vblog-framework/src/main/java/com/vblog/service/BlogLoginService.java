package com.vblog.service;

import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.User;

public interface BlogLoginService {

    ResponseResult login(User user);
}
