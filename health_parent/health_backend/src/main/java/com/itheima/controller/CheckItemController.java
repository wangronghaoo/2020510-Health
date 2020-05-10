package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkItem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;



    /**
     * 添加checkItem
     */
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        Result result = new Result();
        try {
            checkItemService.add(checkItem);
            result.setFlag(true);
            result.setMessage(MessageConstant.ADD_CHECKITEM_SUCCESS);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setMessage(MessageConstant.ADD_CHECKITEM_FAIL);
            return result;
        }
    }

    /**
     * 分页查询checkItem
     */
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

       return checkItemService.findPage(queryPageBean);
    }

    /**
     * 删除checkItem
     */
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            checkItemService.delete(id);
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result (false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }


    /**
     * 根据id查询checkItem
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false);
        }
    }

    /**
     * 修改检查项checkItem
     */

    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    @RequestMapping("/update")
    public Result update(@RequestBody CheckItem checkItem){
        try {
            checkItemService.update(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }


    /**
     * 查询所有检查项
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckItem> checkItems = checkItemService.findAll();
            return new Result(true,checkItems);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false);
        }
    }

    /**
     * 权限控制
     */

    @RequestMapping("/securityCheckItem")
    public Result securityCheckItem(){
//        id = 1 所有权限检查项
//        id = 2 只有查询和新增检查项
        Map<String,Boolean> checkItemSecurityMap = new HashMap<String, Boolean>();
        checkItemSecurityMap.put("edit",false);
        checkItemSecurityMap.put("create",false);
        checkItemSecurityMap.put("delete",false);
        //获取框架上下文对象，获取认证对象，获取user对象
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //根据user对象获取所有权限
        Collection<GrantedAuthority> principalAuthorities = principal.getAuthorities();
        for (GrantedAuthority principalAuthority : principalAuthorities) {
            //得到每一个权限名字
            String authority = principalAuthority.getAuthority();
            if ("CHECKITEM_EDIT".equals(authority)){
                checkItemSecurityMap.put("edit",true);
            }
            if ("CHECKITEM_ADD".equals(authority)){
                checkItemSecurityMap.put("create",true);
            }
            if ("CHECKITEM_DELETE".equals(authority)){
                checkItemSecurityMap.put("delete",true);
            }
        }
        return new Result(true,checkItemSecurityMap);
    }
}
