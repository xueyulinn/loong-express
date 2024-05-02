package com.loong.controller.admin;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.loong.constant.MessageConstant;
import com.loong.result.Result;
import com.loong.utils.AliOssUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public Result<String> uploadFile(MultipartFile file) {
        try {

            String originalFileName = file.getOriginalFilename();

            String fileExention = originalFileName.substring(originalFileName.lastIndexOf("."));

            String storageFileName = UUID.randomUUID().toString() + fileExention;

            String uploadPath = aliOssUtil.upload(file.getBytes(), storageFileName);
            
            return Result.success(uploadPath);
        } catch (IOException e) {
            log.info("upload file failed");
            return Result.error(MessageConstant.UPLOAD_FILE_FAILED);
        }

    }

}
