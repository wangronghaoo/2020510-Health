package com.itheima.ServiceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.UserSecurityMapper;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Service
public class UserSecurityServiceImpl implements UserSecurityService {

    @Autowired
    private UserSecurityMapper userSecurityMapper;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    public User findByUsername(String username) {

        //得到用户名所对应的用户信息
        User user =  userSecurityMapper.findByUsername(username);

        //得到用户id
        Integer userId = user.getId();
        //根据用户id,查询所对应的角色
        Set<Role> roleList = userSecurityMapper.findRoleById(userId);
        for (Role role : roleList) {
            user.setRoles(roleList);
            //得到角色的id
            Integer roleId = role.getId();
            //根据角色id获取所对应的所有权限
            Set<Permission> permissionList = userSecurityMapper.findPermission(roleId);
            role.setPermissions(permissionList);
        }
        return user;
    }
}
