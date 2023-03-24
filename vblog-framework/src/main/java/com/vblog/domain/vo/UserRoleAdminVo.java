package com.vblog.domain.vo;

import com.vblog.domain.entity.Role;
import com.vblog.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleAdminVo {
    private List<String> roleIds;
    private List<Role> roles;
    private User user;
}
