package com.djb.taotao.common.pojo;

import java.io.Serializable;

/**
 * @author djb
 * @create 2019-05-03 23:56
 */
public class EasyUITreeNode implements Serializable {

    private long id;
    private String text;
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
