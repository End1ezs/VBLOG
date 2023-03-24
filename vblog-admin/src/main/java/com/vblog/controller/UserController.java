package com.vblog.controller;

import com.vblog.domain.ResponseResult;
import com.vblog.domain.dto.AddUserDto;
import com.vblog.service.UserRoleService;
import com.vblog.service.UserService;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/list")
    public ResponseResult getUserlist(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
        return userService.getUserlist(pageNum,pageSize,userName,phonenumber,status);
    }
    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }
    @DeleteMapping("{id}")
    public ResponseResult deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    @GetMapping("{id}")
    public ResponseResult getUser(@PathVariable Long id){
        return userService.getUser(id);
    }
    @PutMapping
    public ResponseResult updateUser(@RequestBody AddUserDto addUserDto){
        return userService.updateUser(addUserDto);
    }
}
