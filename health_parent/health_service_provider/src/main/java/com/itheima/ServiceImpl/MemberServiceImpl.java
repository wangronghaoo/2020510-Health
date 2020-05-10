package com.itheima.ServiceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.MemberMapper;
import com.itheima.mapper.OrderMapper;
import com.itheima.service.MemberService;
import com.itheima.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 根据日期查找会员数量
     *
     * @param months
     * @return
     */
    public List<Integer> findMemberCount(List<String> months) {

        List<Integer> list = new ArrayList<Integer>();
        //得到每一个月份
        for (String month : months) {
            month += ".31";  //格式 2019.01.31
            Integer memberCount = memberMapper.findMember(month);
            list.add(memberCount);
        }
        return list;
    }

    public Map<String, Object> getBussinessReportData() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        //today日期
        Date date = new Date();
        map.put("reportDate", DateUtils.parseDate2String(date));


        //todayNewMember 今日新增会员
        Integer newMemberCount = memberMapper.findTodayNewMember(new SimpleDateFormat("yyyy-MM-dd").format(date));
        map.put("todayNewMember", newMemberCount);


        //会员总数
        Integer totalMember = memberMapper.findTotalMember();
        map.put("totalMember", totalMember);


        //本周新增会员thisWeekNewMember
        //设置本周的第一天为周一
        //本周第一天
        Date firstDayOfWeek = DateUtils.getFirstDayOfWeek(date);
        String firstDay = DateUtils.parseDate2String(firstDayOfWeek);

        //本周最后一天
        Date lastDayOfWeek = DateUtils.getLastDayOfWeek(date);
        String lastDay = DateUtils.parseDate2String(lastDayOfWeek);
        Map map1 = new HashMap();
        map1.put("firstDay", firstDay);
        map1.put("lastDay", lastDay);
        Integer thisWeekNewMember = memberMapper.thisWeekNewMember(map1);
        map.put("thisWeekNewMember", thisWeekNewMember);


        //获取这个月第一天
        Date firstDay4ThisMonth = DateUtils.getFirstDay4ThisMonth();
        String firstDayOfThisMonth = DateUtils.parseDate2String(firstDay4ThisMonth);
        System.out.println(firstDayOfThisMonth);

        //获取这个月最后一天
        Date lastDayOfMonths = DateUtils.getLastDayOfMonths(firstDay4ThisMonth);
        String lastDayOfThisMonth = DateUtils.parseDate2String(lastDayOfMonths);
        System.out.println(lastDayOfThisMonth);
        Map map2 = new HashMap();
        map2.put("firstDayOfThisMonth", firstDayOfThisMonth);
        map2.put("lastDayOfThisMonth", lastDayOfThisMonth);
        Integer thisMonthNewMember = memberMapper.findThisMonthNewMember(map2);
        map.put("thisMonthNewMember",thisMonthNewMember);


        //今天预约数量
        Integer todayOrderNumber = orderMapper.findTodayOrderNumber(new SimpleDateFormat("yyyy-MM-dd").format(date));
        map.put("todayOrderNumber",todayOrderNumber);

        //今日到诊数
        Integer todayVisitsNumber = orderMapper.findTodayVisitsNumber(new SimpleDateFormat("yyyy-MM-dd").format(date));
        map.put("todayVisitsNumber",todayVisitsNumber);

        //本周预约人数
        //本周第一天
        Date first_Day = DateUtils.getFirstDayOfWeek(date);
        String firstDay_Week = DateUtils.parseDate2String(first_Day);

        //本周最后一天
        Date last_Day = DateUtils.getLastDayOfWeek(date);
        String lastDay_Week = DateUtils.parseDate2String(last_Day);
        Map map3 = new HashMap();
        map3.put("first_Day", firstDay_Week);
        map3.put("last_Day", lastDay_Week);
        Integer thisWeekOrderNumber = orderMapper.thisWeekOrderNumber(map3);
        map.put("thisWeekOrderNumber", thisWeekOrderNumber);

        //本周到诊人数
        Integer thisWeekVisitsNumber = orderMapper.thisWeekVisitsNumber(map3);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);

        //thisMonthOrderNumber本月预约人数
        //获取这个月第一天
        Date firstDay4_ThisMonth = DateUtils.getFirstDay4ThisMonth();
        String firstDay_OfThisMonth = DateUtils.parseDate2String(firstDay4_ThisMonth);

        //获取这个月最后一天
        Date lastDayOf_Months = DateUtils.getLastDayOfMonths(firstDay4ThisMonth);
        String lastDay_OfThisMonth = DateUtils.parseDate2String(lastDayOf_Months);

        Map map4 = new HashMap();
        map4.put("firstDay_OfThisMonth", firstDay_OfThisMonth);
        map4.put("lastDay_OfThisMonth", lastDay_OfThisMonth);
        Integer thisMonthOrderNumber = orderMapper.thisMonthOrderNumber(map4);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);

        //thisMonthVisitsNumber
        Integer thisMonthVisitsNumber = orderMapper.thisMonthVisitsNumber(map4);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);


        //热榜
        /*
        * hotSetmeal :[
                        {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
                        {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
                    ]*/

        List<Map<String,Object>> hotSetmealList = orderMapper.hotSetmeal();
        map.put("hotSetmeal",hotSetmealList);

        return map;


    }

}
