package com.vblog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vblog.constants.SystemConstants;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.Menu;
import com.vblog.domain.entity.RoleMenu;
import com.vblog.domain.entity.UserRole;
import com.vblog.domain.vo.MenuTreeVo;
import com.vblog.domain.vo.MenuVo;
import com.vblog.domain.vo.UserMenuVo;
import com.vblog.mapper.MenuMapper;
import com.vblog.mapper.UserRoleMapper;
import com.vblog.service.MenuService;
import com.vblog.service.RoleMenuService;
import com.vblog.utils.BeanCopyUtils;
import com.vblog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-03-16 21:35:45
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Override
    public List<String> selectPermsByUserId(Long id) {
        // 根据用户id查询对应的权限关键字
        // 如果是管理员id=1 返回所有权限

        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> wapper = new LambdaQueryWrapper<>();
            wapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            wapper.eq(Menu::getStatus, SystemConstants.MENU_STATUS_NORMAL);
            List<Menu> menuList = list(wapper);
            List<String> perms = menuList.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }
        // 否则返回其所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        // 判断是否是管理员
        if (SecurityUtils.isAdmin()) {
            // 如果是 返回所有符合需求的menu
            menus = menuMapper.selectAllRouterMenu();
        } else {
            // 否则返回当前用户具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        // 构建tree
        // 先找出第一层的菜单，然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        return menuTree;
    }

    @Override
    public ResponseResult getMenuList(String status, String menuName) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(status), Menu::getStatus, status);
        lambdaQueryWrapper.eq(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
        List<Menu> list = list(lambdaQueryWrapper);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(list, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

//    @Override
//    public ResponseResult getMenuListAll() {
//        List<Menu> list = list();
//        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(list, MenuVo.class);
//        return ResponseResult.okResult(menuVos);
//    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenu(Long id) {
        Menu menu = menuMapper.selectById(id);
        MenuVo menuVo = BeanCopyUtils.copyBean(menu, MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if (Objects.equals(menu.getParentId(), menu.getId())) {
            throw new RuntimeException("父菜单不能是自己");
        }
        menuMapper.updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getParentId, id);
        List<Menu> list = list(lambdaQueryWrapper);
        if (list.isEmpty()) {
            menuMapper.deleteById(id);
        } else {
            throw new RuntimeException("存在子菜单不允许删除");
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTreeselect() {
        List<Menu> list = menuMapper.selectAllRouterMenu();
        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(list, MenuTreeVo.class);
        List<MenuTreeVo> menuTree = builderMenuTree1(menuTreeVos, 0L);
        return ResponseResult.okResult(menuTree);
    }

    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        // 判断是否是管理员
        if (SecurityUtils.isAdmin()) {
            // 如果是 返回所有符合需求的menu
            menus = menuMapper.selectAllRouterMenu();
        } else {
            // 否则返回当前用户具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(id);
        }
        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menus, MenuTreeVo.class);
        List<MenuTreeVo> menuTreevo = builderMenuTree1(menuTreeVos, 0L);

        //用户查角色
        LambdaQueryWrapper<UserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoles = userRoleMapper.selectList(lambdaQueryWrapper);
        List<String> role_ids = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            String s = userRole.getRoleId().toString();
            role_ids.add(s);
        }
        //   角色查菜单权限
        Set<String> menu_ids =new HashSet<>();
        for (String roleId : role_ids) {
            LambdaQueryWrapper<RoleMenu> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(RoleMenu::getRoleId, roleId);
            List<RoleMenu> list = roleMenuService.list(lambdaQueryWrapper1);
            for (RoleMenu roleMenu : list) {
                menu_ids.add(roleMenu.getMenuId().toString());
            }
        }
        List<String>list_menuids = new ArrayList<String>(menu_ids);
        UserMenuVo roleVo = new UserMenuVo(menuTreevo,list_menuids);
        return ResponseResult.okResult(roleVo);
    }


    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream().
                filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    // TODO 这里将vo中多余的menuName去掉

    private List<MenuTreeVo> builderMenuTree1(List<MenuTreeVo> menus, Long parentId) {
        List<MenuTreeVo> menuTree = menus.stream().
                filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus))).map(menu -> menu.setLabel(menu.getMenuName()))
                .collect(Collectors.toList());

        return menuTree;
    }

    /**
     * 从menus中 获取传入菜单的所有子菜单
     *
     * @param menu  父菜单
     * @param menus 所有菜单
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId()
                        // 递归调用，找到子菜单的子菜单放入
                        .equals(menu.getId())).map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

    private List<MenuTreeVo> getChildren(MenuTreeVo menu, List<MenuTreeVo> menus) {
        List<MenuTreeVo> childrenList = menus.stream()
                .filter(m -> m.getParentId()
                        // 递归调用，找到子菜单的子菜单放入
                        .equals(menu.getId())).map(m -> m.setChildren(getChildren(m, menus))).map(m -> m.setLabel(m.getMenuName()))
                .collect(Collectors.toList());
        return childrenList;
    }


}
