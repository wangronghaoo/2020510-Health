package com.itheima.mapper;

import java.util.Map;

public interface UserMapper {
    Integer findByUsername(String username);

    void updatePassword(Map<String, String> userParamMap);
}
