package com.huimaibao.app.fragment.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.mine.entity.CardEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

public class Card2Adapter extends BaseAdapter {
    private Context context;
    public List<CardEntity> ilList;


    public Card2Adapter(Context context, List<CardEntity> ilList) {
        this.context = context;
        this.ilList = ilList;
    }

    @Override
    public int getCount() {
        return ilList == null ? 0 : ilList.size();
    }

    @Override
    public CardEntity getItem(int position) {
        if (ilList != null && ilList.size() != 0) {
            return ilList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.act_mine_vein_treasure_giving_item, null);
            mHolder = new ViewHolder();

            mHolder._item_check_iv = view.findViewById(R.id.card_item_check);
            mHolder._item_vip_iv = view.findViewById(R.id.card_item_vip_iv);
            mHolder._item_company = view.findViewById(R.id.card_item_company);
            mHolder._item_industry = view.findViewById(R.id.card_item_industry);
            mHolder._item_name = view.findViewById(R.id.card_item_name);
            mHolder._item_jobs = view.findViewById(R.id.card_item_jobs);

            mHolder._item_state_ll = view.findViewById(R.id.card_item_state_ll);

            mHolder.mTopView = view.findViewById(R.id.card_item_top);

            mHolder._item_zs = view.findViewById(R.id.card_item_zs);
            mHolder._item_vip = view.findViewById(R.id.card_item_vip);
            mHolder._item_open = view.findViewById(R.id.card_item_open);


            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        CardEntity ilItem = getItem(position);


        if (ilItem.getCardType().equals("名片夹")) {
            if (ilItem.getCardIsShow())
                mHolder._item_check_iv.setVisibility(View.VISIBLE);
            else
                mHolder._item_check_iv.setVisibility(View.GONE);

            if (ilItem.getCardIsCheck())
                mHolder._item_check_iv.setChecked(true);
            else
                mHolder._item_check_iv.setChecked(false);

            mHolder._item_state_ll.setVisibility(View.GONE);
            if (ilItem.getCardIsVip().equals("1")) {
                mHolder._item_vip_iv.setImageResource(R.drawable.mine_vip_1_icon);
            } else if (ilItem.getCardIsVip().equals("2")) {
                mHolder._item_vip_iv.setImageResource(R.drawable.mine_vip_1_icon);
            } else if (ilItem.getCardIsVip().equals("3")) {
                mHolder._item_vip_iv.setImageResource(R.drawable.mine_vip_2_icon);
            } else if (ilItem.getCardIsVip().equals("4")) {
                mHolder._item_vip_iv.setImageResource(R.drawable.mine_vip_2_icon);
            }

        } else {
            mHolder._item_state_ll.setVisibility(View.VISIBLE);
            switch (position) {
                case 0:
                    mHolder._item_zs.setVisibility(View.VISIBLE);
                    mHolder._item_vip.setVisibility(View.GONE);
                    mHolder._item_open.setVisibility(View.GONE);
                    break;
                case 1:
                    mHolder._item_zs.setVisibility(View.GONE);
                    mHolder._item_vip.setVisibility(View.VISIBLE);
                    mHolder._item_open.setVisibility(View.GONE);
                    break;
                case 2:
                    mHolder._item_zs.setVisibility(View.GONE);
                    mHolder._item_vip.setVisibility(View.GONE);
                    mHolder._item_open.setVisibility(View.VISIBLE);
                    break;
                default:
                    mHolder._item_zs.setVisibility(View.GONE);
                    mHolder._item_vip.setVisibility(View.GONE);
                    mHolder._item_open.setVisibility(View.GONE);
                    break;
            }
        }
        setTopView(mHolder.mTopView, ilItem.getCardStyle(), ilItem.getCardHead(), ilItem.getCardName(), ilItem.getCardJobs(), ilItem.getCardCompany(), ilItem.getCardPhone(), ilItem.getCardWechat(), ilItem.getCardAddr());
        // XImage.getInstance().load(mHolder._item_image, ilItem.getCardImage());

        mHolder._item_company.setText(ilItem.getCardCompany());
        mHolder._item_industry.setText(ilItem.getCardIndustry());
        mHolder._item_jobs.setText(ilItem.getCardJobs());
        mHolder._item_name.setText(ilItem.getCardName());


        return view;
    }

    static class ViewHolder {
        CheckBox _item_check_iv;
        ImageView _item_vip_iv;
        TextView _item_time, _item_name, _item_company, _item_industry, _item_jobs;
        TextView _item_zs, _item_vip, _item_open;
        LinearLayout _item_state_ll;
        LinearLayout mTopView;
    }


    /**
     * 顶部名片
     */
    private LinearLayout mTopView;
    private View _TopView, _top_ll;
    private RoundedImagView _top_head;
    private TextView _top_name, _top_jobs, _top_company, _top_phone, _top_wechat, _top_addr;

    private void setTopView(LinearLayout mTopView, String _style_value, String _head_image, String _name_value, String _jobs_value, String _company_value, String _phone_value, String _wechat_value, String _addr_value) {
//        if (_style_value.equals("style3")) {
//            initTopView(mTopView, R.layout.act_home_card_list_1_view, R.drawable.card_1_view, _head_image, _name_value, _jobs_value, _company_value, _phone_value, _wechat_value, _addr_value);
//            setTopTextColor(R.color.color000000, R.color.color333333, R.color.color333333);
//        } else
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

//        XImage.getInstance().load(_top_head, _head_value, R.drawable.ic_default);
        ImageLoaderManager.loadImage(_head_image,_top_head,R.drawable.ic_default);
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