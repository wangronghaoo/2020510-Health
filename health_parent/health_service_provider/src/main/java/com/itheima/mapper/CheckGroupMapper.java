package com.itheima.mapper;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;


public interface CheckGroupMapper {


    void addCheckGroup(CheckGroup checkGroup);

    Page<CheckGroup> findCondition(String strings);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemById(Integer id);

    void deleteCheckItem_id(Integer id);

    void editCheckGroup(CheckGroup checkGroup);

    void update(Map<String, Integer> map);

    void delete(Integer id);

    List<CheckGroup> findAll();

}
