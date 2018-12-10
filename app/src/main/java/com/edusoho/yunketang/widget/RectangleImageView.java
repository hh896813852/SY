package com.edusoho.yunketang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.edusoho.yunketang.R;

/**
 * @author huhao on 2018/7/10
 */
public class RectangleImageView extends ImageView {
    private float aspectRatio = 1; // 高宽比

    public RectangleImageView(Context context) {
        this(context, null);
    }

    public RectangleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectangleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.RectangleLayout);
        aspectRatio = arr.getFloat(R.styleable.RectangleLayout_aspectRatio, 1F);
        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize * aspectRatio), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
