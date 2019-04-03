package com.huimaibao.app.fragment.message.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class MessageEntity implements Serializable {

    public String MessageId;
    public String MessageName;


    public String MessageWechatName;
    public String MessageContent;
    public String MessageImage;
    public String MessageUrl;
    public String MessageTime;
    public String MessageMoney;
    public String MessageType;
    public String MessagePhone;

    public String getMessageWechatName() {
        return MessageWechatName;
    }

    public void setMessageWechatName(String messageWechatName) {
        this.MessageWechatName = messageWechatName;
    }

    public String getMessagePhone() {
        return MessagePhone;
    }

    public void setMessagePhone(String messagePhone) {
        this.MessagePhone = messagePhone;
    }


    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String MessageId) {
        this.MessageId = MessageId;
    }

    public String getMessageName() {
        return MessageName;
    }

    public void setMessageName(String MessageName) {
        this.MessageName = MessageName;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String MessageContent) {
        this.MessageContent = MessageContent;
    }

    public String getMessageImage() {
        return MessageImage;
    }

    public void setMessageImage(String MessageImage) {
        this.MessageImage = MessageImage;
    }

    public String getMessageUrl() {
        return MessageUrl;
    }

    public void setMessageUrl(String MessageUrl) {
        this.MessageUrl = MessageUrl;
    }

    public String getMessageTime() {
        return MessageTime;
    }

    public void setMessageTime(String MessageTime) {
        this.MessageTime = MessageTime;
    }

    public String getMessageMoney() {
        return MessageMoney;
    }

    public void setMessageMoney(String MessageMoney) {
        this.MessageMoney = MessageMoney;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String MessageType) {
        this.MessageType = MessageType;
    }


}