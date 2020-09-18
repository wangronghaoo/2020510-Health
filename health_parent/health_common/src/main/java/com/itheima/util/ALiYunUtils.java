package com.itheima.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ALiYunUtils {

    public static String endpoint = "https://oss-cn-beijing.aliyuncs.com";
    //阿里云主账号AccessKey的访问权限
    public static String accessKeyId = "";
    public static String accessKeySecret = "";
    //存储空间
    public static String bucketName = "";

    /**
     * 上传文件
     * @param content
     * @param objectName
     * @throws IOException
     */
    public static void uploadALiYun(MultipartFile content, String objectName) throws IOException {

        //创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        //上传内容到指定的存储空间(bucketName),并保存为指定的文件名称(objectName)
        ossClient.putObject(bucketName,objectName,new ByteArrayInputStream(content.getBytes()));

        //关闭OSSClient
        ossClient.shutdown();
    }

    /**
     * 删除文件
     */

    public static void deleteFile(String objectName){
        //创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);

        //删除文件
        ossClient.deleteObject(bucketName,objectName);

        //关闭OSSClient
        ossClient.shutdown();
    }

}
