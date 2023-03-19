package com.vblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.User;
import com.vblog.domain.vo.PageVo;
import com.vblog.domain.vo.UserAdminGetVo;
import com.vblog.domain.vo.UserInfoVo;
import com.vblog.enums.AppHttpCodeEnum;
import com.vblog.exception.SystemException;
import com.vblog.mapper.UserMapper;
import com.vblog.service.UserService;
import com.vblog.utils.BeanCopyUtils;
import com.vblog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-15 09:51:56
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult userInfo() {
        //获取用户当前id
        Long userId = SecurityUtils.getUserId();

        // 根据用户id查询用户信息
        User user = getById(userId);
        // 封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(User user) {
        // 对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        // 对数据进行是否已存在的判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USER_INFO_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        // 对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        save(user);
        // 存入数据库
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserlist(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User>lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(userName),User::getUserName,userName);
        lambdaQueryWrapper.eq(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        lambdaQueryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        page(page,lambdaQueryWrapper);
        List<UserAdminGetVo> userAdminGetVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserAdminGetVo.class);
        PageVo pageVo = new PageVo(userAdminGetVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, userName);
        return count(lambdaQueryWrapper) > 0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getNickName, nickName);
        return count(lambdaQueryWrapper) > 0;
    }

}
