package com.huimaibao.app.fragment.mine.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class CollectionEntity implements Serializable {

    public String CollectionId;
    public String CollectionTitle;
    public String CollectionImage;
    public String CollectionImage1;
    public String CollectionImage2;
    public String CollectionImage3;
    public String CollectionBrowse;
    public String CollectionTime;
    public String CollectionName;

    public String CollectionType;
    public boolean CollectionIsCheck;
    public boolean CollectionIsShow;

    public boolean getCollectionIsCheck() {
        return CollectionIsCheck;
    }

    public void setCollectionIsCheck(boolean collectionIsCheck) {
        this.CollectionIsCheck = collectionIsCheck;
    }

    public boolean getCollectionIsShow() {
        return CollectionIsShow;
    }

    public void setCollectionIsShow(boolean collectionIsShow) {
        this.CollectionIsShow = collectionIsShow;
    }


    public String getCollectionImage() {
        return CollectionImage;
    }

    public void setCollectionImage(String collectionImage) {
        this.CollectionImage = collectionImage;
    }

    public String getCollectionImage1() {
        return CollectionImage1;
    }

    public void setCollectionImage1(String collectionImage1) {
        this.CollectionImage1 = collectionImage1;
    }

    public String getCollectionImage2() {
        return CollectionImage2;
    }

    public void setCollectionImage2(String collectionImage2) {
        this.CollectionImage2 = collectionImage2;
    }

    public String getCollectionImage3() {
        return CollectionImage3;
    }

    public void setCollectionImage3(String collectionImage3) {
        this.CollectionImage3 = collectionImage3;
    }

    public String getCollectionBrowse() {
        return CollectionBrowse;
    }

    public void setCollectionBrowse(String collectionBrowse) {
        this.CollectionBrowse = collectionBrowse;
    }


    public String getCollectionId() {
        return CollectionId;
    }

    public void setCollectionId(String CollectionId) {
        this.CollectionId = CollectionId;
    }

    public String getCollectionType() {
        return CollectionType;
    }

    public void setCollectionType(String CollectionType) {
        this.CollectionType = CollectionType;
    }

    public String getCollectionTitle() {
        return CollectionTitle;
    }

    public void setCollectionTitle(String CollectionTitle) {
        this.CollectionTitle = CollectionTitle;
    }

    public String getCollectionTime() {
        return CollectionTime;
    }

    public void setCollectionTime(String CollectionTime) {
        this.CollectionTime = CollectionTime;
    }

    public String getCollectionName() {
        return CollectionName;
    }

    public void setCollectionName(String CollectionName) {
        this.CollectionName = CollectionName;
    }
}