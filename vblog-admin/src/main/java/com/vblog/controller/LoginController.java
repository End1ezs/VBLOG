package com.vblog.controller;

import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.LoginUser;
import com.vblog.domain.entity.Menu;
import com.vblog.domain.entity.User;
import com.vblog.domain.vo.RoutersVo;
import com.vblog.domain.vo.UserInfoVo;
import com.vblog.enums.AppHttpCodeEnum;
import com.vblog.exception.SystemException;
import com.vblog.service.MenuService;
import com.vblog.service.RoleService;
import com.vblog.service.LoginService;
import com.vblog.utils.BeanCopyUtils;
import com.vblog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo() {
        // 获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        // 根据用户id查询权限信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        // 根据用户id查询角色信息

        // 获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        // 封装数据返回
        AdminUserInfoVo adminUserInfo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfo);
    }

    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters() {
        // 查询menu 结果是tree的形式
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        // 封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }
}
