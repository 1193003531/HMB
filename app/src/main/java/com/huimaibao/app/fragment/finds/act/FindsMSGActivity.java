package com.huimaibao.app.fragment.finds.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.finds.adapter.FindsAdapter;
import com.huimaibao.app.fragment.finds.adapter.FindsMSGAdapter;
import com.huimaibao.app.fragment.finds.entity.FindsEntity;
import com.huimaibao.app.fragment.finds.entity.FindsMSGEntity;
import com.huimaibao.app.utils.ToastUtils;
import com.youth.xframe.utils.XPreferencesUtils;
import com.youth.xframe.widget.XSwipeRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * 新消息，消息记录
 */
public class FindsMSGActivity extends BaseActivity {

    private String mType = "";
    //title,clear
    private TextView _top_title, _top_clear;
    private RelativeLayout _top_msg_rl;


    //数据集合
    private LinearLayout _no_data;
    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;

    private FindsMSGAdapter mAdapter;
    private List<FindsMSGEntity> listData;

    private int countPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_finds_list_my);
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        _top_title = findViewById(R.id.finds_list_title);
        _top_clear = findViewById(R.id.finds_clear_btn);
        _top_msg_rl = findViewById(R.id.finds_list_top_msg_rl);

        _top_msg_rl.setVisibility(View.GONE);
        _top_title.setText(mType);
        if (mType.equals("新消息")) {
            _top_clear.setVisibility(View.INVISIBLE);
        } else {
            _top_clear.setVisibility(View.VISIBLE);
        }

        //数据集合
        mSwipeRefreshView = findViewById(R.id.list_swipe_value);
        mListView = findViewById(R.id.list_pull_value);
        _no_data = findViewById(R.id.list_no_data);
        _no_data.setVisibility(View.GONE);


        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);

        // 手动调用,通知系统去测量
        mSwipeRefreshView.measure(0, 0);
        mSwipeRefreshView.setRefreshing(true);
        mSwipeRefreshView.setItemCount(5);

        initEvent();
        initData();
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

    private void initData() {
        countPage = 1;
        listData = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            FindsMSGEntity entity = new FindsMSGEntity();
            entity.setFindsId(i + "");
            entity.setFindsContent("土耳其真是霸道：埃尔多安就这么决定了，俄罗斯能接受吗?" + i);
            entity.setFindsTeaching("" + i);
            entity.setFindsCommentId("0" + i);
            if (i % 2 == 0) {
                entity.setFindsImage("http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg");
            } else {
                entity.setFindsImage("");
            }

            entity.setFindsUserHead(XPreferencesUtils.get("portrait", "") + "");
            entity.setFindsTime("2019-04-03 1" + i + ":30:00");
            entity.setFindsUserId("2" + i);
            entity.setFindsUserName(XPreferencesUtils.get("name", "") + "");


            listData.add(entity);
        }
        mAdapter = new FindsMSGAdapter(mActivity, listData);
        mListView.setAdapter(mAdapter);
        // 加载完数据设置为不加载状态，将加载进度收起来
        if (mSwipeRefreshView.isRefreshing()) {
            mSwipeRefreshView.setRefreshing(false);
        }

    }

    private void loadMoreData() {
        countPage++;
        for (int i = 1; i < 10; i++) {
            FindsMSGEntity entity = new FindsMSGEntity();
            entity.setFindsId(i + "");
            entity.setFindsContent("土耳其真是霸道：埃尔多安就这么决定了，俄罗斯能接受吗?" + i);
            entity.setFindsTeaching("1");
            entity.setFindsCommentId("0" + i);
            if (i % 2 == 0) {
                entity.setFindsImage("http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg");
            } else {
                entity.setFindsImage("");
            }

            entity.setFindsUserHead(XPreferencesUtils.get("portrait", "") + "");
            entity.setFindsTime("2019-04-03 1" + i + ":30:00");
            entity.setFindsUserId("2" + i);
            entity.setFindsUserName(XPreferencesUtils.get("name", "") + "");


            listData.add(entity);
        }

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new FindsMSGAdapter(mActivity, listData);
            mListView.setAdapter(mAdapter);
        }
        // 加载完数据设置为不加载状态，将加载进度收起来
        if (mSwipeRefreshView.isRefreshing()) {
            mSwipeRefreshView.setRefreshing(false);
        }
    }


    /**
     * 点击事件
     */
    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.finds_clear_btn:
                //清空
                ToastUtils.showCenter("清空");
                break;
        }
    }


}
