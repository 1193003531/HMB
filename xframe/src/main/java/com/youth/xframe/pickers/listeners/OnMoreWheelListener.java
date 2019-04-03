package com.youth.xframe.pickers.listeners;

/**
 * 针对地址选择，车牌选择等提供的外部回调接口
 */

public interface OnMoreWheelListener {
    public abstract void onFirstWheeled(int index, String item);

    public abstract void onSecondWheeled(int index, String item);

    public abstract void onThirdWheeled(int index, String item);
}
