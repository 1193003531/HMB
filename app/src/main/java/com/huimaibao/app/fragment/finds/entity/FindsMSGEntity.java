package com.huimaibao.app.fragment.finds.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ITEM的对应可序化队列属性
 */
public class FindsMSGEntity implements Serializable {

    public String FindsId;
    public String FindsUserId;
    public String FindsUserName;
    public String FindsUserHead;
    public String FindsContent;
    public String FindsImage;
    public String FindsTeaching;
    public String FindsTime;
    public String FindsCommentId;
    public String FindsNewMessageId;

    public String getFindsNewMessageId() {
        return FindsNewMessageId;
    }

    public void setFindsNewMessageId(String findsNewMessageId) {
        this.FindsNewMessageId = findsNewMessageId;
    }

    public String getFindsId() {
        return FindsId;
    }

    public void setFindsId(String findsId) {
        this.FindsId = findsId;
    }

    public String getFindsUserId() {
        return FindsUserId;
    }

    public void setFindsUserId(String findsUserId) {
        this.FindsUserId = findsUserId;
    }

    public String getFindsUserName() {
        return FindsUserName;
    }

    public void setFindsUserName(String findsUserName) {
        this.FindsUserName = findsUserName;
    }

    public String getFindsUserHead() {
        return FindsUserHead;
    }

    public void setFindsUserHead(String findsUserHead) {
        this.FindsUserHead = findsUserHead;
    }

    public String getFindsContent() {
        return FindsContent;
    }

    public void setFindsContent(String findsContent) {
        this.FindsContent = findsContent;
    }

    public String getFindsImage() {
        return FindsImage;
    }

    public void setFindsImage(String FindsImage) {
        this.FindsImage = FindsImage;
    }

    public String getFindsTeaching() {
        return FindsTeaching;
    }

    public void setFindsTeaching(String FindsTeaching) {
        this.FindsTeaching = FindsTeaching;
    }

    public String getFindsCommentId() {
        return FindsCommentId;
    }

    public void setFindsCommentId(String findsCommentId) {
        this.FindsCommentId = findsCommentId;
    }

    public String getFindsTime() {
        return FindsTime;
    }

    public void setFindsTime(String findsTime) {
        this.FindsTime = findsTime;
    }


}