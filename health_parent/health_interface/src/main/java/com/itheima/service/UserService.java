package com.itheima.service;

import java.util.Map;

public interface UserService {
    Integer findByUsername(String username);

    void updatePassword(Map<String, String> userParamMap);
}
