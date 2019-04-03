package com.huimaibao.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseApplication;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XToast;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wechat_pay_result);

        BaseApplication.mWxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        BaseApplication.mWxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        //Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                XToast.normal("支付成功");
                XPreferencesUtils.put("isWXPayMoney", true);
            } else if (resp.errCode == -1) {
                XToast.normal("支付失败");
            } else if (resp.errCode == -2) {
                XToast.normal( "取消支付");
            }
            finish();
        }
//        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("提示");
//            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//            builder.show();
//        }
    }
}