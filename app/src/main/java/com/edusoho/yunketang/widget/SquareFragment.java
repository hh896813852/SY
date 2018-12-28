package com.edusoho.yunketang.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author huhao on 2017/6/22 0022.
 */

public class SquareFragment extends FrameLayout {

    public SquareFragment(Context context) {
        super(context);
    }

    public SquareFragment(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFragment(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
