package com.huimaibao.app.fragment.mine.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class MDetailEntity implements Serializable {

    public String MDetailId;
    public String MDetailType;
    public String MDetailMoney;
    public String MDetailTime;
    public String MDetailName;

    public String getMDetailId() {
        return MDetailId;
    }

    public void setMDetailId(String MDetailId) {
        this.MDetailId = MDetailId;
    }

    public String getMDetailType() {
        return MDetailType;
    }

    public void setMDetailType(String MDetailType) {
        this.MDetailType = MDetailType;
    }

    public String getMDetailMoney() {
        return MDetailMoney;
    }

    public void setMDetailMoney(String MDetailMoney) {
        this.MDetailMoney = MDetailMoney;
    }

    public String getMDetailTime() {
        return MDetailTime;
    }

    public void setMDetailTime(String MDetailTime) {
        this.MDetailTime = MDetailTime;
    }

    public String getMDetailName() {
        return MDetailName;
    }

    public void setMDetailName(String MDetailName) {
        this.MDetailName = MDetailName;
    }
}