package com.vblog.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.fastjson.JSON;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.dto.AddArticleDto;
import com.vblog.domain.entity.Article;
import com.vblog.domain.entity.Category;
import com.vblog.domain.entity.Menu;
import com.vblog.domain.vo.ArticleSelectVo;
import com.vblog.domain.vo.CategoryVo;
import com.vblog.domain.vo.ExcelCategoryVo;
import com.vblog.domain.vo.PageVo;
import com.vblog.enums.AppHttpCodeEnum;
import com.vblog.service.ArticleService;
import com.vblog.service.CategoryService;
import com.vblog.utils.BeanCopyUtils;
import com.vblog.utils.WebUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category/listAllCategory")
    public ResponseResult<CategoryVo> listAllCategory() {
        return categoryService.listAllCategory();
    }

    @PostMapping("/article")
    public ResponseResult add(@RequestBody AddArticleDto article) {
        return articleService.add(article);
    }


    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/category/export")
    public void export(HttpServletResponse httpServletResponse) {
        // 设置下载文件的请求头
        try {
            WebUtils.setDownLoadHeader("分类.xlsx", httpServletResponse);
            //获取需要导出的数据
            List<Category> category = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(category, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(httpServletResponse.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            // 如果出现异常也要响应json
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(responseResult));
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/article/list")
    public ResponseResult<PageVo> listArticle(@RequestParam Integer pageNum, Integer pageSize, String title, String summary) {
        return articleService.listArticle(pageNum, pageSize, title, summary);
    }

    @GetMapping("/article/{id}")
    public ResponseResult<ArticleSelectVo> selectArticle(@PathVariable Long id) {
        return articleService.selectArticle(id);
    }
    @PutMapping("/article")
    public ResponseResult updateArticle(@RequestBody AddArticleDto addArticleDto) {
        return articleService.updateArticle(addArticleDto);
    }
    @DeleteMapping("/article/{id}")
    public ResponseResult deleteArticle(@PathVariable Long id) {
        return articleService.deleteArticle(id);
    }

}


