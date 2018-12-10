package com.edusoho.yunketang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.edusoho.yunketang.R;
import com.edusoho.yunketang.utils.DrawableUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author huhao on 2016/12/8.
 */
public class TagLayout extends ViewGroup {
    private Context mContext;
    private int mPressedColor;
    private int mNormalColor;
    private int mTagPressedColor;
    private int mTagNormalColor;
    private int mRealWidth;
    private List<Line> lines;
    private int mVerticalSpacing;
    private int mHorizontalSpacing;
    private int mPerHeight;
    private OnItemClickListener mOnItemClickListener;
    private int mCorner;
    private int mHorPadding;
    private int mVerPadding;

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        lines = new ArrayList<>();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagLayout);
        mNormalColor = ta.getColor(R.styleable.TagLayout_normalColor, 0);
        mPressedColor = ta.getColor(R.styleable.TagLayout_pressedColor, 0xffcecece);
        mTagNormalColor = ta.getColor(R.styleable.TagLayout_tagNormalColor, 0xffffffff);
        mTagPressedColor = ta.getColor(R.styleable.TagLayout_tagPressedColor, 0xffffffff);
        mHorizontalSpacing = ta.getDimensionPixelOffset(R.styleable.TagLayout_horizontalSpacing, dip2px(10));
        mVerticalSpacing = ta.getDimensionPixelOffset(R.styleable.TagLayout_verticalSpacing, dip2px(10));
        mCorner = ta.getDimensionPixelOffset(R.styleable.TagLayout_corner, dip2px(5));
        mHorPadding = dip2px(10);
        mVerPadding = dip2px(5);
        ta.recycle();
    }


    public void setTags(List<String> tags) {
        setTags(tags, 0);
    }

    public void setTags(List<String> tags, int selectedPosition) {
        tagViewList.clear();
        removeAllViewsInLayout();
        GradientDrawable pressedDrawable = DrawableUtil.createRoundRect(mPressedColor, mCorner);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < tags.size(); i++) {
            addViewInLayout(createTextView(pressedDrawable, tags.get(i), i), -1, lp);
        }
        if (tags.size() > selectedPosition) {
            changeColor(selectedPosition);
        }
        requestLayout();
    }

    @NonNull
    private TextView createTextView(GradientDrawable pressedDrawable, final String tag, int position) {
        if (mNormalColor == 0) {
            Random random = new Random();
            int red = random.nextInt(200) + 25;
            int green = random.nextInt(200) + 25;
            int blue = random.nextInt(200) + 25;
            mNormalColor = Color.rgb(red, green, blue);
        }
        TextView tv = new TextView(mContext);
        tv.setPadding(mHorPadding, mVerPadding, mHorPadding, mVerPadding);
        tv.setText(tag);
        tv.setGravity(Gravity.CENTER);
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, tag, position);
                    changeColor(position);
                }
            }
        });
        tagViewList.add(tv);
        return tv;
    }

    /**
     * 改变tag状态（颜色和背景）
     */
    private void changeColor(int position) {
        for (int i = 0; i < tagViewList.size(); i++) {
            if (i == position) {
                tagViewList.get(i).setTextColor(ContextCompat.getColor(mContext, R.color.theme_color));
                tagViewList.get(i).setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_light_green_corner_4));
            } else {
                tagViewList.get(i).setTextColor(ContextCompat.getColor(mContext, R.color.text_black));
                tagViewList.get(i).setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_light_gray_corner_4));
            }
        }
    }

    private List<TextView> tagViewList = new ArrayList<>();

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        if (count != 0) {
            lines.clear();
            mRealWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
            int realHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mRealWidth, widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(realHeight, heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode);
            int spaceWidth = mRealWidth;

            View child;
            Line line = new Line();
            for (int i = 0; i < count; i++) {
                child = getChildAt(i);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                int childWidth = child.getMeasuredWidth();
                if (spaceWidth >= childWidth) {
                    line.add(child);
                    spaceWidth -= childWidth;
                    spaceWidth -= mHorizontalSpacing;
                } else {
                    lines.add(line);
                    line = new Line();
                    line.add(child);
                    spaceWidth = mRealWidth - childWidth - mHorizontalSpacing;
                }
            }
            lines.add(line);
            mPerHeight = getChildAt(0).getMeasuredHeight();
            int totalHeight = mPerHeight * lines.size() + mVerticalSpacing * (lines.size() - 1)
                    + getPaddingTop() + getPaddingBottom();
            setMeasuredDimension(mRealWidth + getPaddingLeft() + getPaddingRight(),
                    resolveSize(totalHeight, heightMeasureSpec));
        } else {
            setMeasuredDimension(mRealWidth + getPaddingLeft() + getPaddingRight(),
                    resolveSize(0, heightMeasureSpec));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        t = getPaddingTop();
        l += getPaddingLeft();
        for (Line line : lines) {
            line.layout(l, t);
            t += mPerHeight + mVerticalSpacing;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, String tag, int position);
    }

    class Line {
        private List<View> children;
        private int totalWidth;

        public Line() {
            children = new ArrayList<>();
            totalWidth = 0;
        }

        public void add(View child) {
            totalWidth += child.getMeasuredWidth();
            children.add(child);
        }

        public void layout(int l, int t) {
            totalWidth += (children.size() - 1) * mHorizontalSpacing;
            int perPlus = 0;
            if (totalWidth < mRealWidth) {
                perPlus = (mRealWidth - totalWidth) / children.size();
            }
            for (View child : children) {
                int childWidth = child.getMeasuredWidth() + perPlus;
                if (perPlus > 0) {
                    int widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                    int heightMeasureSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), MeasureSpec.EXACTLY);
                    child.measure(widthMeasureSpec, heightMeasureSpec);
                }
                child.layout(l, t, l + childWidth, t + child.getMeasuredHeight());
                l = l + childWidth + mHorizontalSpacing;
            }
        }
    }


    private int dip2px(int dip) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

}
