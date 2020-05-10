package com.itheima.ServiceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.mapper.OrderInfoMapper;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderList;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderInfoService;
import com.itheima.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = OrderInfoService.class)
@Transactional
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    public Result add(Map map) throws Exception {

        //查询该日期是否可以预约
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting = orderInfoMapper.findDate(DateUtils.parseString2Date(orderDate));
        //该日期没有查询到
        if (orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //查询该日期的预约人数是否已满
        //获取可预约
        int settingNumber = orderSetting.getNumber();
        //获取已经预约
        int reservations = orderSetting.getReservations();
        if (reservations >= settingNumber){
            //已满
            return new Result(false,MessageConstant.ORDER_FULL);
        }

       //查询该用户是否为会员,根据手机号进行查询
        String telephone = (String) map.get("telephone");
        Member member = orderInfoMapper.findMember(telephone);
        Map addNumberMap = new HashMap();
        //如果有当前会员,则查询是否预约为同一套餐
        //获取套餐id
        String setmealId = (String) map.get("setmealId");
        if (member != null){
            //会员id,日期,套餐map
            Integer memberId = member.getId();
            Date date = DateUtils.parseString2Date(orderDate);
            int setmeal_id = Integer.parseInt(setmealId);
            //根据日期,会员,以及套餐查询是否为重复预约
            Order isRepeatOrder = new Order(memberId,date,setmeal_id);
            Order isOrderRepeat = orderInfoMapper.isRepeatOrder(isRepeatOrder);
            //如果存在则已经预约该套餐
            if (isOrderRepeat!= null) {
                //存在,重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        //如果没有,则自动注册成为会员
        if (member == null){
            member = new Member();
            String name = (String) map.get("name");
            String sex = (String) map.get("sex");
            String idCard = (String) map.get("idCard");

            //给会员设置信息
            member.setName(name);
            member.setSex(sex);
            member.setIdCard(idCard);
            member.setPhoneNumber(telephone);
            member.setRegTime(DateUtils.parseString2Date(orderDate));
            //调用mapper保存信息,将新添加的会员id返回
            orderInfoMapper.save(member);
        }

        //获取预约类型
        String orderType = (String) map.get("orderType");
        String orderStatus = (String) map.get("orderStatus");
        //保存信息列表
        Order newOrder = new Order(member.getId(),DateUtils.parseString2Date(orderDate),orderType,orderStatus,Integer.parseInt(setmealId));

        orderInfoMapper.saveMapper(newOrder);
        addNumberMap.put("reservations",orderSetting.getReservations() + 1);
        addNumberMap.put("orderDate",DateUtils.parseString2Date(orderDate));
        orderInfoMapper.editReservationsNumber(addNumberMap);

        Result result = new Result();
        result.setFlag(true);
        result.setMessage(MessageConstant.ORDER_SUCCESS);
        result.setData(newOrder.getId());
        System.out.println(result.getData() + result.getMessage());
        return result;


    }

    /**
     * 根据体检信息ID查询用户信息
     * @param orderId
     * @return
     */
    public OrderList findOrderById(Integer orderId) {

        OrderList orderById = orderInfoMapper.findOrderById(orderId);
        System.out.println(orderById.getMember() + orderById.getSetmeal());
        return orderById;
    }

}
