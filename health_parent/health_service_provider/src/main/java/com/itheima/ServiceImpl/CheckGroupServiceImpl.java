package com.itheima.ServiceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.mapper.CheckGroupMapper;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupMapper checkGroupMapper;


    public void add(CheckGroup checkGroup, Integer[] ids) {

        //添加检查组
        checkGroupMapper.addCheckGroup(checkGroup);


        for (Integer integer : ids) {
            Map<String,Integer> map = new HashMap<String, Integer>();
            map.put("checkgroup_id",checkGroup.getId());
            map.put("checkitem_id",integer);
            checkGroupMapper.update(map);
        }


    }
    //分页查询
    public PageResult findPage(QueryPageBean queryPageBean) {


        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        Page<CheckGroup> condition = checkGroupMapper.findCondition(queryPageBean.getQueryString());
        return new PageResult(condition.getTotal(),condition.getResult());
    }

    public CheckGroup findById(Integer id) {

        return checkGroupMapper.findById(id);
    }

    public List<Integer> findCheckItemById(Integer id) {
        return checkGroupMapper.findCheckItemById(id);
    }

    public void edit(CheckGroup checkGroup,Integer[] id) {
        //清空中间表的checkItemid
        checkGroupMapper.deleteCheckItem_id(checkGroup.getId());
        //修改
        for (Integer integer : id) {
            Map<String,Integer> map = new HashMap<String, Integer>();
            map.put("checkgroup_id",checkGroup.getId());
            map.put("checkitem_id",integer);
            checkGroupMapper.update(map);
        }

        //修改
        checkGroupMapper.editCheckGroup(checkGroup);
    }

    public void delete(Integer id) {
        checkGroupMapper.delete(id);
    }

    public List<CheckGroup> findAll() {
        return checkGroupMapper.findAll();
    }


}
