package com.vblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectById;
import com.baomidou.mybatisplus.core.injector.methods.SelectList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.dto.AddCommentDto;
import com.vblog.domain.dto.AddTagDto;
import com.vblog.domain.dto.TagListDto;
import com.vblog.domain.entity.Tag;
import com.vblog.domain.vo.PageVo;
import com.vblog.domain.vo.TagVo;
import com.vblog.enums.AppHttpCodeEnum;
import com.vblog.exception.SystemException;
import com.vblog.mapper.TagMapper;
import com.vblog.service.TagService;
import com.vblog.utils.BeanCopyUtils;
import com.vblog.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-03-16 18:57:28
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> lqm = new LambdaQueryWrapper<>();
        //先判断单独检索条件name和remark是否为空，然后再进行判断相等，如果否的话，那就查询全部
        // 如果是的话，lqm生效，增加AND (name = ?)这样的条件 从Tag::getName搜索tagListDto.getName()的值
        //SELECT COUNT(*) AS total FROM sg_tag WHERE del_flag = 0 AND (name = ?)
        lqm.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        lqm.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, lqm);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult postTag(Tag tag) {
        tag.setCreateBy(SecurityUtils.getUserId());
        if (!StringUtils.hasText(tag.getName()) || !StringUtils.hasText(tag.getRemark())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<TagVo> getTag(Long id) {
//        LambdaQueryWrapper<Tag> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(Tag::getId,id);
//        SelectList(lqw);
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
        if (!StringUtils.hasText(tag.getName()) || !StringUtils.hasText(tag.getRemark())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
//        LambdaQueryWrapper<Tag> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(Tag::getId,tag.getId());
//        TagMapper.update(tag,lqw);
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<TagVo> listAllTag() {
        List<Tag> list = list();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }
}
