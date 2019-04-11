package com.huimaibao.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseFragment;
import com.huimaibao.app.fragment.finds.act.FindsAddActivity;
import com.huimaibao.app.fragment.finds.act.FindsMSGActivity;
import com.huimaibao.app.fragment.finds.act.FindsMyActivity;
import com.huimaibao.app.fragment.finds.adapter.FindsAdapter;
import com.huimaibao.app.fragment.finds.entity.FindsEntity;
import com.huimaibao.app.fragment.finds.server.FindsLogic;
import com.huimaibao.app.fragment.mine.server.CardClipLogic;
import com.huimaibao.app.http.ResultBack;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.pickers.util.LogUtils;
import com.youth.xframe.utils.XFrameAnimation;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 发现
 */
public class FindsFragment extends BaseFragment {
    private Activity mActivity = this.getActivity();

    private View _needOffsetView;

    //我的动态
    private TextView _finds_my_btn;
    //新消息
    private View _view_top;
    private LinearLayout _finds_msg_ll;
    private CircleImageView _finds_msg_head;
    private TextView _finds_msg_content;
    //添加动态
    private ImageView _finds_add_btn;
    //数据集合
    private LinearLayout _no_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;

    private FindsAdapter mAdapter;
    private List<FindsEntity> listData;
    private List<String> listImage;

    private int countPage = 1, praise_num = 0;


    private XFrameAnimation xFAFocus, xFAPraise;

    private int[] focusRes = {R.drawable.finds_list_top_focus, R.drawable.finds_list_top_focus_1, R.drawable.finds_list_top_focus_2,
            R.drawable.finds_list_top_focus_3, R.drawable.finds_list_top_focus_4, R.drawable.finds_list_top_focus_5
            , R.drawable.finds_list_top_focus_6, R.drawable.finds_list_top_focus_7, R.drawable.finds_list_top_focus_8
            , R.drawable.finds_list_top_focus_9, R.drawable.finds_list_top_focus_10, R.drawable.finds_list_top_focus_11, R.drawable.finds_list_top_focus_12
            , R.drawable.finds_list_top_focus_13, R.drawable.finds_list_top_focus_14, R.drawable.finds_list_top_focus_15
            , R.drawable.finds_list_top_focus_16, R.drawable.finds_list_top_focus_17, R.drawable.finds_list_top_focus_18
            , R.drawable.finds_list_top_focus_19, R.drawable.finds_list_top_focus_20, R.drawable.finds_list_top_focus_22, R.drawable.finds_list_top_focus_23
            , R.drawable.finds_list_top_focus_24, R.drawable.finds_list_top_focus_25, R.drawable.finds_list_top_focus_26};

    private int[] praiseRes = {R.drawable.finds_list_praise, R.drawable.finds_list_praise_1, R.drawable.finds_list_praise_2, R.drawable.finds_list_praise_3
            , R.drawable.finds_list_praise_4, R.drawable.finds_list_praise_5, R.drawable.finds_list_praise_6, R.drawable.finds_list_praise_7
            , R.drawable.finds_list_praise_8, R.drawable.finds_list_praise_9, R.drawable.finds_list_praise_10, R.drawable.finds_list_praise_11
            , R.drawable.finds_list_praise_12, R.drawable.finds_list_praise_13, R.drawable.finds_list_praise_14, R.drawable.finds_list_praise_15
            , R.drawable.finds_list_praise_16, R.drawable.finds_list_praise_17, R.drawable.finds_list_praise_18, R.drawable.finds_list_praise_19
            , R.drawable.finds_list_praise_20, R.drawable.finds_list_praise_21, R.drawable.finds_list_praise_22, R.drawable.finds_list_praise_23
            , R.drawable.finds_list_praise_24, R.drawable.finds_list_praise_25, R.drawable.finds_list_praise_26, R.drawable.finds_list_praise_27
            , R.drawable.finds_list_praise_28, R.drawable.finds_list_praise_29};


    private String[] mUrls = new String[]{
            "http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg",
            "http://img3.fengniao.com/forum/attachpics/537/165/21472986.jpg",
            "http://d.hiphotos.baidu.com/image/h%3D200/sign=ea218b2c5566d01661199928a729d498/a08b87d6277f9e2fd4f215e91830e924b999f308.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3445377427,2645691367&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2644422079,4250545639&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1444023808,3753293381&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=882039601,2636712663&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=4119861953,350096499&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2437456944,1135705439&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3251359643,4211266111&fm=21&gp=0.jpg",
            "http://img4.duitang.com/uploads/item/201506/11/20150611000809_yFe5Z.jpeg",
            "http://img5.imgtn.bdimg.com/it/u=1717647885,4193212272&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2024625579,507531332&fm=21&gp=0.jpg"};


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finds, container, false);
        mActivity = this.getActivity();
        initView(view);
        return view;
    }

    private void initView(View v) {
        _needOffsetView = v.findViewById(R.id.fake_status_bar);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, _needOffsetView);

        //我的动态
        _finds_my_btn = v.findViewById(R.id.finds_my_btn);

        //新消息
        _view_top = View.inflate(mActivity, R.layout.fragment_finds_top, null);
        _finds_msg_ll = _view_top.findViewById(R.id.finds_msg_ll);
        _finds_msg_head = _view_top.findViewById(R.id.finds_msg_head);
        _finds_msg_content = _view_top.findViewById(R.id.finds_msg_content);

        //添加动态
        _finds_add_btn = v.findViewById(R.id.finds_add_btn);

        //数据集合
        mSwipeRefreshView = v.findViewById(R.id.list_swipe_value);
        mListView = v.findViewById(R.id.list_pull_value);
        _no_data = v.findViewById(R.id.list_no_data);
        _no_data.setVisibility(View.GONE);


        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);

        // 手动调用,通知系统去测量
         mSwipeRefreshView.measure(0, 0);
         mSwipeRefreshView.setRefreshing(true);
         mSwipeRefreshView.setItemCount(5);

        initEvent();


    }


    private void initEvent() {
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });


        // 设置上拉加载更多
        mSwipeRefreshView.setOnLoadMoreListener(new XSwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
    }

    @Override
    protected void initData() {
        getNewMSGData();
        countPage = 1;
        getDYListData(countPage, false);
    }

    private void loadMoreData() {
        countPage++;
        getDYListData(countPage, false);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        XStatusBar.setTranslucentForImageViewInFragment(mActivity, _needOffsetView);
    }

    /**
     * 点击事件
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //我的动态
        _finds_my_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FindsMyActivity.class, "我的动态");
            }
        });
        //添加动态
        _finds_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FindsAddActivity.class, "发布动态");
            }
        });
        //新消息
        _finds_msg_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FindsMSGActivity.class, "新消息");
            }
        });

    }


    /**
     * 获取动态
     */
    private void getDYListData(final int page, boolean isShow) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        map.put("myConcern", "1");
        map.put("page", page);
        map.put("pageSize", "10");
        FindsLogic.Instance(mActivity).getDYListApi(map, isShow, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("finds:" + json.toString());
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
                        JSONArray array = new JSONArray(json.getJSONObject("data").getString("list"));
                        if (page == 1) {
                            listData = new ArrayList<>();
                        } else {
                            if (array.length() == 0) {
                                showToast("没有数据了");
                            }
                        }

                        for (int i = 0; i < array.length(); i++) {
                            FindsEntity entity = new FindsEntity();
                            entity.setFindsId(array.getJSONObject(i).optString("dynamic_id"));
                            entity.setFindsCommentsNum(array.getJSONObject(i).optString("comment_times"));
                            entity.setFindsContent(array.getJSONObject(i).optString("content"));
                            entity.setFindsIsFocus(array.getJSONObject(i).optString("concern"));
                            entity.setFindsCardId(array.getJSONObject(i).optString("concern"));
                            entity.setFindsIsPraise(array.getJSONObject(i).optString("praise"));
                            entity.setFindsPraiseNum(array.getJSONObject(i).optString("praise_number"));
                            entity.setFindsTime(array.getJSONObject(i).optString("created_at"));
                            entity.setFindsUserId(array.getJSONObject(i).optString("user_id"));
                            entity.setFindsUserHead(array.getJSONObject(i).optString("head_picture"));
                            entity.setFindsUserName(array.getJSONObject(i).optString("user_name"));

                            mUrls = new String[]{};
                            try {
                                mUrls = array.getJSONObject(i).optString("image_path").split(",");
                            } catch (Exception e) {

                            }

                            listImage = new ArrayList<>();
                            for (int j = 0; j < mUrls.length; j++) {
                                listImage.add(mUrls[j]);
                            }
                            entity.setFindsImageList(listImage);

                            listData.add(entity);
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (page == 1) {
                                    if (listData.size() == 0) {
                                        _no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        _no_data.setVisibility(View.GONE);
                                    }
                                    mAdapter = new FindsAdapter(mActivity, "发现", listData);
                                    mListView.setAdapter(mAdapter);

                                    // 加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }


                                    mAdapter.setOnItemFocusClickListener(new FindsAdapter.onItemFocusClickListener() {
                                        @Override
                                        public void onItemFocusClick(int position) {
                                            listData.get(position).setFindsIsFocus("1");
                                            mAdapter.notifyDataSetChanged();
                                            getCardAdd(listData.get(position).getFindsCardId());
                                        }
                                    });

                                    mAdapter.setOnItemPraiseClickListener(new FindsAdapter.onItemPraiseClickListener() {
                                        @Override
                                        public void onItemPraiseClick(int position) {
                                            if (listData.get(position).getFindsIsPraise().equals("0")) {
                                                praise_num = Integer.parseInt(listData.get(position).getFindsPraiseNum()) + 1;
                                                listData.get(position).setFindsIsPraise("1");
                                                listData.get(position).setFindsPraiseNum(praise_num + "");
                                            } else {
                                                praise_num = Integer.parseInt(listData.get(position).getFindsPraiseNum()) - 1;
                                                listData.get(position).setFindsIsPraise("0");
                                                listData.get(position).setFindsPraiseNum(praise_num + "");
                                            }

                                            mAdapter.notifyDataSetChanged();
                                            getDYPraiseData(listData.get(position).getFindsId(), "");
                                        }
                                    });

                                } else {

                                    if (mAdapter != null) {
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        mAdapter = new FindsAdapter(mActivity, "发现", listData);
                                        mListView.setAdapter(mAdapter);
                                    }
                                    // 加载完数据设置为不刷新状态，将下拉进度收起来
                                    if (mSwipeRefreshView.isRefreshing()) {
                                        mSwipeRefreshView.setRefreshing(false);
                                    }
                                }
                            }
                        });

                    } else {
                        showToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
            }
        });
    }

    /**
     * 获取新消息
     */
    private void getNewMSGData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        FindsLogic.Instance(mActivity).getNewMSGApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("finds:" + json);
                    if (json.getString("status").equals("0")) {
                        final JSONObject data = new JSONObject(json.getString("data"));
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!data.optString("count", "0").equals("0")) {
                                    ImageLoaderManager.loadImage(data.optString("head_picture"), _finds_msg_head);
                                    _finds_msg_content.setText(data.optString("user_name") + "等" + data.optString("count", "0") + "人回复了你");
                                    //添加新消息
                                    mListView.addHeaderView(_view_top);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailed(String error) {
            }
        });
    }


    /**
     * 点赞动态
     */
    private void getDYPraiseData(String dynamic_id, final String position) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("dynamic_id", dynamic_id);
        map.put("user_id", XPreferencesUtils.get("user_id", ""));
        FindsLogic.Instance(mActivity).getDYPraiseApi(map, false, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new JSONObject(object.toString());
                    LogUtils.debug("json:" + json);
                    String msg = json.getString("message");
                    if (json.getString("status").equals("0")) {
//                        mActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showToast("删除成功");
//                                if (listData.size() > 0) {
//                                    listData.remove(position);
//                                    mAdapter.notifyDataSetChanged();
//                                }
//
//                            }
//                        });

                    } else {
                        //showToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //showToast("删除失败");
                }
            }

            @Override
            public void onFailed(String error) {
                //XLog.e("error:" + error);
                //showToast("删除失败");
            }
        });
    }

    /**
     * 添加收藏
     */
    private void getCardAdd(String card_id_value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("card_id", card_id_value);
        CardClipLogic.Instance(mActivity).getCardClipAddApi(map, true, new ResultBack() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject json = new org.json.JSONObject(object.toString());
                    //XLog.d("card:" + json);
                    if (json.getString("status").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // showToast("收藏成功");

                            }
                        });
                    } else {
                        //showToast("收藏失败");
                    }
                } catch (Exception e) {
                    // showToast("收藏失败");
                }
            }

            @Override
            public void onFailed(String error) {
                //showToast("收藏失败");
            }
        });
    }


    /***/
    public void showToast(final String msg) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                XToast.normal(msg);
            }
        });
    }


}
