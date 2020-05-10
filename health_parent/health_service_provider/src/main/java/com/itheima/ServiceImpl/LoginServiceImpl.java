package com.itheima.ServiceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.LoginMapper;
import com.itheima.pojo.Member;
import com.itheima.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    public Member findMember(String telephone) {
        return loginMapper.findMember(telephone);
    }

    public void save(Member member) {
        loginMapper.save(member);
    }
}
