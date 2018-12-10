package com.edusoho.yunketang.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * @author any on 2017/2/24.
 */
public class GuideViewPager extends ViewPager {
    private OnScrollListener onScrollListener;

    public GuideViewPager(Context context) {
        super(context);
    }

    public GuideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (onScrollListener != null) {
            onScrollListener.onScroll(x);
        }
    }

    /**
     * 设置滑动接口监听
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 滑动的回调接口
     */
    public interface OnScrollListener {
        // 回调方法， 返回GuideViewPager滑动的X方向距离
        void onScroll(int scrollX);
    }
}
