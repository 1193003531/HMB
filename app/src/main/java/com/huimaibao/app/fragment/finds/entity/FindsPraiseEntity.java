package com.huimaibao.app.fragment.finds.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class FindsPraiseEntity implements Serializable {

    public String FindsUserId;
    public String FindsUserName;
    public String FindsUserHead;


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