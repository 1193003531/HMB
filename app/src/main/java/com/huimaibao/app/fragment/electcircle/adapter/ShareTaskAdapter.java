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
import com.huimaibao.app.fragment.electcircle.entity.ShareTaskEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

/**分享任务适配器*/
public class ShareTaskAdapter extends BaseAdapter {
    private Context context;
    public List<ShareTaskEntity> list;
    public static final int ITEM_CARD = 0;
    public static final int ITEM_OTHER = 1;

    public ShareTaskAdapter(Context context, List<ShareTaskEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ShareTaskEntity getItem(int position) {
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
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        //判断依据
        return list.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        //布局个数
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ShareTaskEntity item = getItem(position);
        ViewHolderCard mHolderCard = null;
        ViewHolderOther mHolderOther = null;

        if (convertView == null) {
            switch (type) {
                case ITEM_CARD:
                    convertView = LayoutInflater.from(context).inflate(R.layout.act_elect_share_card_item, null);
                    mHolderCard = new ViewHolderCard();
                    mHolderCard._item_head = convertView.findViewById(R.id.elect_share_card_item_head);
                    mHolderCard._item_name = convertView.findViewById(R.id.elect_share_card_item_name);
                    mHolderCard._item_share_btn = convertView.findViewById(R.id.elect_share_card_item_share);

                    mHolderCard._item_card_ll = convertView.findViewById(R.id.elect_share_card_item_user);

                    mHolderCard._item_title = convertView.findViewById(R.id.elect_share_card_item_title);
                    mHolderCard._item_jobs = convertView.findViewById(R.id.elect_share_card_item_jobs);
                    mHolderCard._item_time = convertView.findViewById(R.id.elect_share_card_item_time);
                    mHolderCard._item_browse = convertView.findViewById(R.id.elect_share_card_item_browse);

                    convertView.setTag(mHolderCard);
                    break;
                case ITEM_OTHER:
                    mHolderOther = new ViewHolderOther();
                    convertView = LayoutInflater.from(context).inflate(R.layout.act_elect_share_other_item, null);

                    mHolderOther._item_head = convertView.findViewById(R.id.elect_share_other_item_head);
                    mHolderOther._item_name = convertView.findViewById(R.id.elect_share_other_item_name);
                    mHolderOther._item_share_btn = convertView.findViewById(R.id.elect_share_other_item_share);

                    mHolderOther._item_other_iv = convertView.findViewById(R.id.elect_share_other_item_image);

                    mHolderOther._item_title = convertView.findViewById(R.id.elect_share_other_item_title);
                    mHolderOther._item_time = convertView.findViewById(R.id.elect_share_other_item_time);
                    mHolderOther._item_browse = convertView.findViewById(R.id.elect_share_other_item_browse);
                    convertView.setTag(mHolderOther);
                    break;
            }
        } else {
            switch (type) {
                case ITEM_CARD:
                    mHolderCard = (ViewHolderCard) convertView.getTag();
                    break;
                case ITEM_OTHER:
                    mHolderOther = (ViewHolderOther) convertView.getTag();
                    break;
            }
        }


        switch (type) {
            case ITEM_CARD:
                ImageLoaderManager.loadImage(item.getShareTaskHead(), mHolderCard._item_head, R.drawable.ic_default);
                mHolderCard._item_name.setText(item.getShareTaskName());
                mHolderCard._item_jobs.setText(item.getShareTaskJobs());

                mHolderCard._item_title.setText(item.getShareTaskTitle());
                mHolderCard._item_time.setText(XTimeUtils.getTimeRange(item.getShareTaskTime()));
                //"浏览 " + item.getShareTaskBrowse()
                mHolderCard._item_browse.setText("");

                setTopView(mHolderCard._item_card_ll, item.getShareTaskStyle(), item.getShareTaskHead(), item.getShareTaskName(), item.getShareTaskJobs(), item.getShareTaskCompany(), item.getShareTaskPhone(), item.getShareTaskWechat(), item.getShareTaskAddr());

                mHolderCard._item_share_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemShareListener.onItemShareClick(position);
                    }
                });
                break;
            case ITEM_OTHER:
                ImageLoaderManager.loadImage(item.getShareTaskHead(), mHolderOther._item_head, R.drawable.ic_default);
                mHolderOther._item_name.setText(item.getShareTaskName());

                mHolderOther._item_title.setText(item.getShareTaskTitle());
                mHolderOther._item_time.setText(XTimeUtils.getTimeRange(item.getShareTaskTime()));
                mHolderOther._item_browse.setText("");

                ImageLoaderManager.loadImage(item.getShareTaskHead(), mHolderOther._item_head, R.drawable.ic_default);
                mHolderOther._item_share_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemShareListener.onItemShareClick(position);
                    }
                });
                break;
        }


        return convertView;
    }

    static class ViewHolderCard {
        CircleImageView _item_head;
        TextView _item_name;
        ImageView _item_share_btn;

        LinearLayout _item_card_ll;
        TextView _item_title, _item_jobs, _item_time, _item_browse;

    }

    static class ViewHolderOther {
        CircleImageView _item_head;
        TextView _item_name;
        ImageView _item_share_btn;

        ImageView _item_other_iv;
        TextView _item_title, _item_time, _item_browse;

    }

    /**
     * 点击分享监听接口
     */
    public interface onItemShareListener {
        void onItemShareClick(int position);
    }

    private onItemShareListener mOnItemShareListener;

    public void setOnItemShareListener(onItemShareListener mOnItemShareListener) {
        this.mOnItemShareListener = mOnItemShareListener;
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

}