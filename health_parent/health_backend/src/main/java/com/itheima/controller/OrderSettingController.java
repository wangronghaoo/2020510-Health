package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.util.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result  upload(@RequestParam("excelFile") MultipartFile excelFile){
        //使用工具类读取Excel文件
        try {
            List<String[]> strings = POIUtils.readExcel(excelFile);
            //将内容封装为预约设置
            List<OrderSetting> orderSettings = new ArrayList<OrderSetting>();
            //遍历文件中的数据
            for (String[] string : strings) {
                Date date = new Date(string[0]);//数据为日期
                String number = string[1];      // number
                //将数据封装到实体类中
                OrderSetting orderSetting = new OrderSetting(date,Integer.parseInt(number));
                //将得到的每一个实体类添加到集合当中,调用Service添加到数据库
                orderSettings.add(orderSetting);
                orderSettingService.add(orderSettings);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }


    @RequestMapping("/findOrdersetting")
    public Result findOrdersetting(String date){   //2019/6

        try {
            List<Map> list = orderSettingService.findOrdersetting(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }


    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        System.out.println(orderSetting);
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }

    }
}
