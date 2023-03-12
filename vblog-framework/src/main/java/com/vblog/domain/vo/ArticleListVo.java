package com.vblog.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListVo {
    private Long id;
    //标题

    private String title;
    //文章摘要
    private String summary;
    //所属分类id
    private String categoryName;
    private String thumbnail;
    private Long viewCount;
    //是否允许评论 1是，0否
    private Date createTime;


}
