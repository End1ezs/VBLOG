package com.vblog.controller;

import com.vblog.domain.ResponseResult;
import com.vblog.service.UserRoleService;
import com.vblog.service.UserService;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/list")
    public ResponseResult getUserlist(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
        return userService.getUserlist(pageNum,pageSize,userName,phonenumber,status);
    }
}
