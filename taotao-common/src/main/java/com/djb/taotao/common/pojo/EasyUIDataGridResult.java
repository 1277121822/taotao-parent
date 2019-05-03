package com.djb.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author djb
 * @create 2019-05-01 22:41
 * datagrid 展示数据的pojo 包括商品的pojo
 */
public class EasyUIDataGridResult implements Serializable {
    private Integer total;

    private List rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
