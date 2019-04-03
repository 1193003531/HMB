package com.huimaibao.app.share;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huimaibao.app.api.ServerApi;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

        // 将该app注册到微信
        api.registerApp(ServerApi.WX_APP_ID);
    }
}
