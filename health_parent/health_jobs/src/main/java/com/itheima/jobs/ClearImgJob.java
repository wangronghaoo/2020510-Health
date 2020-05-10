package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.util.ALiYunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){

        //获取垃圾图片set集合
        //服务器中的与数据库中的
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEALPIC_IN_SERVICE, RedisConstant.SETMEALPIC_IN_DB);

        //遍历这些图片,删除
        if (set != null) {
            for (String fileName : set) {
                //调用阿里云工具类删除服务器中的垃圾图片
                ALiYunUtils.deleteFile(fileName);
                System.out.println(fileName);
                //删除redis集合中的垃圾图片
                jedisPool.getResource().srem(RedisConstant.SETMEALPIC_IN_SERVICE ,fileName);
            }
        }
    }

}
