package com.itheima.ServiceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.mapper.SetmealMapper;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private JedisPool jedisPool;

    public void add(Setmeal setmeal, Integer[] id) {

        //添加数据
        setmealMapper.add(setmeal);

        //将图片添加到redis数据库集合中
        jedisPool.getResource().sadd(RedisConstant.SETMEALPIC_IN_DB,setmeal.getImg());

        //在关联表中添加数据
        setMealLdAndCheckGroupId(setmeal.getId(),id);

    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    public PageResult findPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        //如果为空,则查询全部
        if (queryPageBean.getQueryString() == null || queryPageBean.getQueryString().equals("")){
            Page<Setmeal> result = setmealMapper.findAll();
            return new PageResult(result.getTotal(),result.getResult());
        }else {
            //根据条件查询
            Page<Setmeal> setmeals =  setmealMapper.findCondition(queryPageBean.getQueryString());
            return new PageResult(setmeals.getTotal(),setmeals.getResult());
        }
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    public Setmeal findById(Integer id) {
        return setmealMapper.findById(id);
    }

    /**
     * 查询1套餐所对应的检查组id
     * @param id
     * @return
     */
    public List<Integer> findCheckGroupId(Integer id) {
        return setmealMapper.findCheckGroupId(id);
    }

    /**
     * 根据id修改套餐
     * @param setmeal
     * @param id
     */
    public void edit(Setmeal setmeal, Integer[] id) {
        //先清空相关联的数据,根据id

       setmealMapper.deleteCheckGroupIds(setmeal.getId());

        //添加关联的group
        setMealLdAndCheckGroupId(setmeal.getId(),id);

        //根据id修改对应的套餐

        setmealMapper.editById(setmeal);

    }

    /**
     * 根据id删除套餐
     * @param id
     */
    public void delete(Integer id) {
        setmealMapper.delete(id);
    }

    /**
     * 查询所有套餐数据
     * @return
     */
    public List<Setmeal> findAll() {
        return setmealMapper.findAllSetmeal();
    }

    /**
     * 根据套餐id查询对应的检查组和检查项id
     * @param id
     * @return
     */
    public Setmeal findCheckGroupAndCheckItemBySetMealId(Integer id) {
        return setmealMapper.findCheckGroupAndCheckItemBySetMealId(id);
    }

    /**
     * 查询套餐名字,以及预约人数
     * @return
     */
    public List<Map<String, Object>> findSetMealAndCount() {
        return setmealMapper.findSetMealAndCount();
    }


    public  void setMealLdAndCheckGroupId(Integer meal_id , Integer[] checkGroup_id){
        if (checkGroup_id != null && checkGroup_id.length > 0){
            for (Integer integer : checkGroup_id) {
                Map<String,Integer> map = new HashMap<String, Integer>();
                map.put("meal_id",meal_id);
                map.put("checkGroup_id",integer);
                setmealMapper.setMealIdAndCheckGroupId(map);
            }
        }
    }
}
