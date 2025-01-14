package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.QiniuOssUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {

    @Resource
    private QiniuOssUtil qiniuOssUtil;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传开始 {}",file);
        String path = qiniuOssUtil.upload(file);
        if (path == null){
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
        return Result.success(path);
    }
}
