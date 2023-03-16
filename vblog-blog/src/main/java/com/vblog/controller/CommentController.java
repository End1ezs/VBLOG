package com.vblog.controller;

import com.vblog.constants.SystemConstants;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.dto.AddCommentDto;
import com.vblog.domain.entity.Comment;
import com.vblog.mapper.CommentMapper;
import com.vblog.service.CommentService;
import com.vblog.utils.BeanCopyUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论", description = "评论相关接口")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto) {
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友链评论列表", notes = "获取一页友链评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小")
    }
    )
    public ResponseResult linkComment(Long articleId, Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.LINK_COMMENT, articleId, pageNum, pageSize);
    }

    @PostMapping("/linkCommentList")
    public ResponseResult addlinkComment(@RequestBody Comment comment) {
        return commentService.addlinkComment(comment);
    }
}
