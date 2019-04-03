package com.huimaibao.app.fragment.electcircle.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class ShareRecordEntity implements Serializable {

    public ShareTaskEntity ShareRecordOne;
    public ShareTaskEntity ShareRecordTwo;

    public ShareTaskEntity getShareRecordOne() {
        return ShareRecordOne;
    }

    public void setShareRecordOne(ShareTaskEntity shareRecordOne) {
        this.ShareRecordOne = shareRecordOne;
    }

    public ShareTaskEntity getShareRecordTwo() {
        return ShareRecordTwo;
    }

    public void setShareRecordTwo(ShareTaskEntity shareRecordTwo) {
        this.ShareRecordTwo = shareRecordTwo;
    }


}