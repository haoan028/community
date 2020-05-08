package com.haoan.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Component
public class OSSProvider {
    @Value("${oss.upload.AccessKeyID}")
    private String accessKeyId;
    @Value("${oss.upload.AccessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.upload.bucketName}")
    private String bucketName;
    @Value("${oss.upload.endpoint}")
    private String endpoint;

    public String uploadPic(InputStream fileStram, String fileName){
        fileName= "images/"+UUID.randomUUID()+fileName;
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, fileStram);
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
        ossClient.shutdown();
        return url.toString();
    }
}
