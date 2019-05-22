package com.huimaibao.app.fragment.electcircle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.electcircle.entity.ShareRecordEntity;
import com.huimaibao.app.fragment.electcircle.entity.ShareTaskEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XDensityUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

/**
 * 分享记录适配器
 */
public class ShareRecordAdapter extends BaseAdapter {
    private Context context;
    public List<ShareRecordEntity> list;


    public ShareRecordAdapter(Context context, List<ShareRecordEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ShareRecordEntity getItem(int position) {
        if (list != null && list.size() != 0) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.act_elect_share_record_item, null);
            mHolder = new ViewHolder();
            mHolder._item_ll_1 = view.findViewById(R.id.elect_share_record_item_ll_1);
            mHolder._item_ll_2 = view.findViewById(R.id.elect_share_record_item_ll_2);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        ShareRecordEntity item = getItem(position);

        if (item.getShareRecordOne().getType() == 0) {
            initCardView(mHolder._item_ll_1, item.getShareRecordOne().getShareTaskType(), false, item.getShareRecordOne().getShareTaskIsShare(), item.getShareRecordOne(), position);
        } else {
            initOtherView(mHolder._item_ll_1, item.getShareRecordOne().getShareTaskType(), false, item.getShareRecordOne().getShareTaskIsShare(), item.getShareRecordOne(), position);
        }


        if (item.getShareRecordTwo().getType() == 0) {
            initCardView(mHolder._item_ll_2, item.getShareRecordTwo().getShareTaskType(), true, item.getShareRecordTwo().getShareTaskIsShare(), item.getShareRecordTwo(), position);
        } else {
            initOtherView(mHolder._item_ll_2, item.getShareRecordTwo().getShareTaskType(), true, item.getShareRecordTwo().getShareTaskIsShare(), item.getShareRecordTwo(), position);
        }


        mHolder._item_ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemDetailsListener.onItemDetailsClick("1", position);
            }
        });

        mHolder._item_ll_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemDetailsListener.onItemDetailsClick("2", position);
            }
        });


        return view;
    }

    static class ViewHolder {
        LinearLayout _item_ll_1, _item_ll_2;
    }

    /**
     * 名片
     */
    static class ViewHolderCard {
        View _card_View;
        CircleImageView _item_head;
        TextView _item_name, _item_tx, _item_num_tv, _item_num;
        ImageView _item_share_btn;

        LinearLayout _item_card_ll, _item_num_ll, _item_ll;
        TextView _item_title, _item_jobs, _item_time, _item_browse;

    }

    /**
     * 文章-网页
     */
    static class ViewHolderOther {
        View _other_View;
        CircleImageView _item_head;
        TextView _item_name, _item_tx, _item_num_tv, _item_num;
        ImageView _item_share_btn;
        LinearLayout _item_num_ll, _item_ll;
        ImageView _item_other_iv;
        TextView _item_title, _item_time, _item_browse;

    }

    /**
     * 文章-网页
     */
    private void initOtherView(LinearLayout mOtherView, String type, boolean is_initiative, String is_share, ShareTaskEntity item, final int position) {
        mOtherView.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ViewHolderOther holderOther = new ViewHolderOther();
        holderOther._other_View = LayoutInflater.from(context).inflate(R.layout.act_elect_share_other_item, null);
        holderOther._item_ll = holderOther._other_View.findViewById(R.id.elect_share_other_item_ll);
        holderOther._item_head = holderOther._other_View.findViewById(R.id.elect_share_other_item_head);
        holderOther._item_name = holderOther._other_View.findViewById(R.id.elect_share_other_item_name);
        holderOther._item_share_btn = holderOther._other_View.findViewById(R.id.elect_share_other_item_share);
        holderOther._item_tx = holderOther._other_View.findViewById(R.id.elect_share_other_item_tx);
        holderOther._item_num_tv = holderOther._other_View.findViewById(R.id.elect_share_other_item_tv);
        holderOther._item_num = holderOther._other_View.findViewById(R.id.elect_share_other_item_num);
        holderOther._item_num_ll = holderOther._other_View.findViewById(R.id.elect_share_other_item_num_ll);

        holderOther._item_other_iv = holderOther._other_View.findViewById(R.id.elect_share_other_item_image);

        holderOther._item_title = holderOther._other_View.findViewById(R.id.elect_share_other_item_title);
        holderOther._item_time = holderOther._other_View.findViewById(R.id.elect_share_other_item_time);
        holderOther._item_browse = holderOther._other_View.findViewById(R.id.elect_share_other_item_browse);

        mOtherView.addView(holderOther._other_View, params);

        holderOther._item_ll.setBackgroundResource(R.color.transparent);
        ViewGroup.LayoutParams paramsPer = holderOther._item_ll.getLayoutParams();
        paramsPer.height = XDensityUtils.dp2px(130);
        holderOther._item_ll.setLayoutParams(paramsPer);

        holderOther._item_share_btn.setVisibility(View.GONE);
        //1-相互已分享,2-相互未分享
        if (type.equals("2")) {
            //是否主动
            if (is_initiative) {
                holderOther._item_tx.setVisibility(View.GONE);
                holderOther._item_time.setVisibility(View.GONE);
                holderOther._item_browse.setVisibility(View.GONE);
                holderOther._item_title.setMaxLines(2);
            } else {
                holderOther._item_tx.setVisibility(View.VISIBLE);
                holderOther._item_time.setVisibility(View.VISIBLE);
                holderOther._item_browse.setVisibility(View.GONE);
                holderOther._item_num_ll.setVisibility(View.VISIBLE);
                holderOther._item_num_tv.setText("我为他带来");
                holderOther._item_num.setText(item.getShareTaskForBrowse());
                holderOther._item_title.setMaxLines(1);
                //0-未分享,1-已分享
                if (is_share.equals("0")) {
                    holderOther._item_tx.setBackgroundResource(R.drawable.btn_blue_r20_bg);
                    holderOther._item_tx.setText("提醒他分享");
                } else {
                    holderOther._item_tx.setBackgroundResource(R.drawable.btn_hui_r20_bg);
                    holderOther._item_tx.setText("已提醒分享");
                }
            }
        } else {
            holderOther._item_tx.setVisibility(View.GONE);
            holderOther._item_num_ll.setVisibility(View.VISIBLE);
            if (is_initiative) {
                holderOther._item_num_tv.setText("他为我带来");
                holderOther._item_num.setText(item.getShareTaskForBrowse());
            } else {
                holderOther._item_num_tv.setText("我为他带来");
                holderOther._item_num.setText(item.getShareTaskForBrowse());
            }
        }

        ImageLoaderManager.loadImage(item.getShareTaskHead(), holderOther._item_head, R.drawable.ic_default);
        ImageLoaderManager.loadImage(item.getShareTaskImage(), holderOther._item_other_iv, R.drawable.ic_default);

        holderOther._item_name.setText(XEmptyUtils.isSpace(item.getShareTaskName()) ? "未命名" : item.getShareTaskName());

        holderOther._item_title.setText(item.getShareTaskTitle());
        holderOther._item_time.setText(XTimeUtils.getTimeRange(item.getShareTaskTime()));
        holderOther._item_browse.setText("浏览" + item.getShareTaskBrowse());

        holderOther._item_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemMessageListener.onItemMessageClick(position);
            }
        });
    }


    /**
     * 名片
     */
    private void initCardView(LinearLayout mCardView, String type, boolean is_initiative, String is_share, ShareTaskEntity item, final int position) {
        mCardView.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ViewHolderCard holderCard = new ViewHolderCard();
        holderCard._card_View = LayoutInflater.from(context).inflate(R.layout.act_elect_share_card_item, null);
        holderCard._item_ll = holderCard._card_View.findViewById(R.id.elect_share_card_item_ll);
        holderCard._item_head = holderCard._card_View.findViewById(R.id.elect_share_card_item_head);
        holderCard._item_name = holderCard._card_View.findViewById(R.id.elect_share_card_item_name);
        holderCard._item_share_btn = holderCard._card_View.findViewById(R.id.elect_share_card_item_share);
        holderCard._item_tx = holderCard._card_View.findViewById(R.id.elect_share_card_item_tx);
        holderCard._item_num_tv = holderCard._card_View.findViewById(R.id.elect_share_card_item_tv);
        holderCard._item_num = holderCard._card_View.findViewById(R.id.elect_share_card_item_num);
        holderCard._item_num_ll = holderCard._card_View.findViewById(R.id.elect_share_card_item_num_ll);

        holderCard._item_card_ll = holderCard._card_View.findViewById(R.id.elect_share_card_item_user);

        holderCard._item_title = holderCard._card_View.findViewById(R.id.elect_share_card_item_title);
        holderCard._item_jobs = holderCard._card_View.findViewById(R.id.elect_share_card_item_jobs);
        holderCard._item_time = holderCard._card_View.findViewById(R.id.elect_share_card_item_time);
        holderCard._item_browse = holderCard._card_View.findViewById(R.id.elect_share_card_item_browse);
        mCardView.addView(holderCard._card_View, params);

        holderCard._item_ll.setBackgroundResource(R.color.transparent);
        ViewGroup.LayoutParams paramsPer = holderCard._item_ll.getLayoutParams();
        paramsPer.height = XDensityUtils.dp2px(130);
        holderCard._item_ll.setLayoutParams(paramsPer);

        holderCard._item_share_btn.setVisibility(View.GONE);
        //1-相互已分享,2-相互未分享
        if (type.equals("2")) {
            //是否主动
            if (is_initiative) {
                holderCard._item_tx.setVisibility(View.GONE);
                holderCard._item_time.setVisibility(View.GONE);
                holderCard._item_browse.setVisibility(View.GONE);
                holderCard._item_title.setMaxLines(2);
            } else {
                holderCard._item_tx.setVisibility(View.VISIBLE);
                holderCard._item_time.setVisibility(View.VISIBLE);
                holderCard._item_browse.setVisibility(View.GONE);
                holderCard._item_num_ll.setVisibility(View.VISIBLE);
                holderCard._item_num_tv.setText("我为他带来");
                holderCard._item_num.setText(item.getShareTaskForBrowse());
                holderCard._item_title.setMaxLines(1);
                //0-未分享,1-已分享
                if (is_share.equals("0")) {
                    holderCard._item_tx.setBackgroundResource(R.drawable.btn_blue_r20_bg);
                    holderCard._item_tx.setText("提醒他分享");
                } else {
                    holderCard._item_tx.setBackgroundResource(R.drawable.btn_hui_r20_bg);
                    holderCard._item_tx.setText("已提醒分享");
                }
            }

        } else {
            holderCard._item_tx.setVisibility(View.GONE);
            holderCard._item_num_ll.setVisibility(View.VISIBLE);
            if (is_initiative) {
                holderCard._item_num_tv.setText("他为我带来");
                holderCard._item_num.setText(item.getShareTaskForBrowse());
            } else {
                holderCard._item_num_tv.setText("我为他带来");
                holderCard._item_num.setText(item.getShareTaskForBrowse());
            }
        }

        ImageLoaderManager.loadImage(item.getShareTaskHead(), holderCard._item_head, R.drawable.ic_default);

        holderCard._item_name.setText(XEmptyUtils.isSpace(item.getShareTaskName()) ? "未命名" : item.getShareTaskName());
        holderCard._item_jobs.setText(item.getShareTaskJobs());

        holderCard._item_title.setText(item.getShareTaskTitle());
        holderCard._item_time.setText(XTimeUtils.getTimeRange(item.getShareTaskTime()));
        holderCard._item_browse.setText("浏览" + item.getShareTaskBrowse());

        setTopView(holderCard._item_card_ll, item.getShareTaskStyle(), item.getShareTaskHead(), item.getShareTaskName(), item.getShareTaskJobs(), item.getShareTaskCompany(), item.getShareTaskPhone(), item.getShareTaskWechat(), item.getShareTaskAddr());

        holderCard._item_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemMessageListener.onItemMessageClick(position);
            }
        });

    }


    /**
     * 顶部名片
     */
    private View _TopView, _top_ll;
    private RoundedImagView _top_head;
    private TextView _top_name, _top_jobs, _top_company, _top_phone, _top_wechat, _top_addr;

    private void setTopView(LinearLayout mTopView, String _style_value, String _head_image, String _name_value, String _jobs_value, String _company_value, String _phone_value, String _wechat_value, String _addr_value) {
        if (_style_value.equals("style4")) {
            initTopView(mTopView, R.layout.act_home_card_list_1_view, R.drawable.card_2_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else if (_style_value.equals("style5")) {
            initTopView(mTopView, R.layout.act_home_card_list_1_view, R.drawable.card_4_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style6")) {
            initTopView(mTopView, R.layout.act_home_card_list_1_view, R.drawable.card_3_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else if (_style_value.equals("style7")) {
            initTopView(mTopView, R.layout.act_home_card_list_2_view, R.drawable.card_11_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.white);
        } else if (_style_value.equals("style9")) {
            initTopView(mTopView, R.layout.act_home_card_list_2_view, R.drawable.card_12_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else if (_style_value.equals("style10")) {
            initTopView(mTopView, R.layout.act_home_card_list_1_view, R.drawable.card_5_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style11")) {
            initTopView(mTopView, R.layout.act_home_card_list_1_view, R.drawable.card_6_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style12")) {
            initTopView(mTopView, R.layout.act_home_card_list_2_view, R.drawable.card_9_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style13")) {
            initTopView(mTopView, R.layout.act_home_card_list_2_view, R.drawable.card_13_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style14")) {
            initTopView(mTopView, R.layout.act_home_card_list_2_view, R.drawable.card_10_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style15")) {
            initTopView(mTopView, R.layout.act_home_card_list_2_view, R.drawable.card_7_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else if (_style_value.equals("style16")) {
            initTopView(mTopView, R.layout.act_home_card_list_2_view, R.drawable.card_14_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.white, R.color.white, R.color.white);
        } else if (_style_value.equals("style17")) {
            initTopView(mTopView, R.layout.act_home_card_list_2_view, R.drawable.card_8_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        } else {
            initTopView(mTopView, R.layout.act_home_card_list_1_view, R.drawable.card_1_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
        }

    }


    /**
     * 初始化顶部控件
     */
    private void initTopView(LinearLayout mTopView, int resource, int id, String _head_image, String _name_value, String _jobs_value, String _company_value, String _phone_value, String _wechat_value, String _addr_value) {
        mTopView.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        _TopView = LayoutInflater.from(context).inflate(resource, null);

        _top_ll = _TopView.findViewById(R.id.card_list_top_ll);
        _top_head = _TopView.findViewById(R.id.card_list_top_head);
        _top_name = _TopView.findViewById(R.id.card_list_top_name);
        _top_jobs = _TopView.findViewById(R.id.card_list_top_jobs);
        _top_company = _TopView.findViewById(R.id.card_list_top_company);
        _top_phone = _TopView.findViewById(R.id.card_list_top_phone);
        _top_wechat = _TopView.findViewById(R.id.card_list_top_wechat);
        _top_addr = _TopView.findViewById(R.id.card_list_top_addr);

        mTopView.addView(_TopView, params);

        _top_ll.setBackground(context.getResources().getDrawable(id));

        ImageLoaderManager.loadImage(_head_image, _top_head, R.drawable.ic_default);

        _top_name.setText(_name_value);
        _top_jobs.setText(_jobs_value);
        _top_company.setText(_company_value);
        _top_phone.setText("电话：" + _phone_value);
        if (XEmptyUtils.isSpace(_wechat_value)) {
            _top_wechat.setVisibility(View.GONE);
        } else {
            _top_wechat.setVisibility(View.VISIBLE);
            _top_wechat.setText("微信：" + _wechat_value);
        }

        if (XEmptyUtils.isSpace(_addr_value)) {
            _top_addr.setVisibility(View.GONE);
        } else {
            _top_addr.setVisibility(View.VISIBLE);
            _top_addr.setText("地址：" + _addr_value);
        }

    }

    /**
     * 设置顶部字体颜色
     */
    private void setTopTextColor(int nid, int jid, int oid) {
        _top_name.setTextColor(context.getResources().getColor(nid));
        _top_jobs.setTextColor(context.getResources().getColor(jid));
        _top_company.setTextColor(context.getResources().getColor(oid));
        _top_phone.setTextColor(context.getResources().getColor(oid));
        _top_wechat.setTextColor(context.getResources().getColor(oid));
        _top_addr.setTextColor(context.getResources().getColor(oid));
    }

    /**
     * 消息提醒监听接口
     */
    public interface onItemMessageListener {
        void onItemMessageClick(int position);
    }

    private onItemMessageListener mOnItemMessageListener;

    public void setOnItemMessageListener(onItemMessageListener mOnItemMessageListener) {
        this.mOnItemMessageListener = mOnItemMessageListener;
    }


    /**
     * item详情监听接口
     */
    public interface onItemDetailsListener {
        void onItemDetailsClick(String type, int position);
    }

    private onItemDetailsListener mOnItemDetailsListener;

    public void setOnItemDetailsListener(onItemDetailsListener mOnItemDetailsListener) {
        this.mOnItemDetailsListener = mOnItemDetailsListener;
    }

}