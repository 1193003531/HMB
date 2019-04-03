package com.huimaibao.app.fragment.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huimaibao.app.R;
import com.huimaibao.app.fragment.mine.entity.CollectionEntity;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XTimeUtils;
import com.youth.xframe.widget.RoundedImagView;

import java.util.List;

public class CollectionAdapter extends BaseAdapter {
    private Context context;
    public List<CollectionEntity> ilList;


    public CollectionAdapter(Context context, List<CollectionEntity> ilList) {
        this.context = context;
        this.ilList = ilList;
    }

    @Override
    public int getCount() {
        return ilList == null ? 0 : ilList.size();
    }

    @Override
    public CollectionEntity getItem(int position) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_mine_collection_item, null);
            mHolder = new ViewHolder();
            mHolder._item_box = view.findViewById(R.id.collection_item_check);
            mHolder._item_image = view.findViewById(R.id.collection_item_image_right);
            mHolder._item_image_1 = view.findViewById(R.id.collection_item_image_1);
            mHolder._item_image_2 = view.findViewById(R.id.collection_item_image_2);
            mHolder._item_image_3 = view.findViewById(R.id.collection_item_image_3);
            mHolder._item_browse = view.findViewById(R.id.collection_item_browse);
            mHolder._item_title = view.findViewById(R.id.collection_item_title);
            mHolder._item_name = view.findViewById(R.id.collection_item_name);
            mHolder._item_time = view.findViewById(R.id.collection_item_time);
            mHolder._item_image_ll = view.findViewById(R.id.collection_item_image_ll);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        CollectionEntity ilItem = getItem(position);

        if (ilItem.getCollectionIsShow())
            mHolder._item_box.setVisibility(View.VISIBLE);
        else
            mHolder._item_box.setVisibility(View.GONE);

        if (ilItem.getCollectionIsCheck())
            mHolder._item_box.setChecked(true);
        else
            mHolder._item_box.setChecked(false);

        if (ilItem.getCollectionType().equals("1")) {
            mHolder._item_image.setVisibility(View.GONE);
            mHolder._item_image_ll.setVisibility(View.VISIBLE);
            ImageLoaderManager.loadImage(ilItem.getCollectionImage1(), mHolder._item_image_1, R.drawable.ic_launcher);
            ImageLoaderManager.loadImage(ilItem.getCollectionImage2(), mHolder._item_image_2, R.drawable.ic_launcher);
            ImageLoaderManager.loadImage(ilItem.getCollectionImage3(), mHolder._item_image_3, R.drawable.ic_launcher);
        } else {
            mHolder._item_image.setVisibility(View.VISIBLE);
            mHolder._item_image_ll.setVisibility(View.GONE);
            ImageLoaderManager.loadImage(ilItem.getCollectionImage(), mHolder._item_image, R.drawable.ic_launcher);
        }


        mHolder._item_title.setText(ilItem.getCollectionTitle());
        if (XEmptyUtils.isSpace(ilItem.getCollectionTime())) {
            mHolder._item_time.setText("");
        } else {
            mHolder._item_time.setText(XTimeUtils.StringToYMD(ilItem.getCollectionTime()));
        }

        mHolder._item_browse.setText(ilItem.getCollectionBrowse() + "阅读");
        mHolder._item_name.setText(XEmptyUtils.isSpace(ilItem.getCollectionName()) ? "" : ilItem.getCollectionName());


        return view;
    }

    static class ViewHolder {
        CheckBox _item_box;
        RoundedImagView _item_image, _item_image_1, _item_image_2, _item_image_3;
        LinearLayout _item_image_ll;

        TextView _item_title, _item_name, _item_browse, _item_time;
    }

}