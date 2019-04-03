package com.huimaibao.app.fragment.electcircle.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class AreaEntity implements Serializable {

    public String AreaId;
    public String AreaName;
    public boolean AreaIsShow;

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String AreaId) {
        this.AreaId = AreaId;
    }

    public boolean getAreaIsShow() {
        return AreaIsShow;
    }

    public void setAreaIsShow(boolean AreaIsShow) {
        this.AreaIsShow = AreaIsShow;
    }


    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String AreaName) {
        this.AreaName = AreaName;
    }
}