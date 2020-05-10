package com.itheima.service;

import com.itheima.pojo.User;

public interface UserSecurityService {
    User findByUsername(String username);
}
