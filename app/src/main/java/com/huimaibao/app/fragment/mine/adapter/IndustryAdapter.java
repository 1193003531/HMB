package com.huimaibao.app.fragment.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.mine.entity.IndustryEntity;

import java.util.List;

public class IndustryAdapter extends BaseAdapter {
    private Context context;
    public List<IndustryEntity> list;


    public IndustryAdapter(Context context, List<IndustryEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public IndustryEntity getItem(int position) {
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
            view = LayoutInflater.from(context).inflate(R.layout.dialog_industry_list_item, null);
            mHolder = new ViewHolder();
            mHolder._item_name = view.findViewById(R.id.dialog_industry_item_name);
            mHolder._item_iv = view.findViewById(R.id.dialog_industry_item_iv);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        IndustryEntity item = getItem(position);
        if (item.getIndustryIsShow()) {
            mHolder._item_iv.setVisibility(View.VISIBLE);
            mHolder._item_name.setTextColor(context.getResources().getColor(R.color.FF274FF3));
        } else {
            mHolder._item_iv.setVisibility(View.GONE);
            mHolder._item_name.setTextColor(context.getResources().getColor(R.color.color666666));
        }
        mHolder._item_name.setText(item.getIndustryName());


        return view;
    }

    static class ViewHolder {
        TextView _item_name;
        ImageView _item_iv;
    }

}