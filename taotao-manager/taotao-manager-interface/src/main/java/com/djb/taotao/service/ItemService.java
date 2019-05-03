package com.djb.taotao.service;

import com.djb.taotao.common.pojo.EasyUIDataGridResult;

/**
 * @author djb
 * @create 2019-05-01 22:46
 */
public interface ItemService {
    /*根据每页的页码和每页的行数进行分页查询*/
    public EasyUIDataGridResult getItemList(Integer currentpage,Integer rows);

}
