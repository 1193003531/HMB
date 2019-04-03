package com.huimaibao.app.fragment.library.bean;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class LibNewsEntity implements Serializable {

    public String LibNewsId;
    public String LibNewsTitle;
    public String LibNewsImage;
    public String LibNewsImageRight;
    public String LibNewsImage1;
    public String LibNewsImage2;
    public String LibNewsImage3;
    public String LibNewsBrowse;
    public String LibNewsTime;
    public String LibNewsName;
    public String LibNewsType;
    public String LibNewsUrl;//广告链接
    public String LibLibNewsNameImage;
    public String LibLibNewsState;

    public String getLibNewsUrl() {
        return LibNewsUrl;
    }

    public void setLibNewsUrl(String libNewsUrl) {
        this.LibNewsUrl = libNewsUrl;
    }

    public String getLibLibNewsState() {
        return LibLibNewsState;
    }

    public void setLibLibNewsState(String libLibNewsState) {
        this.LibLibNewsState = libLibNewsState;
    }


    public String getLibLibNewsNameImage() {
        return LibLibNewsNameImage;
    }

    public void setLibLibNewsNameImage(String libLibNewsNameImage) {
        this.LibLibNewsNameImage = libLibNewsNameImage;
    }

    public String getLibNewsImageRight() {
        return LibNewsImageRight;
    }

    public void setLibNewsImageRight(String LibNewsImageRight) {
        this.LibNewsImageRight = LibNewsImageRight;
    }


    public String getLibNewsImage() {
        return LibNewsImage;
    }

    public void setLibNewsImage(String LibNewsImage) {
        this.LibNewsImage = LibNewsImage;
    }

    public String getLibNewsImage1() {
        return LibNewsImage1;
    }

    public void setLibNewsImage1(String LibNewsImage1) {
        this.LibNewsImage1 = LibNewsImage1;
    }

    public String getLibNewsImage2() {
        return LibNewsImage2;
    }

    public void setLibNewsImage2(String LibNewsImage2) {
        this.LibNewsImage2 = LibNewsImage2;
    }

    public String getLibNewsImage3() {
        return LibNewsImage3;
    }

    public void setLibNewsImage3(String LibNewsImage3) {
        this.LibNewsImage3 = LibNewsImage3;
    }

    public String getLibNewsBrowse() {
        return LibNewsBrowse;
    }

    public void setLibNewsBrowse(String LibNewsBrowse) {
        this.LibNewsBrowse = LibNewsBrowse;
    }


    public String getLibNewsId() {
        return LibNewsId;
    }

    public void setLibNewsId(String LibNewsId) {
        this.LibNewsId = LibNewsId;
    }

    public String getLibNewsType() {
        return LibNewsType;
    }

    public void setLibNewsType(String LibNewsType) {
        this.LibNewsType = LibNewsType;
    }

    public String getLibNewsTitle() {
        return LibNewsTitle;
    }

    public void setLibNewsTitle(String LibNewsTitle) {
        this.LibNewsTitle = LibNewsTitle;
    }

    public String getLibNewsTime() {
        return LibNewsTime;
    }

    public void setLibNewsTime(String LibNewsTime) {
        this.LibNewsTime = LibNewsTime;
    }

    public String getLibNewsName() {
        return LibNewsName;
    }

    public void setLibNewsName(String LibNewsName) {
        this.LibNewsName = LibNewsName;
    }
}