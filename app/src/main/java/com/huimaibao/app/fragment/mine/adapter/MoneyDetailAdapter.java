package com.huimaibao.app.fragment.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.mine.entity.MoneyDetailEntity;
import com.youth.xframe.utils.XRegexUtils;
import com.youth.xframe.utils.XStringUtils;

import java.util.List;

public class MoneyDetailAdapter extends BaseAdapter {
    private Context context;
    public List<MoneyDetailEntity> list;


    public MoneyDetailAdapter(Context context, List<MoneyDetailEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public MoneyDetailEntity getItem(int position) {
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
        MoneyDetailEntity item = getItem(position);

        mHolder._item_type.setText(item.getMoneyDetailName());
        mHolder._item_time.setText(item.getMoneyDetailTime());
        if (item.getMoneyDetailType().equals("1")) {
            mHolder._item_money.setText("+￥" + item.getMoneyDetailMoney());
        } else {
            mHolder._item_money.setText("-￥" + item.getMoneyDetailMoney());
        }


        return view;
    }

    static class ViewHolder {
        TextView _item_type, _item_money, _item_time;
    }

}