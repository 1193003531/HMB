package com.youth.xframe.banner;

/**
 *
 */
public class BannerItemBean {
    Object img_path;
    String title;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public BannerItemBean(Object img_path, String title, String url) {
        this.img_path = img_path;
        this.title = title;
        this.url = url;
    }

    public BannerItemBean(Object img_path) {
        this.img_path = img_path;
    }

    public BannerItemBean() {
    }

    public Object getImg_path() {
        return img_path;
    }

    public void setImg_path(Object img_path) {
        this.img_path = img_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
