package com.huimaibao.app.fragment.mine.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class IndustryEntity implements Serializable {

    public String IndustryId;
    public String IndustryName;
    public String IndustryData;
    public boolean IndustryIsShow;

    public String getIndustryId() {
        return IndustryId;
    }

    public void setIndustryId(String IndustryId) {
        this.IndustryId = IndustryId;
    }

    public String getIndustryData() {
        return IndustryData;
    }

    public void setIndustryData(String IndustryData) {
        this.IndustryData = IndustryData;
    }

    public boolean getIndustryIsShow() {
        return IndustryIsShow;
    }

    public void setIndustryIsShow(boolean IndustryIsShow) {
        this.IndustryIsShow = IndustryIsShow;
    }


    public String getIndustryName() {
        return IndustryName;
    }

    public void setIndustryName(String IndustryName) {
        this.IndustryName = IndustryName;
    }
}