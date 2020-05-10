package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.OrderList;

import java.util.Map;

public interface OrderInfoService {

    Result add(Map map) throws Exception;
    OrderList findOrderById(Integer orderId);
}
