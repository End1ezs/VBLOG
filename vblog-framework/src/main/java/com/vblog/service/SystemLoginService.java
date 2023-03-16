package com.vblog.service;

import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface SystemLoginService {
    ResponseResult login(User user);
}
