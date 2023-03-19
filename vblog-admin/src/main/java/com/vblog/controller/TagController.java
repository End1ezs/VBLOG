package com.vblog.controller;

import com.vblog.domain.ResponseResult;
import com.vblog.domain.dto.AddCommentDto;
import com.vblog.domain.dto.AddTagDto;
import com.vblog.domain.dto.TagListDto;
import com.vblog.domain.entity.Tag;
import com.vblog.domain.entity.User;
import com.vblog.domain.vo.PageVo;
import com.vblog.domain.vo.TagVo;
import com.vblog.service.TagService;
import com.vblog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }
    @PostMapping
    public ResponseResult postTag(@RequestBody AddTagDto addTagDto) {
        Tag tag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        return tagService.postTag(tag);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Long id) {

        return tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult<TagVo> getTag(@PathVariable Long id) {
        return tagService.getTag(id);
    }
    @PutMapping()
    public ResponseResult updateTag(@RequestBody AddTagDto addTagDto) {
        Tag tag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        return tagService.updateTag(tag);
    }

    @GetMapping("/listAllTag")
    public ResponseResult<TagVo> listAllTag() {
        return tagService.listAllTag();
    }

}
