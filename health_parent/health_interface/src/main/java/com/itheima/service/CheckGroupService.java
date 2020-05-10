package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {


    void add(CheckGroup checkGroup, Integer[] ids);
    PageResult findPage(QueryPageBean queryPageBean);
    CheckGroup findById(Integer id);
    List<Integer> findCheckItemById(Integer id);

    void edit(CheckGroup checkGroup,Integer[] id);

    void delete(Integer id);

    List<CheckGroup> findAll();

}
