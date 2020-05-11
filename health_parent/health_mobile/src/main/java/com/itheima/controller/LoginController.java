package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    private LoginService loginService;

    @Autowired
    private JedisPool jedisPool;


    @RequestMapping("/check")
    public Result login(@RequestBody Map map , HttpServletResponse response) {
        //用户手机号
        String telephone = (String) map.get("telephone");
        //验证码
        String validateCode = (String) map.get("validateCode");
        //redis中的验证码,该方法有销毁时间
        //1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
        String redisValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (validateCode == null || !validateCode.equals(redisValidateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
        Member member = loginService.findMember(telephone);
        if (member == null){
            //给会员设置信息
            member = new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            //完成会员注册
            loginService.save(member);
        }

        //向客户端写入Cookie，内容为用户手机号
        Cookie cookie = new Cookie("member",telephone);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
        //将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
        //将对象转为json字符串
        String memberJson = JSON.toJSON(member).toString();
        jedisPool.getResource().setex(telephone,60 * 30,memberJson);
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
