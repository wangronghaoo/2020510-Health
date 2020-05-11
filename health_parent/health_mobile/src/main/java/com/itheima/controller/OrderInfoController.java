package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderList;
import com.itheima.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import java.util.Map;

@RestController
@RequestMapping("/orderInfo")
public class OrderInfoController {

    @Reference
    private OrderInfoService orderInfoService;

    @Autowired
    private JedisPool jedisPool;

    //没有实体类,只能map进行接收,json格式
        /*idCard: "123456789123456"
        name: "ffff"
        orderDate: "2019-12-08"
        setmealId: "7"
        sex: "1"
        telephone: "15619981574"
        validateCode: "f467"*/

    /**
     * 提交预约
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
       //获取用户输入的验证码
        String validateCode = (String) map.get("validateCode");
        System.out.println(validateCode);
        //获取用户的手机号
        String telephone = (String) map.get("telephone");
        //获取redis中存储的验证码
        String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        System.out.println(redisCode);
        //redis中的验证码与用户输入的验证码一致
        if (validateCode != null &&redisCode!= null && validateCode.equals(redisCode)){
            //设置预约类型
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            map.put("orderStatus",Order.ORDERSTATUS_NO);
            Result result = null;
            try {
                result = orderInfoService.add(map);
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            System.out.println(result.getData() + result.getMessage());
            return result;
        }else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }


    @RequestMapping("/findByOrderId")
    public Result findByOrderId(Integer orderId){

        //获取体检信息的ID
        try {
            //发送前台所要的信息,实体类或者Map
            OrderList orderList = orderInfoService.findOrderById(orderId);
            return new Result(true,orderList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false);
        }
    }

}
