package com.vblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectById;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.dto.AddUserDto;
import com.vblog.domain.entity.Role;
import com.vblog.domain.entity.User;
import com.vblog.domain.entity.UserRole;
import com.vblog.domain.vo.PageVo;
import com.vblog.domain.vo.UserRoleAdminVo;
import com.vblog.domain.vo.UserAdminGetVo;
import com.vblog.domain.vo.UserInfoVo;
import com.vblog.enums.AppHttpCodeEnum;
import com.vblog.exception.SystemException;
import com.vblog.mapper.UserMapper;
import com.vblog.service.RoleService;
import com.vblog.service.UserRoleService;
import com.vblog.service.UserService;
import com.vblog.utils.BeanCopyUtils;
import com.vblog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-15 09:51:56
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

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
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(userName), User::getUserName, userName);
        lambdaQueryWrapper.eq(StringUtils.hasText(phonenumber), User::getPhonenumber, phonenumber);
        lambdaQueryWrapper.eq(StringUtils.hasText(status), User::getStatus, status);
        page(page, lambdaQueryWrapper);
        List<UserAdminGetVo> userAdminGetVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserAdminGetVo.class);
        PageVo pageVo = new PageVo(userAdminGetVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        //新增用户时注意密码加密存储
        //用户名不能为空，否则提示：必需填写用户名

        if (!StringUtils.hasText(addUserDto.getUserName())) {
            throw new RuntimeException("必需填写用户名");
            //用户名必须之前未存在，否则提示：用户名已存在
        } else if (userNameExist(addUserDto.getUserName())) {
            throw new RuntimeException("用户名已存在");
        } else if (phoneNumberExist(addUserDto.getPhonenumber())) {
            //手机号必须之前未存在，否则提示：手机号已存在
            throw new RuntimeException("手机号已存在");
        } else if (emailExist(addUserDto.getEmail())) {
            //邮箱必须之前未存在，否则提示：邮箱已存在
            throw new RuntimeException("邮箱已存在");
        }

        String encodePassword = passwordEncoder.encode(addUserDto.getPassword());
        addUserDto.setPassword(encodePassword);
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        save(user);

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, addUserDto.getUserName());
        List<User> list = list(lambdaQueryWrapper);
        String UserID = new String();
        for (User user1 : list) {
            UserID = user1.getId().toString();
        }
        List<String> roleid = addUserDto.getRoleIds();
        for (String s : roleid) {
            UserRole userRole = new UserRole(Long.valueOf(UserID), Long.valueOf(s));
            userRoleService.save(userRole);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUser(Long id) {
        Long currentUserId = SecurityUtils.getLoginUser().getUser().getId();
        if (id.equals(currentUserId)) {
            throw new RuntimeException("不能删除当前用户");
        }
        userMapper.deleteById(id);
        return ResponseResult.okResult();
    }

    /**
     * @param id 查询的用户id
     * @return roleIds：用户所关联的角色id列表
     * <p>
     * roles：所有角色的列表
     * <p>
     * user：用户信息
     */
    @Override
    public ResponseResult getUser(Long id) {
        // 先查用户关联的角色id列表
        LambdaQueryWrapper<UserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserRole::getUserId, id);
        List<UserRole> list = userRoleService.list(lambdaQueryWrapper);
        List<String> roleids = new ArrayList<>();
        for (UserRole userRole : list) {
            roleids.add(userRole.getRoleId().toString());
        }
        // 查所有角色的列表
        List<Role> roles = (List<Role>) roleService.listAllRole().getData();

        // 查用户信息
        User user = userMapper.selectById(id);

        UserRoleAdminVo userRoleAdminVo = new UserRoleAdminVo(roleids, roles, user);
        return ResponseResult.okResult(userRoleAdminVo);
    }

    @Override
    @Transactional
    public ResponseResult updateUser(AddUserDto addUserDto) {
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, user.getUserName());
        User user1 = userMapper.selectOne(lambdaQueryWrapper);
        user.setId(user1.getId());
        updateById(user);
        LambdaQueryWrapper<UserRole> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(UserRole::getUserId, user.getId());
        userRoleService.remove(lambdaQueryWrapper1);
        for (String roleId : addUserDto.getRoleIds()) {
            UserRole userRole = new UserRole(user1.getId(), Long.valueOf(roleId));
            userRoleService.save(userRole);
        }
        return ResponseResult.okResult();
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, userName);
        return count(lambdaQueryWrapper) > 0;
    }

    private boolean phoneNumberExist(String phoneNumber) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getPhonenumber, phoneNumber);
        return count(lambdaQueryWrapper) > 0;
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, email);
        return count(lambdaQueryWrapper) > 0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getNickName, nickName);
        return count(lambdaQueryWrapper) > 0;
    }

}
