package com.vblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vblog.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-16 14:02:21
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}
