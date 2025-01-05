package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.sky.properties.QiniuOssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@Slf4j
public class QiniuOssUtil {

    @Resource
    QiniuOssProperties properties;

    public String upload(MultipartFile file) {
        try {
            //log.info("file {}", file);
            String originalFilename = file.getOriginalFilename();
            // 截取拓展名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 拼接随机文件名
            String imgName = "sky_take_out/" + UUID.randomUUID() + extension;
            //log.info(" {} 上传成功", imgName);

            // 上传成功，保存到七牛云
            String path = null;
            if (!file.isEmpty()) {
                log.info("开始上传");
                InputStream inputStream = file.getInputStream();
                path = uploadQNImg(inputStream, imgName);

                log.info("保存路径 {}", "https://" + path);
            }

            return "https://" + path;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 调用七牛云Api上传图片
     *
     * @param file 图片文件
     * @param path 存储路径
     * @return 图片访问路径
     */
    public String uploadQNImg(InputStream file, String path) {
        // 构造一个带指定Zone对象的配置类, 注意这里的Zone.zone0需要根据主机选择
        UploadManager uploadManager = new UploadManager(new Configuration(Zone.zone0()));
        Auth auth = Auth.create(properties.getAccessKey(), properties.getSecretKey());
        // 根据命名空间生成的上传token
        String token = auth.uploadToken(properties.getBucketName());

        try {
            Response res = uploadManager.put(file, path, token, null, null);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛云出错：" + res.toString());
            }
            JSONObject result = JSONObject.parseObject(res.bodyString());
            return properties.getDomain() + "/" + result.getString("key");
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
    }
}
