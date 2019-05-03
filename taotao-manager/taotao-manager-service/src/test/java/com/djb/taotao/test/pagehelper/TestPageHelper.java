package com.djb.taotao.test.pagehelper;

import com.djb.taotao.mapper.TbItemMapper;
import com.djb.taotao.pojo.TbItem;
import com.djb.taotao.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author djb
 * @create 2019-05-01 20:46
 */
public class TestPageHelper {

    @Test
    public void testhelper(){
        //1.初始化spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        //2.获取Mapper的代理对象
        TbItemMapper itemMapper = context.getBean(TbItemMapper.class);
        //3.设置分页信息
        PageHelper.startPage(1,3);//(页面，每页显示条数)
        TbItemExample example = new TbItemExample();
        //4.调用Mapper的方法查询数据
        List<TbItem> list = itemMapper.selectByExample(example);
        List<TbItem> list2 = itemMapper.selectByExample(example);
        //取分页信息
        PageInfo<TbItem> info = new PageInfo<>(list);

        System.out.println("第一个的长度："+list.size());
        System.out.println("第二个的长度："+list2.size());

        System.out.println("总记录数："+info.getTotal());

        //5.遍历结果集，打印结果
        for (TbItem tbItem : list) {
            System.out.println(tbItem.getId()+">>>>"+tbItem.getTitle());
        }

    }
}
