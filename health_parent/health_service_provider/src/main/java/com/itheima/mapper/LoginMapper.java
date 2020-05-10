package com.itheima.mapper;

import com.itheima.pojo.Member;

public interface LoginMapper {

    Member findMember(String telephone);

    void save(Member member);
}
