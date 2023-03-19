package com.vblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vblog.domain.entity.UserRole;
import com.vblog.mapper.UserRoleMapper;
import com.vblog.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 16:05:48
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
