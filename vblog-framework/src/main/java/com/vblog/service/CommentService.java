package com.vblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-03-12 20:03:03
 */


public interface CommentService extends IService<Comment> {
    ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize);
}

