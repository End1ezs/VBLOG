package com.vblog.domain.vo;

import com.vblog.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutersVo {
    private List<Menu>menus;
}
