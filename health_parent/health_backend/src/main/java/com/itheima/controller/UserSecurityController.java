package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserSecurityService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserSecurityController implements UserDetailsService {

    @Reference
    private UserSecurityService userSecurityService;

    /**
     * 根据username查询该用户的角色与权限
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = null;
        if (username != null && username.length() >0){
            //根据用户名查询数据库
             user = userSecurityService.findByUsername(username);
        }
        if (user == null){
            return null;
        }
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        //获取用户所拥有的角色
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //获取每一个角色的权限,添加集合
                list.add(new SimpleGrantedAuthority(role.getKeyword()));
            //得到每一个权限
            Set<Permission> rolePermissions = role.getPermissions();
            for (Permission rolePermission : rolePermissions) {
                list.add(new SimpleGrantedAuthority(rolePermission.getKeyword()));
            }
        }
        //new框架user对象
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);

    }



}
