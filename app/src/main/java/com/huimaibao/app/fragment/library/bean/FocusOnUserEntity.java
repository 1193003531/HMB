package com.huimaibao.app.fragment.library.bean;

import java.io.Serializable;

/**
 * 关注用户实体类
 */
public class FocusOnUserEntity implements Serializable {
    public String FocusOnUserId;
    public String FocusOnUserName;
    public String FocusOnUserHead;

    public String getFocusOnUserId() {
        return FocusOnUserId;
    }

    public void setFocusOnUserId(String focusOnUserId) {
        this.FocusOnUserId = focusOnUserId;
    }

    public String getFocusOnUserName() {
        return FocusOnUserName;
    }

    public void setFocusOnUserName(String focusOnUserName) {
        this.FocusOnUserName = focusOnUserName;
    }

    public String getFocusOnUserHead() {
        return FocusOnUserHead;
    }

    public void setFocusOnUserHead(String focusOnUserHead) {
        this.FocusOnUserHead = focusOnUserHead;
    }


}
