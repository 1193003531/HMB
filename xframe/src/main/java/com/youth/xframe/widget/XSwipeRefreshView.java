package com.youth.xframe.widget;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.youth.xframe.R;

/**
 * 自定义View继承SwipeRefreshLayout，添加上拉加载更多的布局属性,添加对RecyclerView的支持
 */

public class XSwipeRefreshView extends SwipeRefreshLayout {

    private int mScaledTouchSlop;
    private View mFooterView;
    private TextView mFooterTv;
    private ProgressBar mFooterPro;
    private GifView mFooterGif;
    private ListView mListView;
    private OnLoadMoreListener mListener;//上拉加载
    private OnScrollListener onScrollListener;

    /**
     * 正在加载状态
     */
    private boolean isLoading;
    private RecyclerView mRecyclerView;
    private int mItemCount = 0;

    private boolean isHidLoaded;

    /**
     * 滑动到最下面时的上拉操作
     */
    private int mTouchSlop;
    private boolean isActionDown;

    public XSwipeRefreshView(Context context) {
        super(context);
    }

    public XSwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充底部加载布局
        mFooterView = View.inflate(context, R.layout.view_footer, null);
        mFooterTv = mFooterView.findViewById(R.id.load_progress_tv);
        mFooterPro = mFooterView.findViewById(R.id.load_progress);
        mFooterGif = mFooterView.findViewById(R.id.load_gif);
        mFooterGif.setMovieResource(R.raw.load_gif_icon);

        // 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        //mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScaledTouchSlop = 200;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 获取ListView,设置ListView的布局位置
        if (mListView == null || mRecyclerView == null) {
            // 判断容器有多少个孩子
            if (getChildCount() > 0) {
                // 判断第一个孩子是不是ListView
                if (getChildAt(0) instanceof ListView) {
                    // 创建ListView对象
                    mListView = (ListView) getChildAt(0);

                    // 设置ListView的滑动监听
                    setListViewOnScroll();
                } else if (getChildAt(0) instanceof RecyclerView) {
                    // 创建ListView对象
                    mRecyclerView = (RecyclerView) getChildAt(0);

                    // 设置RecyclerView的滑动监听
                    setRecyclerViewOnScroll();
                }
            }
        }
    }


    /**
     * 在分发事件的时候处理子控件的触摸事件
     */
    private float mDownY = 0, mUpY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 移动的起点
                mDownY = ev.getY();
                isActionDown = true;
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动的终点距离
                mUpY = ev.getY();
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
                break;
            case MotionEvent.ACTION_UP:
                // 移动的终点
                // mUpY = ev.getY();
                // 抬起
                //mUpY = ev.getY();
                //XLog.e("mDownY:" + mDownY + " mUpY:" + mUpY + " (mDownY - mUpY):" + (mDownY - mUpY) + " mScaledTouchSlop:" + mScaledTouchSlop);
                isActionDown = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断是否满足加载更多条件
     */
    private boolean canLoadMore() {
        // 1. 是上拉状态
        //XLog.e("mDownY:" + mDownY + " mUpY:" + mUpY + " (mDownY - mUpY):" + (mDownY - mUpY) + " mScaledTouchSlop:" + mScaledTouchSlop);
        boolean condition1 = (mDownY - mUpY) >= mScaledTouchSlop;
        if (condition1) {
            //是上拉状态");
            //mFooterPro.setVisibility(VISIBLE);
            //mFooterTv.setText("上拉加载");
        }

        // 2. 当前页面可见的item是最后一个条目,一般最后一个条目位置需要大于第一页的数据长度
        boolean condition2 = false;
        if (mListView != null && mListView.getAdapter() != null) {

            if (mItemCount > 0) {
                if (mListView.getAdapter().getCount() < mItemCount) {
                    // 第一页未满，禁止下拉
                    condition2 = false;
                } else {
                    condition2 = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
                }
            } else {
                // 未设置数据长度，则默认第一页数据不满时也可以上拉
                condition2 = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
            }

        }

        if (condition2) {
            //是最后一个条目");
            //mFooterPro.setVisibility(GONE);
            //mFooterTv.setText("已加载完成");
        }
        // 3. 正在加载状态
        boolean condition3 = !isLoading;
        if (condition3) {
            // 不是正在加载状态");
            //mFooterPro.setVisibility(VISIBLE);
            // mFooterTv.setText("正在加载...");
        }
        return condition1 && condition2 && condition3;
    }

    public void setItemCount(int itemCount) {
        this.mItemCount = itemCount;
    }

    /**
     * 处理加载数据的逻辑
     */
    public void loadData() {
        // System.out.println("加载数据...");
        if (mListener != null) {
            // 设置加载状态，让布局显示出来
            setLoading(true);
            mListener.onLoadMore();
        }

    }

    /**
     * 设置加载状态，是否加载传入boolean值进行判断
     *
     * @param loading
     */
    public void setLoading(boolean loading) {
        // 修改当前的状态
        isLoading = loading;
        if (isLoading) {
            // 显示布局
            mListView.addFooterView(mFooterView);
        } else {
            // 隐藏布局
            mListView.removeFooterView(mFooterView);
            // 重置滑动的坐标
            mDownY = 0;
            mUpY = 0;

        }
    }

    /**
     * @param loaded 是否加载
     */
    public boolean seLoaded(boolean loaded) {
        return this.isLoading = loaded;
    }


    /**
     * 设置ListView的滑动监听
     */
    private void setListViewOnScroll() {

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 移动过程中判断时候能下拉加载更多
//                if (canLoadMore()) {
//                    // 加载数据
//                    loadData();
//                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    /**
     * 设置RecyclerView的滑动监听
     */
    private void setRecyclerViewOnScroll() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    /**
     * 上拉加载的接口回调
     */

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /***/
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }

    private boolean mMeasured = false;
    private boolean mPreMeasureRefreshing = false;

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mMeasured) {
            mMeasured = true;
            setRefreshing(mPreMeasureRefreshing);
        }
    }


    @Override
    public void setRefreshing(boolean refreshing) {
        if (mMeasured) {
            super.setRefreshing(refreshing);
        } else {
            mPreMeasureRefreshing = refreshing;
        }
    }


    /***/
    @Override
    protected int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        XToast.normal("t:" + t + " l:" + l);
        if (onScrollListener != null) {
            onScrollListener.onScroll(t);
        }
    }

    /**
     * 接口对外公开
     *
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 滚动的回调接口
     *
     * @author xiaanming
     */
    public interface OnScrollListener {
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         *
         * @param scrollY
         */
        void onScroll(int scrollY);
    }


}
