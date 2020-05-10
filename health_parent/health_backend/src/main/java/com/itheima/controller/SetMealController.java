package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.util.ALiYunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;
    /**
     * 接收上传文件,预览
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        try {
            //获取原始文件名称
            String originalFilename = imgFile.getOriginalFilename();
            //获取最后一个逗号的所在索引
            int index = originalFilename.lastIndexOf(".");
            //分割,得到文件后缀
            String s = originalFilename.substring(index);
            //使用UUID随机生成文件名,防止文件重名  xxx.jpg
            String fileName = UUID.randomUUID().toString() + s;
            ALiYunUtils.uploadALiYun(imgFile,fileName);
            //图片上传成功
             Result result = new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS ,fileName);
             //将图片存储到redis中的服务器集合中
            jedisPool.getResource().sadd(RedisConstant.SETMEALPIC_IN_SERVICE ,fileName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }


    /**
     * 添加套餐
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal , Integer[] id){
        try {
            setmealService.add(setmeal,id);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    /**
     * 分页查询
     */

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

            return setmealService.findPage(queryPageBean);
    }


    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false);
        }

    }

    /**
     * 查询套餐所对应的检查组id
     * @param id
     * @return
     */
    @RequestMapping("/findCheckGroupId")
    public Result findCheckGroupId(Integer id){
        try {
            List<Integer> ids = setmealService.findCheckGroupId(id);
            return new Result(true,ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false);
        }
    }

    /**
     * 编辑套餐
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal , Integer[] id){
        try{
            setmealService.edit(setmeal,id);
            return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }

    /**
     * 删除套餐
     */

    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            setmealService.delete(id);
            return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }

}
