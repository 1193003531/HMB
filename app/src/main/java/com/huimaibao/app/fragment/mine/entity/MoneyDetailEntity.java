package com.huimaibao.app.fragment.mine.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class MoneyDetailEntity implements Serializable {

    public String MoneyDetailId;
    public String MoneyDetailName;
    public String MoneyDetailType;//0支出1收入
    public String MoneyDetailMoney;
    public String MoneyDetailTime;
    public String MoneyDetailCate;//分类

    public String getMoneyDetailCate() {
        return MoneyDetailCate;
    }

    public void setMoneyDetailCate(String moneyDetailCate) {
        this.MoneyDetailCate = moneyDetailCate;
    }


    public String getMoneyDetailTime() {
        return MoneyDetailTime;
    }

    public void setMoneyDetailTime(String moneyDetailTime) {
        this.MoneyDetailTime = moneyDetailTime;
    }


    public String getMoneyDetailType() {
        return MoneyDetailType;
    }

    public void setMoneyDetailType(String MoneyDetailType) {
        this.MoneyDetailType = MoneyDetailType;
    }


    public String getMoneyDetailMoney() {
        return MoneyDetailMoney;
    }

    public void setMoneyDetailMoney(String MoneyDetailMoney) {
        this.MoneyDetailMoney = MoneyDetailMoney;
    }


    public String getMoneyDetailId() {
        return MoneyDetailId;
    }

    public void setMoneyDetailId(String MoneyDetailId) {
        this.MoneyDetailId = MoneyDetailId;
    }


    public String getMoneyDetailName() {
        return MoneyDetailName;
    }

    public void setMoneyDetailName(String MoneyDetailName) {
        this.MoneyDetailName = MoneyDetailName;
    }
}