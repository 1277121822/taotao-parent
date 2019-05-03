package com.djb.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author djb
 * @create 2019-05-01 19:56
 * 显示页面
 */
@Controller
public class PageController {

    @RequestMapping("/")
    public String showIndex(){
        return "index";
    }

    //显示各个页面
    @RequestMapping("/{page}")
    public String showPage(@PathVariable("page") String page){
        return page;
    }

}
