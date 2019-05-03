package com.djb.taotao.service.impl;

import com.djb.taotao.common.pojo.EasyUIDataGridResult;
import com.djb.taotao.mapper.TbItemMapper;
import com.djb.taotao.pojo.TbItem;
import com.djb.taotao.pojo.TbItemExample;
import com.djb.taotao.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author djb
 * @create 2019-05-01 22:53
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public EasyUIDataGridResult getItemList(Integer currentpage, Integer rows) {
        //1.设置分页信息 使用pagehelper
        if (currentpage == null) currentpage = 1;
        if (rows == null) rows = 1;
        PageHelper.startPage(currentpage,rows);
        //2.注入mapper
        //3.创建example 对象 不需要设置查询条件
        TbItemExample example = new TbItemExample();
        //4.根据mapper调用所有数据的方法
        List<TbItem> list = itemMapper.selectByExample(example);
        //5.获取分页的信息
        PageInfo<TbItem> info = new PageInfo<>(list);
        //6.封装到EasyUIDataGridResult
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal((int) info.getTotal());
        result.setRows(info.getList());
        //7.返回
        return result;
    }
}
