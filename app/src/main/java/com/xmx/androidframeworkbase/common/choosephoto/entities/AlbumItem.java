package com.xmx.androidframeworkbase.common.choosephoto.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlbumItem implements Serializable {
    private String name;   //相册名字
    private int count; //数量

    private List<PhotoInf> bitList = new ArrayList<>();

    public AlbumItem() {
        super();
        count = 1;
    }

    public List<PhotoInf> getBitList() {
        return bitList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return String.valueOf(count);
    }

    public void increaseCount() {
        count++;
    }
}
