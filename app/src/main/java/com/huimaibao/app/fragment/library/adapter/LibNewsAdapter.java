package com.huimaibao.app.fragment.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.library.bean.LibNewsEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.widget.CircleImageView;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

public class LibNewsAdapter extends BaseAdapter {
    private Context context;
    public List<LibNewsEntity> libNewsList;


    public LibNewsAdapter(Context context, List<LibNewsEntity> libNewsList) {
        this.context = context;
        this.libNewsList = libNewsList;
    }

    @Override
    public int getCount() {
        return libNewsList == null ? 0 : libNewsList.size();
    }

    @Override
    public LibNewsEntity getItem(int position) {
        if (libNewsList != null && libNewsList.size() != 0) {
            return libNewsList.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.fragment_lib_news_item, null);
            mHolder = new ViewHolder();

            mHolder._item_focus_image = view.findViewById(R.id.lib_focus_on_item_name_image);
            mHolder._item_focus_name = view.findViewById(R.id.lib_focus_on_item_name);
            mHolder._item_f_line = view.findViewById(R.id.lib_focus_on_item_line);

            mHolder._item_image_right = view.findViewById(R.id.lib_news_item_image_right);
            mHolder._item_image = view.findViewById(R.id.lib_news_item_image);
            mHolder._item_image_1 = view.findViewById(R.id.lib_news_item_image_1);
            mHolder._item_image_2 = view.findViewById(R.id.lib_news_item_image_2);
            mHolder._item_image_3 = view.findViewById(R.id.lib_news_item_image_3);
            mHolder._item_browse = view.findViewById(R.id.lib_news_item_browse);
            mHolder._item_title = view.findViewById(R.id.lib_news_item_title);
            mHolder._item_name = view.findViewById(R.id.lib_news_item_name);
            mHolder._item_time = view.findViewById(R.id.lib_news_item_time);

            mHolder._item_focus_ll = view.findViewById(R.id.lib_focus_on_item_ll);
            mHolder._item_image_ll = view.findViewById(R.id.lib_news_item_image_ll);
            mHolder._item_image_3_ll = view.findViewById(R.id.lib_news_item_image_3_ll);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        LibNewsEntity item = getItem(position);

        if (item.getLibLibNewsState().equals("关注")) {
            mHolder._item_focus_ll.setVisibility(View.VISIBLE);
            mHolder._item_f_line.setVisibility(View.VISIBLE);
            mHolder._item_name.setVisibility(View.GONE);
            mHolder._item_focus_name.setText("" + item.getLibNewsName());
            ImageLoaderManager.loadImage(item.getLibLibNewsNameImage(), mHolder._item_focus_image, R.drawable.ic_launcher);
        } else {
            mHolder._item_focus_ll.setVisibility(View.GONE);
            mHolder._item_f_line.setVisibility(View.GONE);
            mHolder._item_name.setVisibility(View.VISIBLE);
        }


        if (item.getLibNewsType().equals("0")) {
            mHolder._item_image_right.setVisibility(View.VISIBLE);
            mHolder._item_image_ll.setVisibility(View.GONE);
            ImageLoaderManager.loadImage(item.getLibNewsImageRight(), mHolder._item_image_right, R.drawable.ic_launcher);
        } else if (item.getLibNewsType().equals("1")) {
            mHolder._item_image_right.setVisibility(View.GONE);
            mHolder._item_image_ll.setVisibility(View.VISIBLE);
            mHolder._item_image_3_ll.setVisibility(View.VISIBLE);
            mHolder._item_image.setVisibility(View.GONE);
            ImageLoaderManager.loadImage(item.getLibNewsImage1(), mHolder._item_image_1, R.drawable.ic_launcher);
            ImageLoaderManager.loadImage(item.getLibNewsImage2(), mHolder._item_image_2, R.drawable.ic_launcher);
            ImageLoaderManager.loadImage(item.getLibNewsImage3(), mHolder._item_image_3, R.drawable.ic_launcher);
        } else {
            mHolder._item_image_right.setVisibility(View.GONE);
            mHolder._item_image.setVisibility(View.VISIBLE);
            mHolder._item_image_ll.setVisibility(View.VISIBLE);
            mHolder._item_image_3_ll.setVisibility(View.GONE);
            ImageLoaderManager.loadImage(item.getLibNewsImage(), mHolder._item_image, R.drawable.ic_launcher);
        }


        mHolder._item_title.setText(item.getLibNewsTitle());
        mHolder._item_time.setText(item.getLibNewsTime());
        mHolder._item_browse.setText(item.getLibNewsBrowse() + "阅读");
        mHolder._item_name.setText("" + item.getLibNewsName());



        return view;
    }

    static class ViewHolder {
        LinearLayout _item_focus_ll;
        CircleImageView _item_focus_image;
        TextView _item_focus_name;
        ImageView _item_f_line;


        RoundedImagView _item_image, _item_image_right, _item_image_1, _item_image_2, _item_image_3;
        LinearLayout _item_image_ll, _item_image_3_ll;

        TextView _item_title, _item_name, _item_browse, _item_time;
    }

}