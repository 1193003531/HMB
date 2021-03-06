package com.huimaibao.app.fragment.finds.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class FindsCommentEntity implements Serializable {

    public String FindsUserId;
    public String FindsUserName;
    public String FindsUserHead;
    public String FindsCommentId;
    public String FindsContent;
    public String FindsPraiseNum;
    public String FindsIsPraise;
    public String FindsTime;
    public String FindsToUserName;
    public String FindsType;
    public boolean FindsIsNewMsg;

    public boolean isFindsIsNewMsg() {
        return FindsIsNewMsg;
    }

    public void setFindsIsNewMsg(boolean findsIsNewMsg) {
        this.FindsIsNewMsg = findsIsNewMsg;
    }

    public String getFindsToUserName() {
        return FindsToUserName;
    }

    public void setFindsToUserName(String findsToUserName) {
        this.FindsToUserName = findsToUserName;
    }

    public String getFindsType() {
        return FindsType;
    }

    public void setFindsType(String findsType) {
        this.FindsType = findsType;
    }

    public String getFindsCommentId() {
        return FindsCommentId;
    }

    public void setFindsCommentId(String findsCommentId) {
        this.FindsCommentId = findsCommentId;
    }

    public String getFindsContent() {
        return FindsContent;
    }

    public void setFindsContent(String findsContent) {
        this.FindsContent = findsContent;
    }

    public String getFindsPraiseNum() {
        return FindsPraiseNum;
    }

    public void setFindsPraiseNum(String findsPraiseNum) {
        this.FindsPraiseNum = findsPraiseNum;
    }

    public String getFindsIsPraise() {
        return FindsIsPraise;
    }

    public void setFindsIsPraise(String findsIsPraise) {
        this.FindsIsPraise = findsIsPraise;
    }

    public String getFindsTime() {
        return FindsTime;
    }

    public void setFindsTime(String findsTime) {
        this.FindsTime = findsTime;
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


}