package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.util.SMSUtils;
import com.itheima.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {


    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送验证码
     * @return
     */
    @RequestMapping("/sendOrderMessage")
    public Result sendShortMessage(String telephone) throws ClientException {
        //随机生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);

        try {
            //发送验证码
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
            //发送成功,存到redis中
            //定时存储时间,会过期
            //防止重复,键为手机号+存储信息,时间5分钟,以及发送的验证码
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER,60*60,code.toString());
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            //发送错误
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }


    /**
     * 登录验证码
     */

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) throws ClientException {
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,String.valueOf(validateCode));
            //登录的验证码
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN,60*60,validateCode.toString());
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

}
