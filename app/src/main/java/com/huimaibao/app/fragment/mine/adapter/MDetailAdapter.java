package com.huimaibao.app.fragment.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.mine.entity.MDetailEntity;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.utils.XTimeUtils;

import java.util.List;

public class MDetailAdapter extends BaseAdapter {
    private Context context;
    public List<MDetailEntity> ilList;


    public MDetailAdapter(Context context, List<MDetailEntity> ilList) {
        this.context = context;
        this.ilList = ilList;
    }

    @Override
    public int getCount() {
        return ilList == null ? 0 : ilList.size();
    }

    @Override
    public MDetailEntity getItem(int position) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_mine_money_detail_item, null);
            mHolder = new ViewHolder();
            mHolder._item_type = view.findViewById(R.id.money_detail_item_type);
            mHolder._item_money = view.findViewById(R.id.money_detail_item_money);
            mHolder._item_time = view.findViewById(R.id.money_detail_item_time);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        MDetailEntity ilItem = getItem(position);

        mHolder._item_type.setText(ilItem.getMDetailType());
        mHolder._item_time.setText(XTimeUtils.getTimeRange(ilItem.getMDetailTime()));
        if (XStringUtils.m1(ilItem.getMDetailMoney()) > 0) {
            mHolder._item_money.setText("+" + ilItem.getMDetailMoney());
        } else {
            mHolder._item_money.setText("-" + ilItem.getMDetailMoney());
        }

        return view;
    }

    static class ViewHolder {
        TextView _item_type, _item_money, _item_time;
    }

}