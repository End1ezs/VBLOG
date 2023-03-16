package com.vblog.service;

import com.vblog.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.nio.channels.MulticastChannel;

public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
