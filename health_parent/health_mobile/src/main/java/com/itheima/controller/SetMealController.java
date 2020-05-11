package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {

    @Reference
    private SetmealService setmealService;


    /**
     * 查询所有的套餐
     * @return
     */
    @RequestMapping("/findAllSetMeal")
    public Result findAllSetMeal(){
        try {
            List<Setmeal> setmealList = setmealService.findAll();
            return new Result(true,setmealList);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }

    }


    /**
     * 根据id查询套餐所及对应的检查组,和检查组所对应的检查项
     */

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setmealService.findCheckGroupAndCheckItemBySetMealId(id);
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }
}
