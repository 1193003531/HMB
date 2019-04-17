package com.huimaibao.app.fragment.finds.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huimaibao.app.R;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class FindsAlbumAdapter extends BaseAdapter {
    private Context context;
    public List<String> list;
    private boolean _type;


    public FindsAlbumAdapter(Context context, List<String> list, boolean type) {
        this.context = context;
        this.list = list;
        this._type = type;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public String getItem(int position) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_home_card_album_item, null);
            mHolder = new ViewHolder();
            mHolder._item_album = view.findViewById(R.id.card_album_image_value);
            mHolder._item_del = view.findViewById(R.id.card_album_image_del);
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        if (list.get(position).equals("添加")) {
            mHolder._item_album.setImageResource(R.drawable.add_image_no_bg);
            mHolder._item_del.setVisibility(View.GONE);
        } else {
            try {
                //mHolder._item_album.setImageURI(Uri.parse(list.get(position)));
                ImageLoader.getInstance().displayImage("file://" + list.get(position), mHolder._item_album);
            } catch (Exception e) {
                mHolder._item_album.setImageResource(R.drawable.ic_launcher);
            }
            //ImageLoaderManager.loadImage(list.get(position), mHolder._item_album, R.drawable.ic_launcher);
            if (_type) {
                mHolder._item_del.setVisibility(View.VISIBLE);
            } else {
                mHolder._item_del.setVisibility(View.GONE);
            }
        }


        return view;
    }

    static class ViewHolder {
        ImageView _item_album, _item_del;
    }

}