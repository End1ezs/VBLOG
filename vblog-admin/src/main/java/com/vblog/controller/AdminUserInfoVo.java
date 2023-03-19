package com.vblog.controller;

import com.vblog.domain.vo.UserInfoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AdminUserInfoVo {

    private List<String> permissions;
    private List<String> roles;
    private UserInfoVo user;

}
