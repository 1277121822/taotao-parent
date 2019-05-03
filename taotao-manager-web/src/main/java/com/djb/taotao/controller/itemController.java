package com.djb.taotao.controller;

import com.djb.taotao.common.pojo.EasyUIDataGridResult;
import com.djb.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author djb
 * @create 2019-05-01 23:06
 * 商品管理
 */
@Controller
public class itemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/item/list",method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page,Integer rows){
        //1.引入服务
        //2.注入服务
        //3.调用服务的方法
        EasyUIDataGridResult result = itemService.getItemList(page, rows);
        return result;
    }
}
