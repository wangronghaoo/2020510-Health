package com.itheima.service;

import java.util.List;
import java.util.Map;

public interface MemberService {
    List<Integer> findMemberCount(List<String> calendarList);


    Map<String, Object> getBussinessReportData() throws Exception;
}
