package com.huimaibao.app.fragment.genseralize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huimaibao.app.R;
import com.huimaibao.app.base.BaseActivity;
import com.huimaibao.app.fragment.message.adapter.MessageAdapter;
import com.huimaibao.app.fragment.message.entity.MessageEntity;
import com.youth.xframe.widget.XSwipeRefreshView;
import com.youth.xframe.widget.XToast;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的发布(分享记录)
 */
public class ReleaseShareActivity extends BaseActivity {

    private String mType = "";
    private Activity mActivity = this;

    private XSwipeRefreshView mSwipeRefreshView;
    private ListView mListView;
    private MessageAdapter mAdapter;
    private List<MessageEntity> messageList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_release_share);
        mActivity = this;
        setNeedBackGesture(true);
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("vType");
        }

        setTopTitle(mType);
        setTopLeft(true, true, false, "");
        setTopRight(false, true, false, "", null);

        mSwipeRefreshView = findViewById(R.id.swipeRefreshView);
        mListView = findViewById(R.id.list_pull_value);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                XToast.normal(mAdapter.getItem(position).getMessageName());
            }
        });


        // 不能在onCreate中设置，这个表示当前是刷新状态，如果一进来就是刷新状态，SwipeRefreshLayout会屏蔽掉下拉事件
        //swipeRefreshLayout.setRefreshing(true);

        // 设置颜色属性的时候一定要注意是引用了资源文件还是直接设置16进制的颜色，因为都是int值容易搞混
        // 设置下拉进度的背景颜色，默认就是白色的
        //mSwipeRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        mSwipeRefreshView.setColorSchemeResources(R.color.ff274ff3);

        mSwipeRefreshView.setItemCount(5);

        // 手动调用,通知系统去测量
        mSwipeRefreshView.measure(0, 0);
        mSwipeRefreshView.setRefreshing(true);

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

    private void loadMoreData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    MessageEntity msg = new MessageEntity();
                    msg.setMessageId("" + i);
                    msg.setMessageImage("http://n.sinaimg.cn/tech/crawl/93/w550h343/20181109/h6aT-hnprhzw6400508.jpg");
                    msg.setMessageName("app消息名称" + i);
                    msg.setMessageContent("消息内容消息内容消息内容消息内容消息内容消息内容消息内容消息内容消息内容" + i);
                    msg.setMessageTime("1586521562");
                    msg.setMessageType("" + i);
                    messageList.add(msg);
                }
                mAdapter.notifyDataSetChanged();
                // 加载完数据设置为不加载状态，将加载进度收起来
                mSwipeRefreshView.setLoading(false);

            }
        }, 2000);
    }


    private void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (messageList != null) {
                    messageList.clear();
                } else {
                    messageList = new ArrayList<>();
                }

                for (int i = 0; i < 5; i++) {
                    MessageEntity msg = new MessageEntity();
                    msg.setMessageId("" + i);
                    msg.setMessageImage("http://n.sinaimg.cn/tech/crawl/93/w550h343/20181109/h6aT-hnprhzw6400508.jpg");
                    msg.setMessageName("消息名称" + i);
                    msg.setMessageContent("消息内容消息内容消息内容消息内容消息内容消息内容消息内容消息内容消息内容" + i);
                    msg.setMessageTime("1586521562");
                    msg.setMessageType("" + i);
                    messageList.add(msg);
                }
                if (mAdapter == null) {
                    mAdapter = new MessageAdapter(mActivity, messageList,"");
                }
                mListView.setAdapter(mAdapter);

                // 加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        }, 2000);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

}
