package com.vblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vblog.domain.entity.RoleMenu;
import org.springframework.stereotype.Service;


/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 14:26:18
 */
@Service
public interface RoleMenuService extends IService<RoleMenu> {
}

