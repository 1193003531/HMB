package com.huimaibao.app.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huimaibao.app.R;
import com.huimaibao.app.utils.ImageLoaderManager;

import java.util.List;

public class CardAlbumAdapter extends BaseAdapter {
    private Context context;
    public List<String> ilList;
    private boolean _type;


    public CardAlbumAdapter(Context context, List<String> ilList, boolean type) {
        this.context = context;
        this.ilList = ilList;
        this._type = type;
    }

    @Override
    public int getCount() {
        return ilList == null ? 0 : ilList.size();
    }

    @Override
    public String getItem(int position) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_home_card_album_item, null);
            mHolder = new ViewHolder();
            mHolder._item_album_details = view.findViewById(R.id.card_album_image);
            mHolder._item_album = view.findViewById(R.id.card_album_image_value);
            mHolder._item_del = view.findViewById(R.id.card_album_image_del);
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        if (ilList.get(position).equals("添加")) {
            mHolder._item_album.setImageResource(R.drawable.add_image_no_bg);
            mHolder._item_del.setVisibility(View.GONE);
        } else {
            if (_type) {
                mHolder._item_album_details.setVisibility(View.GONE);
                mHolder._item_album.setVisibility(View.VISIBLE);
                mHolder._item_del.setVisibility(View.VISIBLE);
                ImageLoaderManager.loadImage(ilList.get(position), mHolder._item_album, R.drawable.ic_default);
            } else {
                mHolder._item_album_details.setVisibility(View.VISIBLE);
                mHolder._item_album.setVisibility(View.GONE);
                mHolder._item_del.setVisibility(View.GONE);
                ImageLoaderManager.loadImage(ilList.get(position), mHolder._item_album_details, R.drawable.ic_default);
            }
        }


        return view;
    }

    static class ViewHolder {
        ImageView _item_album_details, _item_album, _item_del;
    }

}