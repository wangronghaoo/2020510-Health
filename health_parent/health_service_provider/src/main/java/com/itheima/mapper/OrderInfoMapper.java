package com.itheima.mapper;

import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderList;
import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.Map;

public interface OrderInfoMapper {

    /**
     *是否存在该日期
     * @param date
     * @return
     */
    OrderSetting findDate(Date date);

    /**
     * 查询当前是否为会员
     * @param telephone
     * @return
     */
    Member findMember(String telephone);

    /**
     * 是否重复预约
     * @param order
     * @return
     */
    Order isRepeatOrder(Order order);

    /**
     * 已经预约人数+1
     * @param map
     */
    void editReservationsNumber(Map map);

    /**
     * 保存会员
     * @param newMember
     */
    void save(Member newMember);

    /**
     * 保存预约信息
     * @param newOrder
     */
    void saveMapper(Order newOrder);

    OrderList findOrderById(Integer orderId);
}
