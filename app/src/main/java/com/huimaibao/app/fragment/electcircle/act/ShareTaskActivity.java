package com.huimaibao.app.fragment.electcircle.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.electcircle.adapter.ShareTaskAdapter;
import com.huimaibao.app.fragment.electcircle.entity.ShareTaskEntity;
import com.huimaibao.app.fragment.electcircle.server.ElectLogic;
import com.huimaibao.app.fragment.mine.act.CardClipDetailActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.fragment.web.PersonalWebDetailsActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.share.OnResponseListener;
import com.huimaibao.app.share.WXShare;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 分享任务
 */
public class ShareTaskActivity extends BaseActivity {

    private String mType = "";
    private TextView _top_btn;

    private LinearLayout _no_data;
    private ListView mListView;
    private ShareTaskAdapter mAdapter;
    private List<ShareTaskEntity> listData;

    private WXShare mWxShare;

    private String _record_id_value = "", _cover_value = "";//互推id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_elect_share_task);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }
        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);
        setShowLine(false);

        mWxShare = WXShare.Instance(mActivity);

        initView();
        initData();
    }

    private void initData() {
//        listData = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            ShareTaskEntity entity = new ShareTaskEntity();
//            if (i % 4 == 0) {
//                entity.setType(0);
//            } else {
//                entity.setType(1);
//            }
//            entity.setShareTaskType("" + i);
//            entity.setShareTaskAddr("重庆市渝北区");
//            entity.setShareTaskBrowse("52" + i);
//            entity.setShareTaskCompany("重庆市汇脉宝网络科技有限公司");
//            entity.setShareTaskHead("");
//            entity.setShareTaskId("" + 1);
//            entity.setShareTaskImage("");
//            entity.setShareTaskJobs("产品经理");
//            entity.setShareTaskName("小脉宝");
//            entity.setShareTaskPhone("12568954689");
//            entity.setShareTaskStyle("style7");
//            entity.setShareTaskTime("2019-01-09 10:50:30");
//            entity.setShareTaskWechat("we265894");
//            entity.setShareTaskTitle("测试城市菜市场测试测试测试测试");
//            listData.add(entity);
//        }


        getShareTaskData(true);
    }


    /**
     * 初始化控件
     */
    private void initView() {
        _top_btn = findViewById(R.id.elect_share_task_top);

        mListView = findViewById(R.id.list_pull_value);
        _no_data = findViewById(R.id.list_no_data);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (listData.get(position).getShareTaskStatus()) {
                    //1,文章，2个人网页，3营销网页， 4名片
                    case 1:
                        startActivity(MessageWebActivity.class, "", ServerApi.ARTICLE_DETAILS_URL + listData.get(position).getShareTaskId()+"/"+listData.get(position).getShareTaskUserId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                        break;
                    case 2:
                        Bundle bundle = new Bundle();
                        bundle.putString("id", listData.get(position).getShareTaskId());
                        bundle.putString("vUrl", ServerApi.PERSONAL_DETAILS_URL2 + listData.get(position).getShareTaskId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                        bundle.putString("share_title", listData.get(position).getShareTaskTitle());
                        bundle.putString("share_des", "");
                        bundle.putString("share_imageUrl", listData.get(position).getShareTaskImage());

                        startActivity(PersonalWebDetailsActivity.class, bundle);
                        //startActivity(PersonalWebDetailsActivity.class, listData.get(position).getShareTaskId(), ServerApi.PERSONAL_DETAILS_URL2 + listData.get(position).getShareTaskId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                        break;
                    case 3:
                        startActivity(MessageWebActivity.class, "", ServerApi.MARKETING_DETAILS_URL + listData.get(position).getShareTaskId() + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                        break;
                    case 4:
                        startActivity(CardClipDetailActivity.class, listData.get(position).getShareTaskUserId());
                        break;
                }
            }
        });

    }


    /**
     * 顶部选择变化
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.elect_share_task_top:
                _top_btn.setVisibility(View.GONE);
                setShowLine(true);
                break;
        }
    }


    /**
     * 获取数据
     */
    private void getShareTaskData(boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "3");
        ElectLogic.Instance(mActivity).getShareApi(map, isShow, "加载中...", new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    if (json.optString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.optString("data"));
                        JSONArray arrayPush;
                        JSONObject pushData;
                        listData = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            arrayPush = new JSONArray(array.optJSONObject(i).optString("passive_inter_push_record"));
                            for (int j = 0; j < arrayPush.length(); j++) {

                                if (!XEmptyUtils.isSpace(arrayPush.optJSONObject(j).optString("inter_push_record_materials", ""))) {
                                    pushData = new JSONObject(arrayPush.optJSONObject(j).optString("inter_push_record_materials"));
                                } else {
                                    pushData = new JSONObject();
                                }
                                ShareTaskEntity entity = new ShareTaskEntity();
                                if (pushData.optInt("source_style", 4) == 4) {
                                    entity.setType(0);
                                    try {
                                        JSONObject cardData = new JSONObject(pushData.optString("title"));
                                        entity.setShareTaskAddr(cardData.optString("address_detail"));
                                        entity.setShareTaskCompany(cardData.optString("company"));
                                        entity.setShareTaskJobs(cardData.optString("profession"));
                                        entity.setShareTaskPhone(cardData.optString("mobile_phone"));
                                        entity.setShareTaskStyle(cardData.optString("style", "style3"));
                                        entity.setShareTaskWechat(cardData.optString("wechat_code"));
                                        entity.setShareTaskTitle(pushData.optString("user_name"));
                                        entity.setShareTaskMoto(cardData.optString("moto"));
                                    } catch (Exception e) {
                                        entity.setShareTaskAddr("");
                                        entity.setShareTaskCompany("");
                                        entity.setShareTaskJobs("");
                                        entity.setShareTaskPhone("");
                                        entity.setShareTaskStyle("style3");
                                        entity.setShareTaskWechat("");
                                        entity.setShareTaskMoto("");
                                        entity.setShareTaskTitle(pushData.optString("user_name"));
                                    }

                                } else {
                                    entity.setType(1);
                                    entity.setShareTaskTitle(pushData.optString("title"));
                                }
                                entity.setShareTaskRecordId(arrayPush.getJSONObject(i).optString("id"));
                                entity.setShareTaskPushId(arrayPush.getJSONObject(i).optString("interpush_id"));
                                entity.setShareTaskType("" + pushData.optInt("source_style", 4));
                                entity.setShareTaskStatus(pushData.optInt("source_style", 4));
                                entity.setShareTaskBrowse(pushData.optString("browse_num"));
                                entity.setShareTaskHead(pushData.optString("head_portrait"));
                                entity.setShareTaskId(pushData.optString("source_id"));
                                entity.setShareTaskUserId(pushData.optString("user_id"));
                                entity.setShareTaskName(pushData.optString("user_name"));
                                entity.setShareTaskTime(pushData.optString("created_at"));


                                if (pushData.optString("cover").contains("[")) {
                                    try {
                                        entity.setShareTaskImage(JSON.parseArray(pushData.optString("cover")).get(0).toString().replace("\"", "").trim());
                                    } catch (Exception e) {
                                        entity.setShareTaskImage("");
                                    }
                                } else {
                                    try {
                                        entity.setShareTaskImage(pushData.optString("cover").replace("\"", "").trim());
                                    } catch (Exception e) {
                                        entity.setShareTaskImage("");
                                    }
                                }
                                listData.add(entity);
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (listData.size() > 0) {
                                    _no_data.setVisibility(View.GONE);
                                } else {
                                    _no_data.setVisibility(View.VISIBLE);
                                }

                                mAdapter = new ShareTaskAdapter(mActivity, listData);
                                mListView.setAdapter(mAdapter);
                                mAdapter.setOnItemShareListener(new ShareTaskAdapter.onItemShareListener() {
                                    @Override
                                    public void onItemShareClick(final int position) {
                                        _record_id_value = listData.get(position).getShareTaskRecordId();
                                        _cover_value = listData.get(position).getShareTaskImage();

                                        switch (listData.get(position).getShareTaskStatus()) {
                                            case 1:
                                                setShare(listData.get(position).getShareTaskTitle(), "", ServerApi.ARTICLE_DETAILS_URL + listData.get(position).getShareTaskId() + "/" + listData.get(position).getShareTaskUserId() + "?created_at=" + XTimeUtils.StringToYMD(listData.get(position).getShareTaskTime()) + "&interpush_id=" + XPreferencesUtils.get("user_id", "") + "&target_interpush_id=" + listData.get(position).getShareTaskUserId(), _cover_value);
                                                break;
                                            case 2:
                                                setShare(listData.get(position).getShareTaskTitle(), "", ServerApi.PERSONAL_DETAILS_URL + "detail/" + listData.get(position).getShareTaskId() + "?created_at=" + XTimeUtils.StringToYMD(listData.get(position).getShareTaskTime()) + "&interpush_id=" + XPreferencesUtils.get("user_id", "") + "&target_interpush_id=" + listData.get(position).getShareTaskUserId(), _cover_value);
                                                break;
                                            case 3:
                                                setShare(listData.get(position).getShareTaskTitle(), "", ServerApi.MARKETING_DETAILS_URL + listData.get(position).getShareTaskId() + "?created_at=" + XTimeUtils.StringToYMD(listData.get(position).getShareTaskTime()) + "&interpush_id=" + XPreferencesUtils.get("user_id", "") + "&target_interpush_id=" + listData.get(position).getShareTaskUserId(), _cover_value);
                                                break;
                                            case 4:
                                                setShare("我是" + listData.get(position).getShareTaskName() + " 邀请您与我共享千万营销人名片", listData.get(position).getShareTaskMoto(), ServerApi.CARD_URL + listData.get(position).getShareTaskUserId() + "?created_at=" + XTimeUtils.StringToYMD(listData.get(position).getShareTaskTime()) + "&interpush_id=" + XPreferencesUtils.get("user_id", "") + "&target_interpush_id=" + listData.get(position).getShareTaskUserId(), listData.get(position).getShareTaskHead());
                                                break;
                                        }
                                    }
                                });
                            }
                        });


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("error:" + error);
            }
        });
    }

    /**
     * 分享
     */
    private void setShare(final String title, final String des, final String url, final String imagUrl) {

        mWxShare.showShareDialog("互推圈",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPassiveShare();
                //好友
                getShare(0, title, des, url, imagUrl);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPassiveShare();
                //朋友圈
                getShare(1, title, des, url, imagUrl);
            }
        });
    }

    private void getShare(int sceneFlag, String title, String des, String url, String imagUrl) {
        //"我是" + _name_value + " " + "邀请您与我共享千万营销人名片", XEmptyUtils.isSpace(X) ? "汇脉宝--共享流量，专注营销" : shareDes,
        //String des = "" + XPreferencesUtils.get("motto", "");
        ImageSize imageSize = new ImageSize(120, 120);
        Bitmap thumb;
        try {
            thumb = ImageLoader.getInstance().loadImageSync(imagUrl, imageSize);
        } catch (Exception e) {
            thumb = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.layer_share_icon);
        }
        mWxShare.WXShareWeb(title, XEmptyUtils.isSpace(des) ? "汇脉宝--共享流量，专注营销" : des, url, thumb, sceneFlag, new OnResponseListener() {
            @Override
            public void onSuccess() {
                XToast.normal("分享成功");
                getShareTaskData(false);
            }

            @Override
            public void onCancel() {
                XToast.normal("分享取消");
            }

            @Override
            public void onFail(String message) {
                XToast.normal("分享失败");
            }
        });
        mWxShare.setDismiss();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mWxShare != null) {
            mWxShare.register();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWxShare != null) {
            mWxShare.unregister();
        }

    }

    /**
     * 分享被动匹配put
     */
    private void getPassiveShare() {
        ElectLogic.Instance(mActivity).getPassiveShareApi(_record_id_value, false, "", new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    // hideDialog();
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("PassiveShare:" + json);
                    if (json.getString("status").equals("0")) {
                        XPreferencesUtils.put("isMaterial", true);
                    }
                } catch (Exception e) {
                    LogUtils.debug("PassiveShare:" + e);
                    //  hideDialog();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("PassiveShare:" + error);
                // hideDialog();
            }
        });
    }


}
