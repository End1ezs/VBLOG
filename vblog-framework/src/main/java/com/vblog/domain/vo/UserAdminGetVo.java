package com.vblog.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminGetVo {
    private Long id;

    //用户名
    private String userName;
    //昵称
    private String nickName;
    //密码

    private String status;
    //邮箱
    private String email;
    //手机号
    private String phonenumber;
    //用户性别（0男，1女，2未知）
    private String sex;
    //头像
    private String avatar;
    //创建人的用户id

    //创建时间
    private Date createTime;
    //更新人
    //更新时间
    private Date updateTime;
    private String updateBy;
    //删除标志（0代表未删除，1代表已删除）

}
