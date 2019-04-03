package com.huimaibao.app.fragment.electcircle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.electcircle.entity.WebEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

/**个人微网，营销网页适配器*/
public class WebAdapter extends BaseAdapter {
    private Context context;
    public List<WebEntity> list;


    public WebAdapter(Context context, List<WebEntity> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_home_making_list_check_item, null);
            mHolder = new ViewHolder();
            mHolder._item_check = view.findViewById(R.id.act_p_web_check_item_iv);
            mHolder._item_iv = view.findViewById(R.id.act_p_web_check_item_image);
            mHolder._item_title = view.findViewById(R.id.act_p_web_check_item_title);
            mHolder._item_share = view.findViewById(R.id.act_p_web_check_item_share);
            mHolder._item_browse = view.findViewById(R.id.act_p_web_check_item_browse);
            mHolder._item_time = view.findViewById(R.id.act_p_web_check_item_time);


            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        WebEntity item = getItem(position);
        if (item.isWebIsCheck()) {
            mHolder._item_check.setImageResource(R.drawable.home_true_icon);
        } else {
            mHolder._item_check.setImageResource(R.drawable.home_false_icon);
        }

        ImageLoaderManager.loadImage(item.getWebImage(), mHolder._item_iv, R.drawable.ic_launcher);
        mHolder._item_title.setText(item.getWebTitle());
        mHolder._item_share.setText("转发  " + item.getWebShare());
        mHolder._item_browse.setText("浏览  " + item.getWebBrowse());
        mHolder._item_time.setText(XTimeUtils.StringToYMD(item.getWebTime()));

        mHolder._item_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemCheckListener.onItemCheckClick(position);
            }
        });

        return view;
    }

    static class ViewHolder {
        ImageView _item_check;
        RoundedImagView _item_iv;
        TextView _item_title, _item_share, _item_browse, _item_time;
    }

    /**
     * 点击选择监听接口
     */
    public interface onItemCheckListener {
        void onItemCheckClick(int position);
    }

    private onItemCheckListener mOnItemCheckListener;

    public void setOnItemCheckListener(onItemCheckListener mOnItemCheckListener) {
        this.mOnItemCheckListener = mOnItemCheckListener;
    }

}