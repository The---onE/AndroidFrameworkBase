package com.xmx.androidframeworkbase.common.choosephoto.entities;

import java.io.Serializable;

public class PhotoInf implements Serializable {
    private boolean select;
    private String path;

    public PhotoInf(String path) {
        select = false;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
