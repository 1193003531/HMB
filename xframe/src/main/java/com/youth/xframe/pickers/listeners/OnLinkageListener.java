package com.youth.xframe.pickers.listeners;

import com.youth.xframe.pickers.entity.City;
import com.youth.xframe.pickers.entity.County;
import com.youth.xframe.pickers.entity.Province;

/**
 */

public interface OnLinkageListener {
    /**
     * 选择地址
     *
     * @param province the province
     * @param city    the city
     * @param county   the county ，if {@code hideCounty} is true，this is null
     */
    void onAddressPicked(Province province, City city, County county);
}
