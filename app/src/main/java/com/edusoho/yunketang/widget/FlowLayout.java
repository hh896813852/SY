package com.edusoho.yunketang.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.edusoho.yunketang.R;

import java.util.ArrayList;
import java.util.List;

@TargetApi(14)
public class FlowLayout extends ViewGroup {
    private int mGravity;
    private final List<List<View>> mLines;
    private final List<Integer> mLineHeights;
    private final List<Integer> mLineMargins;

    public FlowLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mGravity = (isIcs() ? 8388611 : 3) | 48;
        this.mLines = new ArrayList();
        this.mLineHeights = new ArrayList();
        this.mLineMargins = new ArrayList();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout, defStyle, 0);

        try {
            int index = a.getInt(R.styleable.FlowLayout_android_gravity, -1);
            if (index > 0) {
                this.setGravity(index);
            }
        } finally {
            a.recycle();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - this.getPaddingLeft() - this.getPaddingRight();
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height = this.getPaddingTop() + this.getPaddingBottom();
        int lineWidth = 0;
        int lineHeight = 0;
        int childCount = this.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            View child = this.getChildAt(i);
            boolean lastChild = i == childCount - 1;
            if (child.getVisibility() == View.GONE) {
                if (lastChild) {
                    width = Math.max(width, lineWidth);
                    height += lineHeight;
                }
            } else {
                this.measureChildWithMargins(child, widthMeasureSpec, lineWidth, heightMeasureSpec, height);
                FlowLayout.LayoutParams lp = (FlowLayout.LayoutParams) child.getLayoutParams();
                int childWidthMode = -2147483648;
                int childWidthSize = sizeWidth;
                int childHeightMode = -2147483648;
                int childHeightSize = sizeHeight;
                if (lp.width == -1) {
                    childWidthMode = 1073741824;
                    childWidthSize = sizeWidth - (lp.leftMargin + lp.rightMargin);
                } else if (lp.width >= 0) {
                    childWidthMode = 1073741824;
                    childWidthSize = lp.width;
                }

                if (lp.height >= 0) {
                    childHeightMode = 1073741824;
                    childHeightSize = lp.height;
                } else if (modeHeight == 0) {
                    childHeightMode = 0;
                    childHeightSize = 0;
                }

                child.measure(MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode), MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode));
                int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                if (lineWidth + childWidth > sizeWidth) {
                    width = Math.max(width, lineWidth);
                    lineWidth = childWidth;
                    height += lineHeight;
                    lineHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                } else {
                    lineWidth += childWidth;
                    lineHeight = Math.max(lineHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                }

                if (lastChild) {
                    width = Math.max(width, lineWidth);
                    height += lineHeight;
                }
            }
        }

        width += this.getPaddingLeft() + this.getPaddingRight();
        this.setMeasuredDimension(modeWidth == 1073741824 ? sizeWidth : width, modeHeight == 1073741824 ? sizeHeight : height);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mLines.clear();
        this.mLineHeights.clear();
        this.mLineMargins.clear();
        int width = this.getWidth();
        int height = this.getHeight();
        int linesSum = this.getPaddingTop();
        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList();
        float horizontalGravityFactor;
        switch (this.mGravity & 7) {
            case 1:
                horizontalGravityFactor = 0.5F;
                break;
            case 2:
            case 3:
            case 4:
            default:
                horizontalGravityFactor = 0.0F;
                break;
            case 5:
                horizontalGravityFactor = 1.0F;
        }

        int verticalGravityMargin;
        int top;
        int i;
        for (verticalGravityMargin = 0; verticalGravityMargin < this.getChildCount(); ++verticalGravityMargin) {
            View child = this.getChildAt(verticalGravityMargin);
            if (child.getVisibility() != View.GONE) {
                FlowLayout.LayoutParams lp = (FlowLayout.LayoutParams) child.getLayoutParams();
                top = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                i = child.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;
                if (lineWidth + top > width) {
                    this.mLineHeights.add(Integer.valueOf(lineHeight));
                    this.mLines.add(lineViews);
                    this.mLineMargins.add(Integer.valueOf((int) ((float) (width - lineWidth) * horizontalGravityFactor) + this.getPaddingLeft()));
                    linesSum += lineHeight;
                    lineHeight = 0;
                    lineWidth = 0;
                    lineViews = new ArrayList();
                }

                lineWidth += top;
                lineHeight = Math.max(lineHeight, i);
                lineViews.add(child);
            }
        }

        this.mLineHeights.add(Integer.valueOf(lineHeight));
        this.mLines.add(lineViews);
        this.mLineMargins.add(Integer.valueOf((int) ((float) (width - lineWidth) * horizontalGravityFactor) + this.getPaddingLeft()));
        linesSum += lineHeight;
        verticalGravityMargin = 0;
        switch (this.mGravity & 112) {
            case 16:
                verticalGravityMargin = (height - linesSum) / 2;
            case 48:
            default:
                break;
            case 80:
                verticalGravityMargin = height - linesSum;
        }

        int numLines = this.mLines.size();
        top = this.getPaddingTop();

        for (i = 0; i < numLines; ++i) {
            lineHeight = ((Integer) this.mLineHeights.get(i)).intValue();
            List<View> lineViews2 = (List) this.mLines.get(i);
            int left = ((Integer) this.mLineMargins.get(i)).intValue();
            int children = lineViews2.size();

            for (int j = 0; j < children; ++j) {
                View child = (View) lineViews2.get(j);
                if (child.getVisibility() != View.GONE) {
                    FlowLayout.LayoutParams lp = (FlowLayout.LayoutParams) child.getLayoutParams();
                    int childWidthMode;
                    int childHeight;
                    if (lp.height == -1) {
                        childWidthMode = -2147483648;
                        childHeight = lineWidth;
                        if (lp.width == -1) {
                            childWidthMode = 1073741824;
                        } else if (lp.width >= 0) {
                            childWidthMode = 1073741824;
                            childHeight = lp.width;
                        }
                        child.measure(MeasureSpec.makeMeasureSpec(childHeight, childWidthMode), MeasureSpec.makeMeasureSpec(lineHeight - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY));
                    }

                    childWidthMode = child.getMeasuredWidth();
                    childHeight = child.getMeasuredHeight();
                    int gravityMargin = 0;
                    if (Gravity.isVertical(lp.gravity)) {
                        switch (lp.gravity) {
                            case 16:
                            case 17:
                                gravityMargin = (lineHeight - childHeight - lp.topMargin - lp.bottomMargin) / 2;
                            case 48:
                            default:
                                break;
                            case 80:
                                gravityMargin = lineHeight - childHeight - lp.topMargin - lp.bottomMargin;
                        }
                    }

                    child.layout(left + lp.leftMargin, top + lp.topMargin + gravityMargin + verticalGravityMargin, left + childWidthMode + lp.leftMargin, top + childHeight + lp.topMargin + gravityMargin + verticalGravityMargin);
                    left += childWidthMode + lp.leftMargin + lp.rightMargin;
                }
            }

            top += lineHeight;
        }

    }

    protected FlowLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new FlowLayout.LayoutParams(p);
    }

    public FlowLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FlowLayout.LayoutParams(this.getContext(), attrs);
    }

    protected FlowLayout.LayoutParams generateDefaultLayoutParams() {
        return new FlowLayout.LayoutParams(-1, -1);
    }

    @TargetApi(14)
    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            if ((gravity & 8388615) == 0) {
                gravity |= isIcs() ? 8388611 : 3;
            }

            if ((gravity & 112) == 0) {
                gravity |= 48;
            }

            this.mGravity = gravity;
            this.requestLayout();
        }

    }

    public int getGravity() {
        return this.mGravity;
    }

    private static boolean isIcs() {
        return VERSION.SDK_INT >= 14;
    }

    public static class LayoutParams extends MarginLayoutParams {
        public int gravity = -1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.FlowLayout_Layout);

            try {
                this.gravity = a.getInt(R.styleable.FlowLayout_Layout_android_layout_gravity, -1);
            } finally {
                a.recycle();
            }

        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}