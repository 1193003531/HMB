package com.huimaibao.app.fragment.finds.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ITEM的对应可序化队列属性
 */
public class FindsEntity implements Serializable {

    public String FindsId;
    public String FindsUserId;
    public String FindsUserName;
    public String FindsUserHead;
    public String FindsContent;
    public String FindsPraiseNum;
    public String FindsCommentsNum;
    public String FindsIsPraise;
    public String FindsIsFocus;
    public String FindsTime;
    public List<String> FindsImageList = new ArrayList<>();


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

    public String getFindsPraiseNum() {
        return FindsPraiseNum;
    }

    public void setFindsPraiseNum(String findsPraiseNum) {
        this.FindsPraiseNum = findsPraiseNum;
    }

    public String getFindsCommentsNum() {
        return FindsCommentsNum;
    }

    public void setFindsCommentsNum(String findsCommentsNum) {
        this.FindsCommentsNum = findsCommentsNum;
    }

    public String getFindsIsPraise() {
        return FindsIsPraise;
    }

    public void setFindsIsPraise(String findsIsPraise) {
        this.FindsIsPraise = findsIsPraise;
    }

    public String getFindsIsFocus() {
        return FindsIsFocus;
    }

    public void setFindsIsFocus(String findsIsFocus) {
        this.FindsIsFocus = findsIsFocus;
    }

    public String getFindsTime() {
        return FindsTime;
    }

    public void setFindsTime(String findsTime) {
        this.FindsTime = findsTime;
    }

    public List<String> getFindsImageList() {
        return FindsImageList;
    }

    public void setFindsImageList(List<String> findsImageList) {
        this.FindsImageList = findsImageList;
    }


}