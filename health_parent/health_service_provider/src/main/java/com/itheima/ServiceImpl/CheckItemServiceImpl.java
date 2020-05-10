package com.itheima.ServiceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.mapper.CheckItemMapper;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemMapper checkItemMapper;


    /**
     * 添加检查项
     * @param checkItem
     */
    public void add(CheckItem checkItem) {
        checkItemMapper.add(checkItem);
    }



    public PageResult findPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckItem> checkItems = checkItemMapper.findCondition(queryPageBean.getQueryString());
        return new PageResult(checkItems.getTotal(),checkItems.getResult());
    }

    public void delete(Integer id) {
        checkItemMapper.delete(id);
    }

    /**
     * 根据id回显数据
     * @param id
     * @return
     */
    public CheckItem findById(Integer id) {
        return checkItemMapper.findById(id);
    }

    public void update(CheckItem checkItem) {
        checkItemMapper.update(checkItem);
    }

    public List<CheckItem> findAll() {
        return checkItemMapper.findAll();
    }


}
