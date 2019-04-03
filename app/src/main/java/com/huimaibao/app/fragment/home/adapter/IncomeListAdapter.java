package com.huimaibao.app.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.home.entity.IncomeListEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.widget.CircleImageView;

import java.util.List;

public class IncomeListAdapter extends BaseAdapter {
    private Context context;
    public List<IncomeListEntity> ilList;


    public IncomeListAdapter(Context context, List<IncomeListEntity> ilList) {
        this.context = context;
        this.ilList = ilList;
    }

    @Override
    public int getCount() {
        return ilList == null ? 0 : ilList.size();
    }

    @Override
    public IncomeListEntity getItem(int position) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_home_income_item, null);
            mHolder = new ViewHolder();
            mHolder._item_num = view.findViewById(R.id.a_h_i_item_num);
            mHolder._item_money = view.findViewById(R.id.a_h_i_item_money);
            mHolder._item_Head = view.findViewById(R.id.a_h_i_item_head);
            mHolder._item_name = view.findViewById(R.id.a_h_i_item_name);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        if (position == 96) {
            mHolder._item_num.setTextSize(11);
        } else {
            mHolder._item_num.setTextSize(16);
        }

        IncomeListEntity ilItem = getItem(position);

        ImageLoaderManager.loadImage(ilItem.getIncomeImage(), mHolder._item_Head, R.drawable.ic_launcher);
        mHolder._item_num.setText("" + (position + 4));
        mHolder._item_name.setText(ilItem.getIncomeName());
        mHolder._item_money.setText("总收入￥" + ilItem.getIncomeMoney());


        return view;
    }

    static class ViewHolder {
        CircleImageView _item_Head;
        TextView _item_num, _item_money, _item_name;
    }

}