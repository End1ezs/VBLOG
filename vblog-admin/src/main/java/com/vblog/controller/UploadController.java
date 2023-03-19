package com.vblog.controller;

import com.vblog.domain.ResponseResult;
import com.vblog.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;
    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("img") MultipartFile multipartFile){
        try {
            return uploadService.uploadImg(multipartFile);
        }catch(RuntimeException e){
            e.printStackTrace();
            throw new RuntimeException("文件上传失败");
        }
    }
}
