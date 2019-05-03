package com.djb.taotao.service.impl;

import com.djb.taotao.mapper.TestMapper;
import com.djb.taotao.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author djb
 * @create 2019-05-01 16:53
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper mapper;

    @Override
    public String queryNow() {
        //1.注入mapper
        //2.调用Mapper的方法返回
        return mapper.queryNow();
    }
}
