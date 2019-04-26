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
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

public class CardAdapter extends BaseAdapter {
    private Context context;
    public List<CardEntity> ilList;


    public CardAdapter(Context context, List<CardEntity> ilList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_mine_card_clip_item, null);
            mHolder = new ViewHolder();

            mHolder._item_head = view.findViewById(R.id.mine_card_clip_item_head);
            mHolder._item_name = view.findViewById(R.id.mine_card_clip_item_name);
            mHolder._item_content = view.findViewById(R.id.mine_card_clip_item_content);

            mHolder._item_fws_iv = view.findViewById(R.id.mine_card_clip_item_fws_iv);
            mHolder._item_rzyh_iv = view.findViewById(R.id.mine_card_clip_item_rzyh_iv);
            mHolder._item_vip_iv = view.findViewById(R.id.mine_card_clip_item_vip_iv);
            mHolder._item_shop_iv = view.findViewById(R.id.mine_card_clip_item_shop_iv);
            mHolder._item_htdr_iv = view.findViewById(R.id.mine_card_clip_item_ht_iv);

            mHolder._item_fws_iv.setVisibility(View.GONE);
            mHolder._item_rzyh_iv.setVisibility(View.GONE);
            mHolder._item_vip_iv.setVisibility(View.GONE);
            mHolder._item_shop_iv.setVisibility(View.GONE);
            mHolder._item_htdr_iv.setVisibility(View.GONE);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        CardEntity ilItem = getItem(position);
        mHolder._item_vip_iv.setVisibility(View.VISIBLE);
        if (ilItem.getCardIsVip().equals("1")) {
            mHolder._item_vip_iv.setImageResource(R.drawable.mine_vip_1_icon);
        } else if (ilItem.getCardIsVip().equals("2")) {
            mHolder._item_vip_iv.setImageResource(R.drawable.mine_vip_1_icon);
        } else if (ilItem.getCardIsVip().equals("3")) {
            mHolder._item_vip_iv.setImageResource(R.drawable.mine_vip_2_icon);
        } else if (ilItem.getCardIsVip().equals("4")) {
            mHolder._item_vip_iv.setImageResource(R.drawable.mine_vip_2_icon);
        } else {
            mHolder._item_vip_iv.setVisibility(View.GONE);
        }


        ImageLoaderManager.loadImage(ilItem.getCardHead(), mHolder._item_head, R.drawable.ic_launcher);
        mHolder._item_name.setText(XEmptyUtils.isSpace(ilItem.getCardName()) ? "" : ilItem.getCardName());
        mHolder._item_content.setText((XEmptyUtils.isSpace(ilItem.getCardJobs()) ? "" : ilItem.getCardJobs()) + "/" + (XEmptyUtils.isSpace(ilItem.getCardCompany()) ? "" : ilItem.getCardCompany()));

        return view;
    }

    static class ViewHolder {
        CircleImageView _item_head;
        //服务商，认证用户，会员，商家，互推达人
        ImageView _item_fws_iv, _item_rzyh_iv, _item_vip_iv, _item_shop_iv, _item_htdr_iv;
        TextView _item_name, _item_content;
    }


}