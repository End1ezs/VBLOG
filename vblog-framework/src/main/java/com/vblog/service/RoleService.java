package com.vblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.dto.AddRoleDto;
import com.vblog.domain.dto.ChangeRoleDto;
import com.vblog.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-03-16 21:54:39
 */
public interface RoleService extends IService<Role> {


    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeStatus(ChangeRoleDto changeRoleDto);

    ResponseResult addRole(AddRoleDto addRoleDto);
}

