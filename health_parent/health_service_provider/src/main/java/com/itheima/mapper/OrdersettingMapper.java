package com.itheima.mapper;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrdersettingMapper {


    Long isSave(Date date);

    void editNumber(OrderSetting orderSetting);

    void save(OrderSetting orderSetting);

    List<OrderSetting> findOrdersetting(Map map);
}
