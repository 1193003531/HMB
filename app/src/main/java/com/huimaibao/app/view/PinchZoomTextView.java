package com.huimaibao.app.view;

import android.content.Context;
import android.graphics.Paint;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *
 */

public class PinchZoomTextView extends AppCompatTextView {
    /**
     * 将两个指针之间的每个“步骤”视为200 px
     */
    private static final float STEP = 200;

    /**
     * 文本大小与其原始大小的比例。
     */
    private float ratio = 1.0f;

    /**
     * 两个指针第一次放在屏幕上时的距离。
     */
    private int baseDistance;

    /**
     * 启动手势时文本大小的比例。
     */
    private float baseRatio;

    /**
     * 是否启用缩放功能的布尔标志。默认值为true。
     */
    private boolean zoomEnabled = true;

    /**
     * 默认构造函数。
     */
    public PinchZoomTextView(Context context) {
        this(context, null);
    }

    /**
     * 默认构造函数。
     */
    public PinchZoomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 处理用户的触摸事件，并决定字体大小是否应该增长，和多少。
     * 如果动作是简单的“POINTER_DOWN”，表示用户只是在放下手指，收集基值。
     * 否则，用户正在进行缩放，所以要得到指针之间的距离，求出比例
     * 我们需要，并设置文本大小。注:根据初始尺寸13，且不能超过比例1024。
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPaintFlags(getPaintFlags() | (Paint.LINEAR_TEXT_FLAG | Paint.SUBPIXEL_TEXT_FLAG));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPaintFlags(getPaintFlags() & ~(Paint.LINEAR_TEXT_FLAG | Paint.SUBPIXEL_TEXT_FLAG));
                break;
        }

        // Must have two gestures.
        if (zoomEnabled && event.getPointerCount() == 2) {
            int action = event.getAction();
            int distance = getDistance(event);
            int pureAction = action & MotionEvent.ACTION_MASK;
            if (pureAction == MotionEvent.ACTION_POINTER_DOWN) {
                baseDistance = distance;
                baseRatio = ratio;
            } else {
                float delta = (distance - baseDistance) / STEP;
                float multi = (float) Math.pow(2, delta);
                ratio = Math.min(1024.0f, Math.max(0.1f, baseRatio * multi));
                setTextSize(ratio + 13);
            }
        }

        return true;
    }

    /**
     * 返回屏幕上两个指针之间的距离。
     */
    private int getDistance(MotionEvent event) {
        int dx = (int) (event.getX(0) - event.getX(1));
        int dy = (int) (event.getY(0) - event.getY(1));
        return (int) (Math.sqrt(dx * dx + dy * dy));
    }

    /**
     * 设置缩放功能的启用状态。
     */
    public void setZoomEnabled(boolean enabled) {
        this.zoomEnabled = enabled;
    }

    /**
     * 返回缩放功能的启用状态。
     */
    public boolean isZoomEnabled() {
        return zoomEnabled;
    }

}

