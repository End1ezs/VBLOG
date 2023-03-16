package com.vblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vblog.domain.entity.Tag;
import com.vblog.mapper.TagMapper;
import com.vblog.service.TagService;
import org.springframework.stereotype.Service;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-03-16 18:57:28
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
