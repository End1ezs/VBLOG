package com.vblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vblog.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-12 09:46:38
 */
//@Repository(value="userMapper")
public interface UserMapper extends BaseMapper<User> {

}
