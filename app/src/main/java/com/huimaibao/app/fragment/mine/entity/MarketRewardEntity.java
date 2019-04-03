package com.huimaibao.app.fragment.mine.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class MarketRewardEntity implements Serializable {

    public String MarketRewardId;
    public String MarketRewardType;


    public String MarketRewardIncomeType;
    public String MarketRewardDescribe;
    public String MarketRewardMoney;
    public String MarketRewardTime;
    public String MarketRewardName;
    public String MarketRewardUserId;

    public String getMarketRewardIncomeType() {
        return MarketRewardIncomeType;
    }

    public void setMarketRewardIncomeType(String marketRewardIncomeType) {
        this.MarketRewardIncomeType = marketRewardIncomeType;
    }

    public String getMarketRewardDescribe() {
        return MarketRewardDescribe;
    }

    public void setMarketRewardDescribe(String marketRewardDescribe) {
        this.MarketRewardDescribe = marketRewardDescribe;
    }

    public String getMarketRewardUserId() {
        return MarketRewardUserId;
    }

    public void setMarketRewardUserId(String marketRewardUserId) {
        this.MarketRewardUserId = marketRewardUserId;
    }


    public String getMarketRewardId() {
        return MarketRewardId;
    }

    public void setMarketRewardId(String marketRewardId) {
        this.MarketRewardId = marketRewardId;
    }

    public String getMarketRewardType() {
        return MarketRewardType;
    }

    public void setMarketRewardType(String marketRewardType) {
        this.MarketRewardType = marketRewardType;
    }

    public String getMarketRewardMoney() {
        return MarketRewardMoney;
    }

    public void setMarketRewardMoney(String marketRewardMoney) {
        this.MarketRewardMoney = marketRewardMoney;
    }

    public String getMarketRewardTime() {
        return MarketRewardTime;
    }

    public void setMarketRewardTime(String marketRewardTime) {
        this.MarketRewardTime = marketRewardTime;
    }

    public String getMarketRewardName() {
        return MarketRewardName;
    }

    public void setMarketRewardName(String marketRewardName) {
        this.MarketRewardName = marketRewardName;
    }
}