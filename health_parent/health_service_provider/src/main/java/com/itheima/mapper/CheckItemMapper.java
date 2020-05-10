package com.itheima.mapper;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemMapper {


    /**
     * 添加检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);


    Page<CheckItem> findCondition(String s);

    void delete(Integer id);

    CheckItem findById(Integer id);

    void update(CheckItem checkItem);

    List<CheckItem> findAll();
}
