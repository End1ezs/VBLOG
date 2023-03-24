package com.vblog.controller;

import com.vblog.domain.ResponseResult;
import com.vblog.domain.dto.AddRoleDto;
import com.vblog.domain.dto.ChangeRoleDto;
import com.vblog.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        return roleService.getRoleList(pageNum, pageSize, roleName, status);
    }
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole() {
        return roleService.listAllRole();
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleDto changeRoleDto) {
        return roleService.changeStatus(changeRoleDto);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto) {
        return roleService.addRole(addRoleDto);
    }
    @GetMapping("{id}")
    public ResponseResult getRole(@PathVariable long id) {
        return roleService.getRole(id);
    }
    @PutMapping
    public ResponseResult updateRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.updateRole(addRoleDto);
    }
    @DeleteMapping("{id}")
    public ResponseResult deleteRole(@PathVariable Long id){
        return roleService.deleteRole(id);
    }


}
