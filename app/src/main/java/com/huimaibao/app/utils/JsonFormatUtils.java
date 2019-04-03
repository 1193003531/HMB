package com.huimaibao.app.utils;

import android.annotation.SuppressLint;

import com.huimaibao.app.fragment.electcircle.entity.ShareRecordEntity;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonFormatUtils {

    /**
     * String 转 List
     */
    public static ArrayList<String> stringToList(String json) {
        ArrayList<String> list = new ArrayList<>();
        if (json.length() > 5) {
            String str = json.replace("[", "").trim().replace("]", "").trim().replace("\\", "").replace("\"", "").trim();
            String[] str2 = str.split(",");
            for (String jso : str2) {
                list.add(jso.trim());
            }
        }
        return list;
    }

    /**
     * 互推圈分享任务列表排序
     */
    @SuppressLint("SimpleDateFormat")
    public static List<ShareRecordEntity> invertOrderList(List<ShareRecordEntity> L) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1, d2;
        ShareRecordEntity temp_r = new ShareRecordEntity();
//做一个冒泡排序，大的在数组的前列
        for (int i = 0; i < L.size() - 1; i++) {

            for (int j = i + 1; j < L.size(); j++) {

                ParsePosition pos1 = new ParsePosition(0);

                ParsePosition pos2 = new ParsePosition(0);

                d1 = sdf.parse(L.get(i).ShareRecordOne.getShareTaskDate(), pos1);

                d2 = sdf.parse(L.get(j).ShareRecordOne.getShareTaskDate(), pos2);

                if (d1.before(d2)) {//如果队前日期靠前，调换顺序
                    temp_r = L.get(i);
                    L.set(i, L.get(j));
                    L.set(j, temp_r);
                }
            }
        }
        return L;
    }

}
