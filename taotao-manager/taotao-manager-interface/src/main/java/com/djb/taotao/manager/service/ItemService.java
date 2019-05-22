package com.djb.taotao.manager.service;

import com.djb.taotao.common.utils.TaotaoResult;
import com.djb.taotao.pojo.TbItem;
import com.djb.taotao.pojo.TbItemDesc;
import com.djb.taotao.common.pojo.EasyUIDataGridResult;

/**
 * @author djb
 * @create 2019-05-01 22:46
 */
public interface ItemService {
    /*根据每页的页码和每页的行数进行分页查询*/
    EasyUIDataGridResult getItemList(Integer currentpage,Integer rows);
    //添加商品和描述
    TaotaoResult addItem(TbItem item, String desc);

    /**
     * 根据商品的id查询商品的数据
     * @param itemId
     * @return
     */
    public TbItem getItemById(Long itemId);

    //根据商品的id查询商品的描述
    public TbItemDesc getItemDescById(Long itemId);

}
