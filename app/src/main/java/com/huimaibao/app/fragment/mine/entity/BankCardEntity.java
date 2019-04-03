package com.huimaibao.app.fragment.mine.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class BankCardEntity implements Serializable {

    public String BankCardId;
    public String BankName;
    public String BankCardName;
    public String BankCardType;
    public String BankCardNum;
    public boolean BankCardCheck;

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        this.BankName = bankName;
    }


    public boolean getBankCardCheck() {
        return BankCardCheck;
    }

    public void setBankCardCheck(boolean bankCardCheck) {
        this.BankCardCheck = bankCardCheck;
    }


    public String getBankCardType() {
        return BankCardType;
    }

    public void setBankCardType(String BankCardType) {
        this.BankCardType = BankCardType;
    }


    public String getBankCardNum() {
        return BankCardNum;
    }

    public void setBankCardNum(String BankCardNum) {
        this.BankCardNum = BankCardNum;
    }


    public String getBankCardId() {
        return BankCardId;
    }

    public void setBankCardId(String BankCardId) {
        this.BankCardId = BankCardId;
    }


    public String getBankCardName() {
        return BankCardName;
    }

    public void setBankCardName(String BankCardName) {
        this.BankCardName = BankCardName;
    }
}