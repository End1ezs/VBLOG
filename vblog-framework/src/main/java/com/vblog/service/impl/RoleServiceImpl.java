package com.vblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.Delete;
import com.baomidou.mybatisplus.core.injector.methods.DeleteById;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.dto.AddRoleDto;
import com.vblog.domain.dto.ChangeRoleDto;
import com.vblog.domain.entity.ArticleTag;
import com.vblog.domain.entity.Role;
import com.vblog.domain.entity.RoleMenu;
import com.vblog.domain.vo.PageVo;
import com.vblog.domain.vo.RoleVo;
import com.vblog.mapper.RoleMapper;
import com.vblog.mapper.RoleMenuMapper;
import com.vblog.service.RoleMenuService;
import com.vblog.service.RoleService;
import com.vblog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-03-16 21:54:40
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员，如果是返回集合中只需要有admin
        //否则查询用户所具有的角色信息
        if (id == 1L) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        Page<Role> rolePage = new Page<>();
        rolePage.setCurrent(pageNum);
        rolePage.setSize(pageSize);

        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(roleName), Role::getRoleName, roleName);
        lambdaQueryWrapper.eq(StringUtils.hasText(status), Role::getStatus, status);
        lambdaQueryWrapper.orderByAsc(Role::getRoleSort);
        page(rolePage, lambdaQueryWrapper);
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(rolePage.getRecords(), RoleVo.class);
        PageVo rolePageVo = new PageVo(roleVos, rolePage.getTotal());
        return ResponseResult.okResult(rolePageVo);
    }

    @Override
    public ResponseResult changeStatus(ChangeRoleDto changeRoleDto) {
        Role role = getById(changeRoleDto.getRoleId());
        role.setStatus(changeRoleDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getRoleKey, addRoleDto.getRoleKey());
        List<Role> rolelist = list(lambdaQueryWrapper);
        Long roleid = null;
        for (Role role1 : rolelist) {
            roleid = role1.getId();
        }
        List<String> MenuIds = addRoleDto.getMenuIds();
        for (String menuid : MenuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleid);
            roleMenu.setMenuId(Long.valueOf(menuid));
            roleMenuService.save(roleMenu);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRole(long id) {
        Role role = roleMapper.selectById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Transactional
    @Override
    public ResponseResult updateRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        updateById(role);

        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getRoleKey, addRoleDto.getRoleKey());
        List<Role> rolelist = list(lambdaQueryWrapper);
        Long roleid = null;
        for (Role role1 : rolelist) {
            roleid = role1.getId();
            roleMenuMapper.deleteById(roleid);
        }

        List<String> MenuIds = addRoleDto.getMenuIds();

        for (String menuid : MenuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleid);
            roleMenu.setMenuId(Long.valueOf(menuid));
            roleMenuService.save(roleMenu);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(Long id) {
        roleMapper.deleteById(id);
        return ResponseResult.okResult();
    }
}
