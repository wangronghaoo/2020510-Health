package com.itheima.service;

import com.itheima.pojo.Member;

public interface LoginService {
    Member findMember(String telephone);

    void save(Member member);
}
