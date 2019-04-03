package com.huimaibao.app.fragment.mine.server;

import android.app.Activity;
import android.view.View;

import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.DialogUtils;
import com.youth.xframe.utils.XNetworkUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.http.HttpCallBack;
import com.youth.xframe.utils.http.XHttp;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * @Used 名片相关接口
 */
public class CardClipLogic {
    private Activity mActivity;
    private static DialogUtils mDialogUtils;
    private static CardClipLogic _Instance = null;

    public static CardClipLogic Instance(Activity activity) {
        _Instance = new CardClipLogic(activity);
        mDialogUtils = new DialogUtils(activity);
        return _Instance;
    }


    private CardClipLogic(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * 获取收藏的名片
     */
    public void getCardClipApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().get(ServerApi.CARD_CLIP_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShow)
                        mDialogUtils.showLoadingDialog("加载中...");

                }

                @Override
                public void dismissProgress() {
                    if (isShow)
                        mDialogUtils.dismissDialog();

                }

                @Override
                public void onSuccess(Object o) {
                    resultBack.onSuccess(o);
                }

                @Override
                public void onFailed(String error) {
                    resultBack.onFailed(error);
                }
            });
        } else {
            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
        }
    }


    /**
     * 添加收藏的名片
     */
    public void getCardClipAddApi(HashMap<String, Object> map, final boolean isShow, final ResultBack resultBack) {
        if (XNetworkUtils.isConnected()) {
            XHttp.obtain().post(ServerApi.CARD_CLIP_URL, map, new HttpCallBack() {
                @Override
                public void showProgress() {
                    if (isShow)
                        mDialogUtils.showLoadingDialog("收藏中...");

                }

                @Override
                public void dismissProgress() {
                    if (isShow)
                        mDialogUtils.dismissDialog();

                }

                @Override
                public void onSuccess(Object o) {
                    resultBack.onSuccess(o);
                }

                @Override
                public void onFailed(String error) {
                    resultBack.onFailed(error);
                }
            });
        } else {
            XToast.normal(mActivity.getResources().getString(R.string.network_enable));
        }
    }

    /**
     * 删除收藏的名片
     */
    public void getCardClipDelApi(List<String> list, final boolean isShow, final String msg, final ResultBack resultBack) {
        try {
            // 创建json对象
            JSONObject jsonObject = new JSONObject();
            // 1个数组参数
            JSONArray jsonArray = new JSONArray();
            for (String tag : list) {
                jsonArray.put(tag);
            }
            jsonObject.put("ids", jsonArray);
            // 3个字符串参数

            String data = jsonObject.toString();
            // 构造请求体
            RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), data);
            Request request = new Request.Builder()
                    .addHeader("authorization", "bearer " + XPreferencesUtils.get("token", "").toString())
                    .url(ServerApi.CARD_CLIP_URL)
                    .delete(body)
                    .build();
            if (isShow)
                mDialogUtils.showLoadingDialog(msg);
            // 向服务器异步请求数据
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (isShow)
                        mDialogUtils.dismissDialog();
                    resultBack.onFailed(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (isShow)
                        mDialogUtils.dismissDialog();
                    if (response.isSuccessful()) {
                        resultBack.onSuccess(response.body().string());
                    } else {
                        resultBack.onFailed(response.message());
                    }
                }
            });
        } catch (JSONException e) {
            if (isShow)
                mDialogUtils.dismissDialog();
            resultBack.onFailed(e.toString());
        }
    }


}