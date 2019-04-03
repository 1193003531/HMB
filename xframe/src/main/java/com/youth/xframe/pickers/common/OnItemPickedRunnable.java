package com.youth.xframe.pickers.common;


import com.youth.xframe.pickers.listeners.OnItemPickListener;
import com.youth.xframe.pickers.widget.WheelView;

final public class OnItemPickedRunnable implements Runnable {
    final private  WheelView wheelView;
    private OnItemPickListener onItemPickListener;
    public OnItemPickedRunnable(WheelView wheelView, OnItemPickListener onItemPickListener) {
        this.wheelView = wheelView;
        this.onItemPickListener = onItemPickListener;
    }

    @Override
    public final void run() {
        onItemPickListener.onItemPicked(wheelView.getCurrentPosition(),wheelView.getCurrentItem());
    }
}
