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
    public String BankCardLogoT;//透明
    public String BankCardLogoM;//彩色
    public String BankCardLogoW;//白色
    public boolean BankCardCheck;
    public int BankCardSColor;
    public int BankCardEColor;

    public String getBankCardLogoT() {
        return BankCardLogoT;
    }

    public void setBankCardLogoT(String bankCardLogoT) {
        this.BankCardLogoT = bankCardLogoT;
    }

    public String getBankCardLogoM() {
        return BankCardLogoM;
    }

    public void setBankCardLogoM(String bankCardLogoM) {
        this.BankCardLogoM = bankCardLogoM;
    }

    public String getBankCardLogoW() {
        return BankCardLogoW;
    }

    public void setBankCardLogoW(String bankCardLogoW) {
        this. BankCardLogoW = bankCardLogoW;
    }


    public int getBankCardSColor() {
        return BankCardSColor;
    }

    public void setBankCardSColor(int bankCardSColor) {
        this.BankCardSColor = bankCardSColor;
    }

    public int getBankCardEColor() {
        return BankCardEColor;
    }

    public void setBankCardEColor(int bankCardEColor) {
        this.BankCardEColor = bankCardEColor;
    }


    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        this.BankName = bankName;
    }



    public boolean isBankCardCheck() {
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