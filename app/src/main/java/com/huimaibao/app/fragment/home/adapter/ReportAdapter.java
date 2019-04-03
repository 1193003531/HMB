package com.huimaibao.app.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.home.entity.ReportEntity;

import java.util.List;

/**
 * 举报
 */
public class ReportAdapter extends BaseAdapter {
    private Context context;
    public List<ReportEntity> list;


    public ReportAdapter(Context context, List<ReportEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ReportEntity getItem(int position) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_report_item, null);
            mHolder = new ViewHolder();
            mHolder._item_check = view.findViewById(R.id.report_item_check);
            mHolder._item_line = view.findViewById(R.id.report_item_line);
            mHolder._item_name = view.findViewById(R.id.report_item_name);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        ReportEntity item = getItem(position);

        mHolder._item_name.setText(item.getReportName());
        if (item.getReportIsCheck()) {
            mHolder._item_check.setVisibility(View.VISIBLE);
        } else {
            mHolder._item_check.setVisibility(View.GONE);
        }
        //隐藏最后一条数据-line
        if (position == list.size()) {
            mHolder._item_line.setVisibility(View.GONE);
        } else {
            mHolder._item_line.setVisibility(View.VISIBLE);
        }

        return view;
    }

    static class ViewHolder {
        ImageView _item_check, _item_line;
        TextView _item_name;
    }

}