package com.huimaibao.app.share;

public interface OnResponseListener {
    void onSuccess();

    void onCancel();

    void onFail(String message);
}
