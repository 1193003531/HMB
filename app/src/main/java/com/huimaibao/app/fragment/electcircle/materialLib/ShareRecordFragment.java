package com.huimaibao.app.fragment.electcircle.materialLib;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.huimaibao.app.R;
import com.huimaibao.app.api.ServerApi;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.electcircle.adapter.ShareRecordAdapter;
import com.huimaibao.app.fragment.electcircle.entity.ShareRecordEntity;
import com.huimaibao.app.fragment.electcircle.entity.ShareTaskEntity;
import com.huimaibao.app.fragment.electcircle.server.ElectLogic;
import com.huimaibao.app.fragment.mine.act.CardClipDetailActivity;
import com.huimaibao.app.fragment.web.MessageWebActivity;
import com.huimaibao.app.fragment.web.PersonalWebDetailsActivity;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.JsonFormatUtils;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.XScrollViewUtils;
import com.youth.xframe.widget.GifView;
import com.youth.xframe.widget.NoScrollListView;
import com.youth.xframe.widget.XScrollView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 分享记录
 */
public class ShareRecordFragment extends BaseFragment {


    private String _view_name = "", _view_type = "";

    private LinearLayout _top_y_share_ll, _top_w_share_ll, _no_data;
    private TextView _top_num_1, _top_num_2;

    private XScrollView mScrollView;
    private ListView mListView;
    private ShareRecordAdapter mAdapter;
    private List<ShareRecordEntity> listData;

    private int countPage = 1;

    //加载更多
    private View mFooterView;
    private GifView _footer_gif;
    private boolean isLoading = false;


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_elect_share_record_list, container, false);
        Bundle args = getArguments();
        _view_name = args != null ? args.getString("name") : "已分享";
        _view_type = args != null ? args.getString("type") : "1";

        initView(view);
        return view;
    }


    @Override
    protected void initData() {
        getData();
    }


    private void initView(View v) {
        _top_y_share_ll = v.findViewById(R.id.elect_share_record_top_yfx_ll);
        _top_w_share_ll = v.findViewById(R.id.elect_share_record_top_wfx_ll);
        _top_num_1 = v.findViewById(R.id.elect_share_record_num_1);
        _top_num_2 = v.findViewById(R.id.elect_share_record_num_2);

        _no_data = v.findViewById(R.id.list_no_data);
        mListView = v.findViewById(R.id.list_pull_value);

        mListView.setFocusable(false);

        mFooterView = View.inflate(mActivity, R.layout.view_footer, null);
        _footer_gif = mFooterView.findViewById(R.id.load_gif);
        _footer_gif.setMovieResource(com.youth.xframe.R.raw.load_gif_icon);


        mScrollView = v.findViewById(R.id.elect_share_record_sv);

        mScrollView.setScanScrollChangedListener(new XScrollView.ISmartScrollChangedListener() {
            @Override
            public void onScrolledToBottom() {
                if (listData.size() > 0 && !isLoading) {
                    mListView.removeFooterView(mFooterView);
                    mListView.addFooterView(mFooterView);
                    isLoading = true;
                    loadMoreData();
                }
            }

            @Override
            public void onScrolledToTop() {
            }
        });

        //加载更多
//        _footer_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadMoreData();
//            }
//        });

        if (_view_name.equals("已分享")) {
            _top_y_share_ll.setVisibility(View.VISIBLE);
            _top_w_share_ll.setVisibility(View.GONE);
            _top_num_1.setText("" + XPreferencesUtils.get("forme_browse_num", "0"));
            _top_num_2.setText("" + XPreferencesUtils.get("others_browse_num", "0"));
        } else {
            _top_y_share_ll.setVisibility(View.GONE);
            _top_w_share_ll.setVisibility(View.VISIBLE);
        }

    }


    private void loadMoreData() {
//        _footer_pro.setVisibility(View.VISIBLE);
//        _footer_tv.setText("正在加载中...");
        countPage++;
        if (_view_name.equals("已分享")) {
            getShareTaskData(countPage, "1", false);
        } else {
            getShareTaskData(countPage, "2", false);
        }
    }

    /**
     * 加载数据
     */
    private void getData() {
        countPage = 1;
        if (_view_name.equals("已分享")) {
            getShareTaskData(countPage, "1", true);
        } else {
            getShareTaskData(countPage, "2", true);
        }
    }

    /**
     * 获取数据
     */
    private void getShareTaskData(final int page, final String type, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("page", page);
        ElectLogic.Instance(mActivity).getShareApi(map, isShow, "加载中...", new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    if (json.optString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.optJSONObject("data").optString("data"));
                        JSONArray arrayPush, arrayPassivePush;
                        JSONObject pushData1 = null, pushData2 = null;
                        if (countPage == 1) {
                            listData = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }
                        for (int i = 0; i < array.length(); i++) {
                            arrayPush = new JSONArray(array.optJSONObject(i).optString("inter_push_record"));
                            for (int j = 0; j < arrayPush.length(); j++) {
                                ShareRecordEntity recordEntity = new ShareRecordEntity();
                                ShareTaskEntity entity1 = new ShareTaskEntity();
                                ShareTaskEntity entity2 = new ShareTaskEntity();
                                if (!XEmptyUtils.isSpace(arrayPush.optJSONObject(j).optString("inter_push_record_materials", ""))) {
                                    pushData1 = new JSONObject(arrayPush.optJSONObject(j).optString("inter_push_record_materials", ""));//主动
                                } else {
                                    pushData1 = new JSONObject();
                                }

                                if (!XEmptyUtils.isSpace(arrayPush.optJSONObject(j).optString("passive_inter_push_record_materials", ""))) {
                                    pushData2 = new JSONObject(arrayPush.optJSONObject(j).optString("passive_inter_push_record_materials", ""));//被动
                                } else {
                                    pushData2 = new JSONObject();
                                }


                                //设置主动数据
                                if (pushData1.optInt("source_style", 4) == 4) {
                                    entity1.setType(0);
                                    JSONObject cardData;
                                    try {
                                        cardData = new JSONObject(pushData1.optString("title"));
                                        entity1.setShareTaskAddr(cardData.optString("address_detail"));
                                        entity1.setShareTaskCompany(cardData.optString("company"));
                                        entity1.setShareTaskJobs(cardData.optString("profession"));
                                        entity1.setShareTaskPhone(cardData.optString("mobile_phone"));
                                        entity1.setShareTaskStyle(cardData.optString("style", "style3"));
                                        entity1.setShareTaskWechat(cardData.optString("wechat_code"));
                                        entity1.setShareTaskTitle(pushData1.optString("user_name"));
                                    } catch (Exception e) {
                                        entity1.setShareTaskAddr("");
                                        entity1.setShareTaskCompany("");
                                        entity1.setShareTaskJobs("");
                                        entity1.setShareTaskPhone("");
                                        entity1.setShareTaskStyle("style3");
                                        entity1.setShareTaskWechat("");
                                        entity1.setShareTaskTitle(pushData1.optString("user_name"));
                                    }

                                } else {
                                    entity1.setType(1);
                                    entity1.setShareTaskTitle(pushData1.optString("title"));
                                }
                                entity1.setShareTaskStatus(pushData1.optInt("source_style", 4));
                                entity1.setShareTaskPushId(arrayPush.getJSONObject(i).optString("interpush_id"));
                                entity1.setShareTaskIsShare(arrayPush.getJSONObject(i).optString("target_is_share"));
                                entity1.setShareTaskType(type);
                                entity1.setShareTaskForBrowse(pushData1.optString("browse_num"));
                                entity1.setShareTaskBrowse(pushData1.optString("browse_num", "0"));
                                entity1.setShareTaskHead(pushData1.optString("head_portrait"));
                                entity1.setShareTaskId(pushData1.optString("source_id"));
                                entity1.setShareTaskUserId(pushData1.optString("user_id"));
                                entity1.setShareTaskName(pushData1.optString("user_name"));
                                entity1.setShareTaskTime(arrayPush.optJSONObject(j).optString("share_time"));
                                entity1.setShareTaskDate(arrayPush.optJSONObject(j).optString("created_at"));

                                if (pushData1.optString("cover").contains("[")) {
                                    try {
                                        entity1.setShareTaskImage(JSON.parseArray(pushData1.optString("cover")).get(0).toString().replace("\"", "").trim());
                                    } catch (Exception e) {
                                        entity1.setShareTaskImage("");
                                    }
                                } else {
                                    try {
                                        entity1.setShareTaskImage(pushData1.optString("cover").replace("\"", "").trim());
                                    } catch (Exception e) {
                                        entity1.setShareTaskImage("");
                                    }
                                }

                                //设置被动数据
                                if (pushData2.optInt("source_style", 4) == 4) {
                                    entity2.setType(0);
                                    try {
                                        JSONObject cardData = new JSONObject(pushData2.optString("title"));
                                        entity2.setShareTaskAddr(cardData.optString("address_detail"));
                                        entity2.setShareTaskCompany(cardData.optString("company"));
                                        entity2.setShareTaskJobs(cardData.optString("profession"));
                                        entity2.setShareTaskPhone(cardData.optString("mobile_phone"));
                                        entity2.setShareTaskStyle(cardData.optString("style", "style3"));
                                        entity2.setShareTaskWechat(cardData.optString("wechat_code"));
                                        entity2.setShareTaskTitle(pushData2.optString("user_name"));
                                    } catch (Exception e) {
                                        entity2.setShareTaskAddr("");
                                        entity2.setShareTaskCompany("");
                                        entity2.setShareTaskJobs("");
                                        entity2.setShareTaskPhone("");
                                        entity2.setShareTaskStyle("style3");
                                        entity2.setShareTaskWechat("");
                                        entity2.setShareTaskTitle(pushData2.optString("user_name"));
                                    }

                                } else {
                                    entity2.setType(1);
                                    entity2.setShareTaskTitle(pushData2.optString("title"));
                                }
                                entity2.setShareTaskStatus(pushData2.optInt("source_style", 4));
                                entity2.setShareTaskPushId(arrayPush.getJSONObject(i).optString("interpush_id"));
                                entity2.setShareTaskIsShare(arrayPush.getJSONObject(i).optString("target_is_share"));
                                entity2.setShareTaskType(type);
                                entity2.setShareTaskForBrowse(pushData2.optString("browse_num"));
                                entity2.setShareTaskBrowse(pushData2.optString("browse_num"));
                                entity2.setShareTaskHead(pushData2.optString("head_portrait"));
                                entity2.setShareTaskId(pushData2.optString("source_id"));
                                entity2.setShareTaskUserId(pushData2.optString("user_id"));
                                entity2.setShareTaskName(pushData2.optString("user_name"));
                                entity2.setShareTaskTime(arrayPush.optJSONObject(j).optString("share_time"));
                                entity2.setShareTaskDate(arrayPush.optJSONObject(j).optString("created_at"));

                                if (pushData2.optString("cover").contains("[")) {
                                    try {
                                        entity2.setShareTaskImage(JSON.parseArray(pushData2.optString("cover")).get(0).toString().replace("\"", "").trim());
                                    } catch (Exception e) {
                                        entity2.setShareTaskImage("");
                                    }
                                } else {
                                    try {
                                        entity2.setShareTaskImage(pushData2.optString("cover").replace("\"", "").trim());
                                    } catch (Exception e) {
                                        entity2.setShareTaskImage("");
                                    }
                                }

                                if (type.equals("1")) {
                                    if (pushData2.optString("user_id").equals(XPreferencesUtils.get("user_id", ""))) {
                                        recordEntity.setShareRecordOne(entity1);
                                        recordEntity.setShareRecordTwo(entity2);
                                    } else {
                                        recordEntity.setShareRecordOne(entity2);
                                        recordEntity.setShareRecordTwo(entity1);
                                    }
                                } else {
                                    recordEntity.setShareRecordOne(entity2);
                                    recordEntity.setShareRecordTwo(entity1);
                                }
                                listData.add(recordEntity);
                            }

                            if (type.equals("1")) {
                                arrayPassivePush = new JSONArray(array.optJSONObject(i).optString("passive_inter_push_record"));
                                for (int j = 0; j < arrayPassivePush.length(); j++) {
                                    ShareRecordEntity recordEntity = new ShareRecordEntity();
                                    ShareTaskEntity entity1 = new ShareTaskEntity();
                                    ShareTaskEntity entity2 = new ShareTaskEntity();
                                    if (!XEmptyUtils.isSpace(arrayPassivePush.optJSONObject(j).optString("inter_push_record_materials", ""))) {
                                        pushData1 = new JSONObject(arrayPassivePush.optJSONObject(j).optString("inter_push_record_materials", ""));//主动
                                    } else {
                                        pushData1 = new JSONObject();
                                    }

                                    if (!XEmptyUtils.isSpace(arrayPassivePush.optJSONObject(j).optString("passive_inter_push_record_materials", ""))) {
                                        pushData2 = new JSONObject(arrayPassivePush.optJSONObject(j).optString("passive_inter_push_record_materials", ""));//被动
                                    } else {
                                        pushData2 = new JSONObject();
                                    }


                                    //设置主动数据
                                    if (pushData1.optInt("source_style", 4) == 4) {
                                        entity1.setType(0);
                                        try {
                                            JSONObject cardData = new JSONObject(pushData1.optString("title"));
                                            entity1.setShareTaskAddr(cardData.optString("address_detail"));
                                            entity1.setShareTaskCompany(cardData.optString("company"));
                                            entity1.setShareTaskJobs(cardData.optString("profession"));
                                            entity1.setShareTaskPhone(cardData.optString("mobile_phone"));
                                            entity1.setShareTaskStyle(cardData.optString("style", "style3"));
                                            entity1.setShareTaskWechat(cardData.optString("wechat_code"));
                                            entity1.setShareTaskTitle(pushData1.optString("user_name"));
                                        } catch (Exception e) {
                                            entity1.setShareTaskAddr("");
                                            entity1.setShareTaskCompany("");
                                            entity1.setShareTaskJobs("");
                                            entity1.setShareTaskPhone("");
                                            entity1.setShareTaskStyle("style3");
                                            entity1.setShareTaskWechat("");
                                            entity1.setShareTaskTitle(pushData1.optString("user_name"));
                                        }


                                    } else {
                                        entity1.setType(1);
                                        entity1.setShareTaskTitle(pushData1.optString("title"));
                                    }
                                    entity1.setShareTaskStatus(pushData1.optInt("source_style", 4));
                                    entity1.setShareTaskPushId(arrayPassivePush.getJSONObject(i).optString("interpush_id"));
                                    entity1.setShareTaskIsShare(arrayPassivePush.getJSONObject(i).optString("target_is_share"));
                                    entity1.setShareTaskType(type);
                                    entity1.setShareTaskForBrowse(pushData1.optString("browse_num"));
                                    entity1.setShareTaskBrowse(pushData1.optString("browse_num", "0"));
                                    entity1.setShareTaskHead(pushData1.optString("head_portrait"));
                                    entity1.setShareTaskId(pushData1.optString("source_id"));
                                    entity1.setShareTaskUserId(pushData1.optString("user_id"));
                                    entity1.setShareTaskName(pushData1.optString("user_name"));
                                    entity1.setShareTaskTime(arrayPassivePush.optJSONObject(j).optString("share_time"));
                                    entity1.setShareTaskDate(arrayPassivePush.optJSONObject(j).optString("created_at"));

                                    if (pushData1.optString("cover").contains("[")) {
                                        try {
                                            entity1.setShareTaskImage(JSON.parseArray(pushData1.optString("cover")).get(0).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            entity1.setShareTaskImage("");
                                        }
                                    } else {
                                        try {
                                            entity1.setShareTaskImage(pushData1.optString("cover").replace("\"", "").trim());
                                        } catch (Exception e) {
                                            entity1.setShareTaskImage("");
                                        }
                                    }

                                    //设置被动数据
                                    if (pushData2.optInt("source_style", 4) == 4) {
                                        entity2.setType(0);
                                        try {
                                            JSONObject cardData = new JSONObject(pushData2.optString("title"));
                                            entity2.setShareTaskAddr(cardData.optString("address_detail"));
                                            entity2.setShareTaskCompany(cardData.optString("company"));
                                            entity2.setShareTaskJobs(cardData.optString("profession"));
                                            entity2.setShareTaskPhone(cardData.optString("mobile_phone"));
                                            entity2.setShareTaskStyle(cardData.optString("style", "style3"));
                                            entity2.setShareTaskWechat(cardData.optString("wechat_code"));
                                            entity2.setShareTaskTitle(pushData2.optString("user_name"));
                                        } catch (Exception e) {
                                            entity2.setShareTaskAddr("");
                                            entity2.setShareTaskCompany("");
                                            entity2.setShareTaskJobs("");
                                            entity2.setShareTaskPhone("");
                                            entity2.setShareTaskStyle("style3");
                                            entity2.setShareTaskWechat("");
                                            entity2.setShareTaskTitle(pushData2.optString("user_name"));
                                        }

                                    } else {
                                        entity2.setType(1);
                                        entity2.setShareTaskTitle(pushData2.optString("title"));
                                    }
                                    entity2.setShareTaskStatus(pushData2.optInt("source_style", 4));
                                    entity2.setShareTaskPushId(arrayPassivePush.getJSONObject(i).optString("interpush_id"));
                                    entity2.setShareTaskIsShare(arrayPassivePush.getJSONObject(i).optString("target_is_share"));
                                    entity2.setShareTaskType(type);
                                    entity2.setShareTaskForBrowse(pushData2.optString("browse_num"));
                                    entity2.setShareTaskBrowse(pushData2.optString("browse_num"));
                                    entity2.setShareTaskHead(pushData2.optString("head_portrait"));
                                    entity2.setShareTaskId(pushData2.optString("source_id"));
                                    entity2.setShareTaskUserId(pushData2.optString("user_id"));
                                    entity2.setShareTaskName(pushData2.optString("user_name"));
                                    entity2.setShareTaskTime(arrayPassivePush.optJSONObject(j).optString("target_share_time"));
                                    entity2.setShareTaskDate(arrayPassivePush.optJSONObject(j).optString("created_at"));

                                    if (pushData2.optString("cover").contains("[")) {
                                        try {
                                            entity2.setShareTaskImage(JSON.parseArray(pushData2.optString("cover")).get(0).toString().replace("\"", "").trim());
                                        } catch (Exception e) {
                                            entity2.setShareTaskImage("");
                                        }
                                    } else {
                                        try {
                                            entity2.setShareTaskImage(pushData2.optString("cover").replace("\"", "").trim());
                                        } catch (Exception e) {
                                            entity2.setShareTaskImage("");
                                        }
                                    }


                                    if (pushData2.optString("user_id").equals(XPreferencesUtils.get("user_id", ""))) {
                                        recordEntity.setShareRecordOne(entity1);
                                        recordEntity.setShareRecordTwo(entity2);
                                    } else {
                                        recordEntity.setShareRecordOne(entity2);
                                        recordEntity.setShareRecordTwo(entity1);
                                    }
                                    listData.add(recordEntity);
                                }
                            }
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                listData = JsonFormatUtils.invertOrderList(listData);

                                if (countPage == 1) {
                                    if (listData.size() > 0) {
                                        _no_data.setVisibility(View.GONE);
                                    } else {
                                        _no_data.setVisibility(View.VISIBLE);
                                    }

                                    mAdapter = new ShareRecordAdapter(mActivity, listData);
                                    mListView.setAdapter(mAdapter);
                                    XScrollViewUtils.setListViewHeightBasedOnChildren(mListView);
                                } else {
                                    mAdapter.notifyDataSetChanged();
                                    XScrollViewUtils.setListViewHeightBasedOnChildren(mListView);
                                }

                                mAdapter.setOnItemMessageListener(new ShareRecordAdapter.onItemMessageListener() {
                                    @Override
                                    public void onItemMessageClick(int position) {
                                        if (listData.get(position).getShareRecordOne().getShareTaskIsShare().equals("0")) {
                                            //ToastUtils.showCenter(listData.get(position).getShareRecordOne().getShareTaskUserId() + "+" + listData.get(position).getShareRecordOne().getShareTaskTitle());
                                            getPassiveShare(listData.get(position).getShareRecordOne().getShareTaskUserId(), listData.get(position).getShareRecordTwo().getShareTaskName(), position);
                                        }
                                    }
                                });

                                mAdapter.setOnItemDetailsListener(new ShareRecordAdapter.onItemDetailsListener() {
                                    @Override
                                    public void onItemDetailsClick(String type, int position) {
                                        String _type_id, _user_id;
                                        if (type.equals("1")) {
                                            _type_id = listData.get(position).getShareRecordOne().getShareTaskId();
                                            _user_id = listData.get(position).getShareRecordOne().getShareTaskUserId();
                                            toDetails(listData.get(position).getShareRecordOne().getShareTaskStatus(), _type_id, _user_id, listData.get(position).getShareRecordOne().getShareTaskTitle(), listData.get(position).getShareRecordOne().getShareTaskImage());
                                        } else {
                                            _type_id = listData.get(position).getShareRecordTwo().getShareTaskId();
                                            _user_id = listData.get(position).getShareRecordTwo().getShareTaskUserId();
                                            toDetails(listData.get(position).getShareRecordTwo().getShareTaskStatus(), _type_id, _user_id, listData.get(position).getShareRecordTwo().getShareTaskTitle(), listData.get(position).getShareRecordTwo().getShareTaskImage());
                                        }
                                    }
                                });
                                loadOver();
                            }
                        });

                    } else {
                        loadOver();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    loadOver();
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("error:" + error);
                loadOver();
            }
        });
    }


    /**
     * 查看分享内容详情
     */
    private void toDetails(int status, String typeID, String userID, String title, String imageUrl) {
        switch (status) {
            //1,文章，2个人网页，3营销网页， 4名片
            case 1:
                startActivity(MessageWebActivity.class, "", ServerApi.ARTICLE_DETAILS_URL + typeID + "/" + userID + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                break;
            case 2:
                Bundle bundle = new Bundle();
                bundle.putString("id", typeID);
                bundle.putString("vUrl", ServerApi.PERSONAL_DETAILS_URL2 + typeID + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                bundle.putString("share_title", title);
                bundle.putString("share_des", "");
                bundle.putString("share_imageUrl", imageUrl);

                startActivity(PersonalWebDetailsActivity.class, bundle);
                //startActivity(PersonalWebDetailsActivity.class, typeID, ServerApi.PERSONAL_DETAILS_URL2 + typeID + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                break;
            case 3:
                startActivity(MessageWebActivity.class, "", ServerApi.MARKETING_DETAILS_URL + typeID + "?token=" + XPreferencesUtils.get("token", "") + "&platform=android");
                break;
            case 4:
                startActivity(CardClipDetailActivity.class, userID);
                break;
        }
    }

    /**
     * 加载完成
     */
    private void loadOver() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                _footer_pro.setVisibility(View.GONE);
//                _footer_tv.setText("点击加载更多");
                isLoading = false;
                mListView.removeFooterView(mFooterView);
                //XScrollViewUtils.setListViewHeightBasedOnChildren(mListView);
            }
        });
    }

    /**
     * 消息提醒
     */
    private void getPassiveShare(String to_user_id, String user_name, final int position) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("to_user_id", to_user_id);
        map.put("user_name", user_name);
        ElectLogic.Instance(mActivity).getShareMessageApi(map, true, "提醒中...", new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    // hideDialog();
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    if (json.getString("status").equals("0")) {
                        showToast("提醒成功");
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listData.get(position).getShareRecordOne().setShareTaskIsShare("1");
                                if (mAdapter != null) {
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        });

                    } else {
                        showToast("提醒失败," + json.getString("message"));

                    }
                } catch (Exception e) {
                    LogUtils.debug("e:" + e);
                    showToast("提醒失败");
                }
            }

            @Override
            public void onFailed(String error) {
                LogUtils.debug("error:" + error);
                showToast("提醒失败");
            }
        });
    }

    private void showToast(final String msg) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(msg);
            }
        });
    }

}
