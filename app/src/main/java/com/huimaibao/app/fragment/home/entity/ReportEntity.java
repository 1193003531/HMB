package com.huimaibao.app.fragment.home.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class ReportEntity implements Serializable {


    public String ReportId;
    public String ReportName;
    public boolean ReportIsCheck;


    public String getReportId() {
        return ReportId;
    }

    public void setReportId(String ReportId) {
        this.ReportId = ReportId;
    }

    public String getReportName() {
        return ReportName;
    }

    public void setReportName(String ReportName) {
        this.ReportName = ReportName;
    }

    public boolean getReportIsCheck() {
        return ReportIsCheck;
    }

    public void setReportIsCheck(boolean ReportIsCheck) {
        this.ReportIsCheck = ReportIsCheck;
    }

}