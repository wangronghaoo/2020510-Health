package com.itheima.ServiceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.UserMapper;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    public Integer findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public void updatePassword(Map<String, String> userParamMap) {
        userMapper.updatePassword(userParamMap);
    }
}
