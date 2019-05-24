package com.huimaibao.app.fragment.electcircle.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huimaibao.app.R;
import com.huimaibao.app.utils.ImageLoaderManager;
import com.youth.xframe.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class VectorTagsAdapter extends TagsAdapter {

    private Activity mActivity;
    //private List<String> dataSet = new ArrayList<>();
    private List<Map<String, Object>> dataSet = new ArrayList<>();

    public VectorTagsAdapter(Activity mActivity, @NonNull List<Map<String, Object>> data) {
        this.mActivity = mActivity;
        this.dataSet = data;
    }


    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public View getView(Context context, final int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.tag_item_vector, parent, false);

        CircleImageView imageView = view.findViewById(R.id.vector_img);
//        CircleImageView imageView2 = view.findViewById(R.id.vector_img_iv);
//        imageView2.setTag("" + position);
//        imageView2.setImageResource(R.color.tagLight);
        ImageLoaderManager.loadImage(dataSet.get(position).get("headImage").toString(), imageView, R.drawable.ic_default);
        imageView.setTag("" + position);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                XToast.normal("Tag " + dataSet.get(position).get("id") + " clicked.");
//            }
//        });

        return view;
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public int getPopularity(int position) {
        Log.e("", "Popularity" + position % 5);
        return position % 5;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor, float alpha) {
//        CircleImageView imageView = view.findViewById(R.id.vector_img_iv);
//        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(themeColor,
//                PorterDuff.Mode.SRC_ATOP);
//        if (imageView == null) {
//            return;
//        }
//        imageView.getDrawable().setColorFilter(porterDuffColorFilter);

        view.findViewById(R.id.android_eye).setBackgroundColor(themeColor);
        int rgb = 75;
        int color = Color.argb((int) ((1 - alpha) * 200), rgb, rgb, rgb);
        ((CircleImageView) view.findViewById(R.id.vector_img)).setColorFilter(color);

    }
}
