package com.itheima.service;


import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] id);

    PageResult findPage(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupId(Integer id);

    void edit(Setmeal setmeal, Integer[] id);

    void delete(Integer id);

    List<Setmeal> findAll();

    Setmeal findCheckGroupAndCheckItemBySetMealId(Integer id);


    List<Map<String, Object>> findSetMealAndCount();

}
