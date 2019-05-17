package com.huimaibao.app.picture;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;

/**
 * 上传返回
 */

public interface LoadCallback<Request, String> {

    void onSuccess(Request request, String result);

    void onFailure(Request request, ClientException clientException, ServiceException serviceException);
}
