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
import com.youth.xframe.utils.XFrameAnimation;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.XSwipeRefreshView;

import java.util.ArrayList;
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
        // mSwipeRefreshView.measure(0, 0);
        // mSwipeRefreshView.setRefreshing(true);
        // mSwipeRefreshView.setItemCount(5);

        initEvent();

        //添加新消息
        mListView.addHeaderView(_view_top);


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
        countPage = 1;
        listData = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            FindsEntity entity = new FindsEntity();
            entity.setFindsId(i + "");
            entity.setFindsCommentsNum("" + i);
            entity.setFindsContent("土耳其真是霸道：埃尔多安就这么决定了，俄罗斯能接受吗?" + i);
            entity.setFindsIsFocus("0");
            entity.setFindsIsPraise("0");
            entity.setFindsPraiseNum("10" + i);
            entity.setFindsUserHead(XPreferencesUtils.get("portrait", "") + "");
            entity.setFindsTime("2019-04-03 1" + i + ":30:00");
            entity.setFindsUserId("2" + i);
            entity.setFindsUserName(XPreferencesUtils.get("name", "") + "");

            listImage = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                listImage.add(mUrls[j]);
            }
            entity.setFindsImageList(listImage);

            listData.add(entity);
        }
        mAdapter = new FindsAdapter(mActivity,"发现", listData);
        mListView.setAdapter(mAdapter);
        // 加载完数据设置为不加载状态，将加载进度收起来
        //mSwipeRefreshView.setLoading(false);
        mAdapter.setOnItemFocusClickListener(new FindsAdapter.onItemFocusClickListener() {
            @Override
            public void onItemFocusClick(int position) {
                listData.get(position).setFindsIsFocus("1");
                mAdapter.notifyDataSetChanged();
            }
        });

        mAdapter.setOnItemPraiseClickListener(new FindsAdapter.onItemPraiseClickListener() {
            @Override
            public void onItemPraiseClick(int position) {
//                FindsEntity entity = new FindsEntity();
//                entity.setFindsIsPraise("1");
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
            }
        });
    }

    private void loadMoreData() {
        countPage++;
        for (int i = 1; i < 10; i++) {
            FindsEntity entity = new FindsEntity();
            entity.setFindsId(i + "");
            entity.setFindsCommentsNum("" + i);
            entity.setFindsContent("土耳其真是霸道：埃尔多安就这么决定了，俄罗斯能接受吗?" + i);
            entity.setFindsIsFocus("0");
            entity.setFindsIsPraise("0");
            entity.setFindsPraiseNum("10" + i);
            entity.setFindsUserHead(XPreferencesUtils.get("portrait", "") + "");
            entity.setFindsTime("2019-04-03 1" + i + ":30:00");
            entity.setFindsUserId("2" + i);
            entity.setFindsUserName(XPreferencesUtils.get("name", "") + "");

            listImage = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                listImage.add(mUrls[j]);
            }
            entity.setFindsImageList(listImage);

            listData.add(entity);
        }
        mAdapter = new FindsAdapter(mActivity,"发现", listData);
        mListView.setAdapter(mAdapter);
        // 加载完数据设置为不加载状态，将加载进度收起来
        // mSwipeRefreshView.setLoading(false);
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


}
