package com.vblog.controller;

import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.Menu;
import com.vblog.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class SystemController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult getMenuList(@RequestParam(required = false) String status, String menuName) {
        return menuService.getMenuList(status, menuName);
    }
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }
    @GetMapping("{id}")
    public ResponseResult getMenu(@PathVariable Long id) {
        return menuService.getMenu(id);
    }
    @PutMapping()
    public ResponseResult updateMenu(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }

    @DeleteMapping("{id}")
    public ResponseResult deleteMenu(@PathVariable Long id) {
        return menuService.deleteMenu(id);
    }
    @GetMapping("/treeselect")
    public ResponseResult getTreeselect() {
        return menuService.getTreeselect();
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable Long id) {
        return menuService.roleMenuTreeselect(id);
    }
}
