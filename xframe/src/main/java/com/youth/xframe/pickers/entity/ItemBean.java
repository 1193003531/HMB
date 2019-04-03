package com.youth.xframe.pickers.entity;

/**
 *
 */
public abstract class ItemBean extends JavaBean {
    private String areaId;
    private String areaName;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Override
    public String toString() {
        return "areaId=" + areaId + ",areaName=" + areaName;
    }
}