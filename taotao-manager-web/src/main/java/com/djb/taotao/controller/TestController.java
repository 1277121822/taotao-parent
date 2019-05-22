package com.djb.taotao.controller;

import com.djb.taotao.pojo.TbItem;
import com.djb.taotao.pojo.TbItemDesc;
import com.djb.taotao.manager.service.ItemService;
import com.djb.taotao.manager.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author djb
 * @create 2019-05-01 17:00
 * 测试controller 查询当前的时间
 */
@Controller
public class TestController {
    @Autowired
    private TestService testService;
    @Autowired
    private ItemService itemService;

    @RequestMapping("/test/queryNow")
    @ResponseBody
    public String queryNow(){
        //1.引入服务
        //2、注入服务
        //3、调用服务
        return testService.queryNow();
    }
    @RequestMapping("/get")
    @ResponseBody
    public TbItemDesc get(){
        TbItem item = itemService.getItemById(101434521126763l);
        TbItemDesc itemDesc = itemService.getItemDescById(101434521126763l);
        return itemDesc;
    }

}
