package com.vblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.dto.AddTagDto;
import com.vblog.domain.dto.TagListDto;
import com.vblog.domain.entity.Tag;
import com.vblog.domain.vo.PageVo;
import com.vblog.domain.vo.TagVo;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-03-16 14:02:22
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult postTag(Tag tag);

    ResponseResult deleteTag(Long id);

    ResponseResult<TagVo> getTag(Long id);

    ResponseResult updateTag(Tag tag);

    ResponseResult<TagVo> listAllTag();
}

