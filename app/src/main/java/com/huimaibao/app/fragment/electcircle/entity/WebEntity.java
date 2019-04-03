package com.huimaibao.app.fragment.electcircle.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class WebEntity implements Serializable {

    public String WebId;
    public String WebImage;
    public String WebTitle;
    public String WebBrowse;
    public String WebShare;
    public String WebTime;
    public boolean WebIsCheck;

    public String getWebId() {
        return WebId;
    }

    public void setWebId(String webId) {
        this.WebId = webId;
    }

    public String getWebImage() {
        return WebImage;
    }

    public void setWebImage(String webImage) {
        this.WebImage = webImage;
    }

    public String getWebTitle() {
        return WebTitle;
    }

    public void setWebTitle(String webTitle) {
        this.WebTitle = webTitle;
    }

    public String getWebBrowse() {
        return WebBrowse;
    }

    public void setWebBrowse(String webBrowse) {
        this.WebBrowse = webBrowse;
    }

    public String getWebShare() {
        return WebShare;
    }

    public void setWebShare(String webShare) {
        this.WebShare = webShare;
    }

    public String getWebTime() {
        return WebTime;
    }

    public void setWebTime(String webTime) {
        this.WebTime = webTime;
    }

    public boolean isWebIsCheck() {
        return WebIsCheck;
    }

    public void setWebIsCheck(boolean webIsCheck) {
        this.WebIsCheck = webIsCheck;
    }


}