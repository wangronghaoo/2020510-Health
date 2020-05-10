package com.itheima.mapper;

import java.util.List;
import java.util.Map;

public interface OrderMapper {

    Integer findTodayOrderNumber(String date);

    Integer findTodayVisitsNumber(String format);

    Integer thisWeekOrderNumber(Map map3);

    Integer thisWeekVisitsNumber(Map map3);

    Integer thisMonthOrderNumber(Map map4);

    Integer thisMonthVisitsNumber(Map map4);

    List<Map<String, Object>> hotSetmeal();
}
