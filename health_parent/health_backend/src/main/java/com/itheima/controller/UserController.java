package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {


    @Reference
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    /**
     * 查询用户名
     *
     * @return
     */
    @RequestMapping("/getUsername")
    public Result getUsername() {

        //获取security上下文对象,获取认证对象以及user对象
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false);
        }

    }

    @RequestMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map map) {
        //查找用户名是否存在
        String username = (String) map.get("username");
        Integer count = userService.findByUsername(username);
        if (count == 0) {
            return new Result(false, MessageConstant.USERNAME_NOTFOUND);
        }
        //用户存在
        String newPassword = (String) map.get("newPassword");
        Map<String,String> userParamMap = new HashMap<String, String>();

        //使用BCrypt进行对密码加密
        String encodePassword = bCryptPasswordEncoder.encode(newPassword);
        System.out.println(encodePassword);
        userParamMap.put("username",username);
        userParamMap.put("password",encodePassword);
        try {
            userService.updatePassword(userParamMap);
            return new Result(true,MessageConstant.UPDATE_USERPASSWORD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.UPDATE_USERPASSWORD_FAIL);
        }
    }
}
