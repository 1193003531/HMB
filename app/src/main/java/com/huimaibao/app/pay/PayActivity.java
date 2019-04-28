package com.huimaibao.app.pay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.mine.act.WalletActivity;
import com.huimaibao.app.fragment.mine.server.MemberLogic;
import com.huimaibao.app.fragment.mine.settings.VerifyPhoneActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.huimaibao.app.view.PayPsdInputView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XKeyboardUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.utils.permission.XPermission;
import com.youth.xframe.widget.XToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付
 */
public class PayActivity extends BaseActivity {

    private String mType = "";

    private TextView _pay_type, _pay_money;
    //类型，支付金额，钱包
    private String _type_value = "", _pay_money_value = "0", _wallet_value = "0";
    //钱包，微信，支付宝，支付
    private TextView _wallet_tv, _pay_wechat_tv, _pay_alipay_tv, _pay_btn;
    private Switch _wallet_btn;
    private CheckBox _pay_wechat_btn, _pay_alipay_btn;
    //是否选择钱包
    private boolean isCheckWallet = false;
    private boolean isOtherPay = true;
    //0-支付宝，1-微信,3-钱包抵扣
    private String _pay_type_value = "1", _pay_type_id_value = "1";
    //1会员 2克隆 3脉宝
    private String biz_type = "";

    //需支付金额
    private String _money_value = "";

    private DialogUtils mDialogUtils;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                //showAlert(mActivity, "支付成功: " + payResult);
                showToast("支付成功");
                XPreferencesUtils.put("isPaymentMoney", true);
                finish();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                showToast("支付失败");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pay_layout);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
            _type_value = intent.getStringExtra("payType");
            _pay_money_value = intent.getStringExtra("payMoney");
            _pay_type_id_value = intent.getStringExtra("payId");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);


        mDialogUtils = new DialogUtils(mActivity);

        initView();
        initData();
    }


    /***/
    private void initView() {
        _pay_type = findViewById(R.id.pay_type_value);
        _pay_money = findViewById(R.id.pay_money_value);

        _wallet_tv = findViewById(R.id.wallet_maoey_tv);
        _wallet_btn = findViewById(R.id.wallet_maoey_btn);
        _pay_wechat_tv = findViewById(R.id.pay_wechat_money_tv);
        _pay_alipay_tv = findViewById(R.id.pay_alipay_money_tv);
        _pay_wechat_btn = findViewById(R.id.pay_wechat_money_cb);
        _pay_alipay_btn = findViewById(R.id.pay_alipay_money_cb);
        _pay_btn = findViewById(R.id.pay_money_btn);
    }

    /***/
    private void initData() {
        _pay_type.setText(_type_value);
        _pay_money.setText("￥" + _pay_money_value);
        _wallet_value = XPreferencesUtils.get("money", "0").toString().trim();
        _wallet_tv.setText("账户余额共￥" + _wallet_value);
        _money_value = _pay_money_value;
        _pay_btn.setText("实付款" + _pay_money_value + "元");
        _wallet_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (XStringUtils.m1(_pay_money_value) >= XStringUtils.m1(_wallet_value)) {
                        _wallet_tv.setText("账户余额共￥" + _wallet_value + ",抵￥" + _wallet_value);
                        _pay_wechat_tv.setTextColor(getResources().getColor(R.color.color000000));
                        _pay_alipay_tv.setTextColor(getResources().getColor(R.color.color000000));
                        isCheckWallet = true;
                        isOtherPay = true;
                        _pay_type_value = "1";
                        _pay_wechat_btn.setChecked(true);
                        _pay_alipay_btn.setChecked(false);
                        _money_value = XStringUtils.m1((XStringUtils.m1(_pay_money_value) - XStringUtils.m1(_wallet_value)) + "") + "";
                        _pay_btn.setText("实付款" + _money_value + "元");
                    } else {
                        _wallet_tv.setText("账户余额共￥" + _wallet_value + ",抵￥" + _pay_money_value);
                        _pay_wechat_tv.setTextColor(getResources().getColor(R.color.color999999));
                        _pay_alipay_tv.setTextColor(getResources().getColor(R.color.color999999));
                        isCheckWallet = false;
                        isOtherPay = false;
                        _pay_wechat_btn.setChecked(false);
                        _pay_alipay_btn.setChecked(false);
                        _pay_type_value = "3";
//                        _money_value = "" + (XStringUtils.m1(_wallet_value) - XStringUtils.m1(_pay_money_value));
//                        if ((XStringUtils.m1(_wallet_value) - XStringUtils.m1(_pay_money_value)) > 0) {
//                            _money_value = "0";
//                            _pay_btn.setText("实付款" + _money_value + "元");
//                        }
                        _money_value = "0";
                        _pay_btn.setText("实付款" + _money_value + "元");
                    }
                } else {
                    isCheckWallet = false;
                    isOtherPay = true;
                    _pay_type_value = "1";
                    _pay_wechat_btn.setChecked(true);
                    _pay_alipay_btn.setChecked(false);
                    _money_value = _pay_money_value;
                    _wallet_tv.setText("账户余额共￥" + _wallet_value);
                    _pay_btn.setText("实付款" + _money_value + "元");

                }
            }
        });

    }

    public void onAction(View v) {
        switch (v.getId()) {
            //微信支付
            case R.id.pay_wechat_maoey_btn:
                if (isOtherPay) {
                    _pay_type_value = "1";
                    _pay_wechat_btn.setChecked(true);
                    _pay_alipay_btn.setChecked(false);
                }
                break;
            //支付宝支付
            case R.id.pay_alipay_money_btn:
                if (isOtherPay) {
                    _pay_type_value = "0";
                    _pay_wechat_btn.setChecked(false);
                    _pay_alipay_btn.setChecked(true);
                }
                break;
            //支付
            case R.id.pay_money_btn:
                if (mType.equals("开通会员")) {
                    biz_type = "1";
                } else if (mType.equals("克隆付费")) {
                    biz_type = "2";
                }
                //钱包支付
                if (_pay_type_value.equals("3")) {
                    if ((boolean) XPreferencesUtils.get("payment_pwd", false)) {
                        mDialogUtils.showBankPayPwdDialog(new PayPsdInputView.onPasswordListener() {
                            @Override
                            public void onDifference(String oldPsd, String newPsd) {
                            }

                            @Override
                            public void onEqual(String psd) {
                            }

                            @Override
                            public void inputFinished(String inputPsd) {
                                getMemberPaymentData(biz_type, _money_value, "1", "0", "" + _pay_type_id_value, inputPsd);

                                mDialogUtils.dismissDialog();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(VerifyPhoneActivity.class, "忘记支付密码");
                                mDialogUtils.dismissDialog();
                            }
                        });
                       // XKeyboardUtils.closeKeyboard(mActivity);
                    } else {
                        mDialogUtils.showNoTitleDialog("您还没有设置支付密码,马上去设置?", "取消", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(VerifyPhoneActivity.class, "设置支付密码");
                                mDialogUtils.dismissDialog();
                            }
                        });
                    }

                }
                //微信支付
                else if (_pay_type_value.equals("1")) {
                    getMemberPaymentData(biz_type, _money_value, isCheckWallet ? "1" : "0", "1", "" + _pay_type_id_value, "");
                }
                //支付宝支付
                else if (_pay_type_value.equals("0")) {
                    getMemberPaymentData(biz_type, _money_value, isCheckWallet ? "1" : "0", "2", "" + _pay_type_id_value, "");
                }

                break;
        }
    }


    /**
     * 获取vip用户信息
     */
    private void getMemberPaymentData(String biz_type, String money, String wallet, String pay_type, String target_id, String pwd) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("biz_type", biz_type);//1会员 2克隆 3脉宝
        map.put("money", money);//实付款金额
//        if(wallet.equals("1")){
//            map.put("wallet", true);//是否勾选钱包 1or0
//        }else{
//            map.put("wallet", false);//是否勾选钱包 1or0
//        }
        map.put("wallet", wallet);//是否勾选钱包 1or0
        map.put("pay_type", pay_type);//支付方式 0钱包 1微信 2支付宝
        map.put("target_id", target_id);//会员配置id clone_id 脉宝个数
        map.put("pwd", pwd);//支付密码
        LogUtils.debug("json:" + map);
        MemberLogic.Instance(mActivity).memberPaymentApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    //String data = json.getString("data");

                    if (status.equals("0")) {
                        final JSONObject data = new JSONObject(json.getString("data"));
                        if (data.optString("pay_type").equals("0")) {
                            showToast("支付成功");
                            XPreferencesUtils.put("isPaymentMoney", true);
                            finish();
                        } else if (data.optString("pay_type").equals("1")) {
                            JSONObject params = new JSONObject(data.optString("params"));
                            final String appId = params.optString("appid");
                            final String partnerId = params.optString("partnerid");
                            final String prepayId = params.optString("prepayid");
                            final String packageValue = params.optString("package");
                            final String nonceStr = params.optString("noncestr");
                            final String timeStamp = params.optString("timestamp");
                            final String signValue = params.optString("sign");
                            //微信支付
                            WXPay(mActivity, appId, partnerId, prepayId, packageValue, nonceStr, timeStamp, signValue);
                        } else if (data.optString("pay_type").equals("2")) {
                            XPermission.requestPermissions(mActivity, 1002, new String[]{
                                    Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            }, new XPermission.OnPermissionListener() {
                                //权限申请成功时调用
                                @Override
                                public void onPermissionGranted() {
                                    Alipay(data.optString("params"));
                                }

                                //权限被用户禁止时调用
                                @Override
                                public void onPermissionDenied() {
                                    //给出友好提示，并且提示启动当前应用设置页面打开权限
                                    XPermission.showTipsDialog(mActivity);
                                }
                            });
                        }
                    } else {
                        showToast("支付失败," + message);
                    }
                } catch (Exception e) {
                    LogUtils.debug("json:" + e);
                    showToast("支付失败");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("json:" + error);
                showToast("支付失败");
            }
        });
    }


    /**
     * 调起微信支付的方法,不需要在客户端签名
     **/
    private IWXAPI iwxapi; //微信支付api

    public void WXPay(Context context, final String appId, final String partnerId, final String prepayId, final String packageValue, final String nonceStr, final String timeStamp, final String signValue) {

        iwxapi = WXAPIFactory.createWXAPI(context, appId); //初始化微信api
        iwxapi.registerApp(appId); //注册appid  appid可以在开发平台获取

        Runnable payRunnable = new Runnable() {  //这里注意要放在子线程
            @Override
            public void run() {
                PayReq request = new PayReq(); //调起微信APP的对象
                //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
                request.appId = appId;
                request.partnerId = partnerId;
                request.prepayId = prepayId;
                request.packageValue = packageValue;
                request.nonceStr = nonceStr;
                request.timeStamp = timeStamp;
                request.sign = signValue;
                iwxapi.sendReq(request);//发送调起微信的请求
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝支付
     */
    private void Alipay(final String orderInfo) {


        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity);
                //用户在商户app内部点击付款，是否需要一个loading做为在钱包唤起之前的过渡，这个值设置为true，
                // 将会在调用pay接口的时候直接唤起一个loading，直到唤起H5支付页面或者唤起外部的钱包付款页面loading才消失。
                // （建议将该值设置为true，优化点击付款到支付唤起支付页面的过渡过程。）
                Map<String, String> result = alipay.payV2(orderInfo, true);
                //XLog.d("msp", result.toString());

                Message msg = new Message();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) XPreferencesUtils.get("isWXPayMoney", false)) {
            XPreferencesUtils.put("isPaymentMoney", true);
            finish();
            XPreferencesUtils.put("isWXPayMoney", false);
        }
    }
}
