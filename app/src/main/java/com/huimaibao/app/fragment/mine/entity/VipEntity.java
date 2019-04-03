package com.huimaibao.app.fragment.mine.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class VipEntity implements Serializable {

    public String VipId;
    public String VipName;
    public String VipDays;
    public String VipMoney;
    public String VipLastMoney;
    public String VipAutoCharge;
    public String VipDescription;
    public String VipMonths;
    public String VipType;
    public String VipIconPath;//行业

    public String getVipIconPath() {
        return VipIconPath;
    }

    public void setVipIconPath(String VipIconPath) {
        this.VipIconPath = VipIconPath;
    }


    public String getVipDescription() {
        return VipDescription;
    }

    public void setVipDescription(String VipDescription) {
        this.VipDescription = VipDescription;
    }


    public String getVipMonths() {
        return VipMonths;
    }

    public void setVipMonths(String VipMonths) {
        this.VipMonths = VipMonths;
    }

    public String getVipType() {
        return VipType;
    }

    public void setVipType(String VipType) {
        this.VipType = VipType;
    }

    public String getVipAutoCharge() {
        return VipAutoCharge;
    }

    public void setVipAutoCharge(String VipAutoCharge) {
        this.VipAutoCharge = VipAutoCharge;
    }

    public String getVipId() {
        return VipId;
    }

    public void setVipId(String VipId) {
        this.VipId = VipId;
    }

    public String getVipDays() {
        return VipDays;
    }

    public void setVipDays(String VipDays) {
        this.VipDays = VipDays;
    }

    public String getVipMoney() {
        return VipMoney;
    }

    public void setVipMoney(String VipMoney) {
        this.VipMoney = VipMoney;
    }

    public String getVipLastMoney() {
        return VipLastMoney;
    }

    public void setVipLastMoney(String VipLastMoney) {
        this.VipLastMoney = VipLastMoney;
    }

    public String getVipName() {
        return VipName;
    }

    public void setVipName(String VipName) {
        this.VipName = VipName;
    }
}