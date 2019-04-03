package com.huimaibao.app.fragment.electcircle.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class ShareTaskEntity implements Serializable {


    public int type;//0名片，1-文章，网页
    public String ShareTaskRecordId;
    public String ShareTaskPushId;
    public String ShareTaskId;
    public String ShareTaskUserId;
    public String ShareTaskHead;
    public String ShareTaskName;
    public String ShareTaskImage;
    public String ShareTaskTitle;
    public String ShareTaskTime;
    public String ShareTaskType;
    public int ShareTaskStatus;//1,文章，2个人网页，3营销网页， 4名片
    public String ShareTaskBrowse;
    public String ShareTaskForBrowse;
    public String ShareTaskJobs;
    public String ShareTaskStyle;
    public String ShareTaskCompany;
    public String ShareTaskWechat;
    public String ShareTaskPhone;
    public String ShareTaskAddr;
    public String ShareTaskMoto;
    public String ShareTaskIsShare;
    public String ShareTaskDate;

    public String getShareTaskDate() {
        return ShareTaskDate;
    }

    public void setShareTaskDate(String shareTaskDate) {
        this.ShareTaskDate = shareTaskDate;
    }


    public String getShareTaskForBrowse() {
        return ShareTaskForBrowse;
    }

    public void setShareTaskForBrowse(String shareTaskForBrowse) {
        ShareTaskForBrowse = shareTaskForBrowse;
    }

    public String getShareTaskMoto() {
        return ShareTaskMoto;
    }

    public void setShareTaskMoto(String shareTaskMoto) {
        this.ShareTaskMoto = shareTaskMoto;
    }

    public String getShareTaskRecordId() {
        return ShareTaskRecordId;
    }

    public void setShareTaskRecordId(String shareTaskRecordId) {
        this.ShareTaskRecordId = shareTaskRecordId;
    }

    public String getShareTaskIsShare() {
        return ShareTaskIsShare;
    }

    public void setShareTaskIsShare(String shareTaskIsShare) {
        this.ShareTaskIsShare = shareTaskIsShare;
    }

    public String getShareTaskUserId() {
        return ShareTaskUserId;
    }

    public void setShareTaskUserId(String shareTaskUserId) {
        this.ShareTaskUserId = shareTaskUserId;
    }

    public String getShareTaskPushId() {
        return ShareTaskPushId;
    }

    public void setShareTaskPushId(String shareTaskPushId) {
        this.ShareTaskPushId = shareTaskPushId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getShareTaskStyle() {
        return ShareTaskStyle;
    }

    public void setShareTaskStyle(String shareTaskStyle) {
        this.ShareTaskStyle = shareTaskStyle;
    }

    public String getShareTaskCompany() {
        return ShareTaskCompany;
    }

    public void setShareTaskCompany(String shareTaskCompany) {
        this.ShareTaskCompany = shareTaskCompany;
    }

    public String getShareTaskWechat() {
        return ShareTaskWechat;
    }

    public void setShareTaskWechat(String shareTaskWechat) {
        this.ShareTaskWechat = shareTaskWechat;
    }

    public String getShareTaskPhone() {
        return ShareTaskPhone;
    }

    public void setShareTaskPhone(String shareTaskPhone) {
        this.ShareTaskPhone = shareTaskPhone;
    }

    public String getShareTaskAddr() {
        return ShareTaskAddr;
    }

    public void setShareTaskAddr(String shareTaskAddr) {
        this.ShareTaskAddr = shareTaskAddr;
    }


    public String getShareTaskId() {
        return ShareTaskId;
    }

    public String getShareTaskHead() {
        return ShareTaskHead;
    }

    public String getShareTaskName() {
        return ShareTaskName;
    }

    public String getShareTaskImage() {
        return ShareTaskImage;
    }

    public String getShareTaskTitle() {
        return ShareTaskTitle;
    }

    public String getShareTaskTime() {
        return ShareTaskTime;
    }

    public String getShareTaskType() {
        return ShareTaskType;
    }

    public int getShareTaskStatus() {
        return ShareTaskStatus;
    }

    public String getShareTaskBrowse() {
        return ShareTaskBrowse;
    }

    public String getShareTaskJobs() {
        return ShareTaskJobs;
    }

    public void setShareTaskJobs(String shareTaskJobs) {
        this.ShareTaskJobs = shareTaskJobs;
    }


    public void setShareTaskId(String shareTaskId) {
        this.ShareTaskId = shareTaskId;
    }

    public void setShareTaskHead(String shareTaskHead) {
        this.ShareTaskHead = shareTaskHead;
    }

    public void setShareTaskName(String shareTaskName) {
        this.ShareTaskName = shareTaskName;
    }

    public void setShareTaskImage(String shareTaskImage) {
        this.ShareTaskImage = shareTaskImage;
    }

    public void setShareTaskTitle(String shareTaskTitle) {
        this.ShareTaskTitle = shareTaskTitle;
    }

    public void setShareTaskTime(String shareTaskTime) {
        this.ShareTaskTime = shareTaskTime;
    }

    public void setShareTaskType(String shareTaskType) {
        this.ShareTaskType = shareTaskType;
    }

    public void setShareTaskStatus(int shareTaskStatus) {
        this.ShareTaskStatus = shareTaskStatus;
    }

    public void setShareTaskBrowse(String shareTaskBrowse) {
        this.ShareTaskBrowse = shareTaskBrowse;
    }


}