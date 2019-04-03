package com.huimaibao.app.fragment.mine.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class VeinTreasureEntity implements Serializable {

    public String VeinTreasureId;
    public String VeinTreasureType;
    public String VeinTreasureMoney;
    public String VeinTreasureTime;
    public String VeinTreasureName;
    public String VeinTreasureState;

    public String getVeinTreasureState() {
        return VeinTreasureState;
    }

    public void setVeinTreasureState(String veinTreasureState) {
        this.VeinTreasureState = veinTreasureState;
    }

    public String getVeinTreasureImage() {
        return VeinTreasureImage;
    }

    public void setVeinTreasureImage(String veinTreasureImage) {
        this.VeinTreasureImage = veinTreasureImage;
    }

    public String VeinTreasureImage;

    public String getVeinTreasureId() {
        return VeinTreasureId;
    }

    public void setVeinTreasureId(String VeinTreasureId) {
        this.VeinTreasureId = VeinTreasureId;
    }

    public String getVeinTreasureType() {
        return VeinTreasureType;
    }

    public void setVeinTreasureType(String VeinTreasureType) {
        this.VeinTreasureType = VeinTreasureType;
    }

    public String getVeinTreasureMoney() {
        return VeinTreasureMoney;
    }

    public void setVeinTreasureMoney(String VeinTreasureMoney) {
        this.VeinTreasureMoney = VeinTreasureMoney;
    }

    public String getVeinTreasureTime() {
        return VeinTreasureTime;
    }

    public void setVeinTreasureTime(String VeinTreasureTime) {
        this.VeinTreasureTime = VeinTreasureTime;
    }

    public String getVeinTreasureName() {
        return VeinTreasureName;
    }

    public void setVeinTreasureName(String VeinTreasureName) {
        this.VeinTreasureName = VeinTreasureName;
    }
}