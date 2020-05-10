package com.itheima.mapper;

import java.util.Map;

public interface MemberMapper {
    Integer findMember(String month);

    Integer findTodayNewMember(String date);

    Integer findTotalMember();

    Integer thisWeekNewMember(Map map1);

    Integer findThisMonthNewMember(Map map2);
}
