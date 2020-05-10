package com.itheima.ServiceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.OrdersettingMapper;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrdersettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrdersettingMapper ordersettingMapper;

    public void add(List<OrderSetting> orderSetting) {

        //1.直接添加数据
        //2.有可能已经设置,只能修改

        //判断数据库是否设置过该日期的预约数量
        if (orderSetting != null && orderSetting.size() > 0){
            for (OrderSetting setting : orderSetting) {
                //查询是否已经设置过
                Long count = ordersettingMapper.isSave(setting.getOrderDate());
                //如果大于1则已经设置,需要修改
                if (count > 0){
                    ordersettingMapper.editNumber(setting);
                }else {
                    //如果没有,则进行添加
                    ordersettingMapper.save(setting);
                }
            }
        }



    }

    public List<Map> findOrdersetting(String date) {


        //每一月的第一天开始-最后一天
        String dateBegin = date + "-1";    //2019-9-1
        String dateEnd = date + "-31";     //2019-9-31
        //将数据存放到map中,将map作为参数,查询数据库
        Map map = new HashMap();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        //查询数据库,返回list集合
        List<OrderSetting> orderSetting = ordersettingMapper.findOrdersetting(map);
        //将数据封装为map返回
         List<Map> list = new ArrayList<Map>();
        for (OrderSetting setting : orderSetting) {
            /*this.leftobj = [
                        { date: 1, number: 120, reservations: 1 },
                        { date: 3, number: 120, reservations: 1 },
                        { date: 4, number: 120, reservations: 120 },
                        { date: 6, number: 120, reservations: 1 },
                        { date: 8, number: 120, reservations: 1 }
                    ];*/

            Map data = new HashMap();
            data.put("date",setting.getOrderDate().getDate());  //获取的为某月的具体的几号
            data.put("number",setting.getNumber());             //可预约人数
            data.put("reservations",setting.getReservations());  //已经预约人数
            list.add(data);
            System.out.println(data);
        }
        System.out.println(list);
        return list;
    }

    public void editNumberByDate(OrderSetting orderSetting) {
        //是否有当前日期,如果有则修改,没有则添加
        Long date = ordersettingMapper.isSave(orderSetting.getOrderDate());

        if (date > 0 ){
            ordersettingMapper.editNumber(orderSetting);
        }else {
            ordersettingMapper.save(orderSetting);
        }
    }

}
