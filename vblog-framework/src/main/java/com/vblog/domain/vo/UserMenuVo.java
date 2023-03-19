package com.vblog.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMenuVo {
    private List<MenuTreeVo> menus;
    private List<String> checkedKeys;
}
