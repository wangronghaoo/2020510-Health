package com.itheima.mapper;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealMapper {

    void add(Setmeal setmeal);

    void setMealIdAndCheckGroupId(Map<String, Integer> map);

    Page<Setmeal> findCondition(String queryString);

    Page<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupId(Integer id);

    void deleteCheckGroupIds(Integer id);

    void editById(Setmeal setmeal);

    void delete(Integer id);

    List<Setmeal> findAllSetmeal();

    Setmeal findCheckGroupAndCheckItemBySetMealId(Integer id);

    List<Map<String, Object>> findSetMealAndCount();
}
