package com.huimaibao.app.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.electcircle.entity.WebEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XStringUtils;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

public class PersonalAdapter extends BaseAdapter {
    private Context context;
    public List<WebEntity> list;


    public PersonalAdapter(Context context, List<WebEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public WebEntity getItem(int position) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_home_personal_item, null);
            mHolder = new ViewHolder();
            mHolder._item_iv = view.findViewById(R.id.act_p_web_item_image);
            mHolder._item_title = view.findViewById(R.id.act_p_web_item_title);
            mHolder._item_money = view.findViewById(R.id.act_p_web_item_money);
            mHolder._item_browse = view.findViewById(R.id.act_p_web_item_browse);


            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        WebEntity item = getItem(position);
        if (item.isWebIsCheck()) {
            mHolder._item_money.setVisibility(View.VISIBLE);

            if (XStringUtils.m1(item.getWebShare()) > 0) {
                mHolder._item_money.setTextColor(context.getResources().getColor(R.color.colorfac216));
                mHolder._item_money.setText("克隆" + item.getWebShare() + "元");
            } else {
                mHolder._item_money.setTextColor(context.getResources().getColor(R.color.color999999));
                mHolder._item_money.setText("免费克隆");
            }

        } else {
            mHolder._item_money.setVisibility(View.INVISIBLE);
        }

        ImageLoaderManager.loadImage(item.getWebImage(), mHolder._item_iv, R.drawable.ic_default);

        mHolder._item_title.setText(item.getWebTitle());
        mHolder._item_browse.setText("浏览" + item.getWebBrowse());


        return view;
    }

    static class ViewHolder {
        RoundedImagView _item_iv;
        TextView _item_title, _item_money, _item_browse;
    }


}