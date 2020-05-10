package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 添加检查组,将所对应id添加到所属组中
     */

    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup ,Integer[] id){
        try {
            checkGroupService.add(checkGroup,id);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 分页查询
     */
    @RequestMapping("/findPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean) {

        return checkGroupService.findPage(queryPageBean);

    }


    /**
     * 根据id查询检查组
     */

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false);
        }
    }


    @RequestMapping("/findCheckItemById")
    public Result findCheckItem(Integer id){
        try {
            List<Integer> checkItemList = checkGroupService.findCheckItemById(id);
            return new Result(true,checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false);
        }
    }


    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] id){
        try {
            checkGroupService.edit(checkGroup,id);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }


    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            checkGroupService.delete(id);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }


    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckGroup> groupList = checkGroupService.findAll();
            return new Result(true,groupList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false);
        }
    }
}
