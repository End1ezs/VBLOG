package com.vblog.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {
    private Long id;

    //评论类型（0代表文章评论，1代表友链评论）
    private Long articleId;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    //回复目标评论id
    private String toCommentUserName;
    private Long toCommentId;
    private Long createBy;
    private Date createTime;
    // 评论人的名字
    private String username;
    private List<CommentVo> children;

}
