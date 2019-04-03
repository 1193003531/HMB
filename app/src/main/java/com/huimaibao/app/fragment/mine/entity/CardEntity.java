package com.huimaibao.app.fragment.mine.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class CardEntity implements Serializable {

    public String CardId;
    public String CardName;
    public String CardJobs;
    public String CardAddr;
    public String CardTime;
    public String CardImage;
    public String CardType;
    public String CardHead;
    public String CardState;
    public String CardCompany;
    public String CardIndustry;//行业
    public String CardPhone;
    public String CardWechat;
    public String CardIsVip;
    public String CardStyle;
    public boolean CardIsCheck;
    public boolean CardIsShow;

    public boolean getCardIsCheck() {
        return CardIsCheck;
    }

    public void setCardIsCheck(boolean cardIsCheck) {
        this.CardIsCheck = cardIsCheck;
    }

    public boolean getCardIsShow() {
        return CardIsShow;
    }

    public void setCardIsShow(boolean cardIsShow) {
        this.CardIsShow = cardIsShow;
    }


    public String getCardStyle() {
        return CardStyle;
    }

    public void setCardStyle(String cardStyle) {
        this.CardStyle = cardStyle;
    }


    public String getCardIsVip() {
        return CardIsVip;
    }

    public void setCardIsVip(String cardIsVip) {
        this.CardIsVip = cardIsVip;
    }


    public String getCardPhone() {
        return CardPhone;
    }

    public void setCardPhone(String cardPhone) {
        this.CardPhone = cardPhone;
    }

    public String getCardWechat() {
        return CardWechat;
    }

    public void setCardWechat(String cardWechat) {
        this.CardWechat = cardWechat;
    }


    public String getCardCompany() {
        return CardCompany;
    }

    public void setCardCompany(String cardCompany) {
        this.CardCompany = cardCompany;
    }


    public String getCardIndustry() {
        return CardIndustry;
    }

    public void setCardIndustry(String cardIndustry) {
        this.CardIndustry = cardIndustry;
    }


    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        this.CardType = cardType;
    }


    public String getCardHead() {
        return CardHead;
    }

    public void setCardHead(String cardHead) {
        this.CardHead = cardHead;
    }

    public String getCardState() {
        return CardState;
    }

    public void setCardState(String CardState) {
        this.CardState = CardState;
    }

    public String getCardImage() {
        return CardImage;
    }

    public void setCardImage(String CardImage) {
        this.CardImage = CardImage;
    }

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String CardId) {
        this.CardId = CardId;
    }

    public String getCardJobs() {
        return CardJobs;
    }

    public void setCardJobs(String CardJobs) {
        this.CardJobs = CardJobs;
    }

    public String getCardAddr() {
        return CardAddr;
    }

    public void setCardAddr(String CardAddr) {
        this.CardAddr = CardAddr;
    }

    public String getCardTime() {
        return CardTime;
    }

    public void setCardTime(String CardTime) {
        this.CardTime = CardTime;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String CardName) {
        this.CardName = CardName;
    }
}