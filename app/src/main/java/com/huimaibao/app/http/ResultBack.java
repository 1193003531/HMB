package com.huimaibao.app.http;


public interface ResultBack<Object> {

    void onSuccess(Object object);

    void onFailed(String error);

}
