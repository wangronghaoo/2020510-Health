package com.itheima.mapper;

import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.Set;

public interface UserSecurityMapper {
    User findByUsername(String username);


    Set<Role> findRoleById(Integer userId);

    Set<Permission> findPermission(Integer roleId);
}
