package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    void add(List<OrderSetting> orderSetting);

    List<Map> findOrdersetting(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
