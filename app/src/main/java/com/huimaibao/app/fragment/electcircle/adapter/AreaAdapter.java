package com.huimaibao.app.fragment.electcircle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.electcircle.entity.AreaEntity;

import java.util.List;

/**推广区域适配器*/
public class AreaAdapter extends BaseAdapter {
    private Context context;
    public List<AreaEntity> list;


    public AreaAdapter(Context context, List<AreaEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public AreaEntity getItem(int position) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_elect_area_item, null);
            mHolder = new ViewHolder();
            mHolder._item_name = view.findViewById(R.id.elect_area_item_name);
            mHolder._item_iv = view.findViewById(R.id.elect_area_item_iv);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        AreaEntity item = getItem(position);
        if (item.getAreaIsShow()) {
            mHolder._item_iv.setVisibility(View.VISIBLE);
        } else {
            mHolder._item_iv.setVisibility(View.GONE);
        }
        mHolder._item_name.setText(item.getAreaName());


        return view;
    }

    static class ViewHolder {
        TextView _item_name;
        ImageView _item_iv;
    }

}