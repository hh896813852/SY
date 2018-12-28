package com.edusoho.yunketang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edusoho.yunketang.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;

/**
 * @author huhao on 2018/11/29.
 */
public class AnswerResultLayout extends ViewGroup {
    public static final String TITLE_TAG = "title";  // 标题标记
    public static final int NOT_ANSWER = 0;  // 未答
    public static final int ANSWERED = 1;    // 已答
    public static final int ANSWER_TRUE = 3; // 答对
    public static final int ANSWER_FALSE = 4;// 答错
    private Context mContext;
    private boolean withTitle; // 是否包含标题
    private int colNum;
    private int mRealWidth;
    private int titleHeight;
    private int mVerticalSpacing;
    private int mHorizontalSpacing;

    private List<Integer> childLeft;
    private List<Integer> childTop;
    private List<Integer> childRight;
    private List<Integer> childBottom;

    private TagClickListener tagClickListener;

    public AnswerResultLayout(Context context) {
        super(context);
    }

    public AnswerResultLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnswerResultLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        titleHeight = dip2px(20);
        mVerticalSpacing = dip2px(15);
        mHorizontalSpacing = dip2px(15);
        childLeft = new ArrayList<>();
        childTop = new ArrayList<>();
        childRight = new ArrayList<>();
        childBottom = new ArrayList<>();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnswerResultLayout);
        colNum = ta.getColor(R.styleable.AnswerResultLayout_colNum, 1);
        ta.recycle();
    }

    public void setTags(List<Integer> tags) {
        withTitle = false;
        removeAllViewsInLayout();
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < tags.size(); i++) {
            addViewInLayout(createTextView(tags.get(i), i), -1, lp);
        }
        requestLayout();
    }

    /**
     * 带标题
     */
    public void setTags(Map<String, List<Integer>> mapTags) {
        withTitle = true;
        removeAllViewsInLayout();
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        for (Map.Entry<String, List<Integer>> mapTag : mapTags.entrySet()) {
            if (!TextUtils.isEmpty(mapTag.getKey())) {
                addViewInLayout(createTitleView(mapTag.getKey()), -1, lp);
            }
            for (int i = 0; i < mapTag.getValue().size(); i++) {
                addViewInLayout(createTextView(mapTag.getValue().get(i), i), -1, lp);
            }
        }
        requestLayout();
    }

    /**
     * 创建标题
     */
    private TextView createTitleView(String title) {
        TextView titleView = new TextView(mContext);
        titleView.setTag(TITLE_TAG);
        titleView.setText(title);
        titleView.setTextSize(14);
        titleView.setTextColor(ContextCompat.getColor(mContext, R.color.text_black));
        titleView.setSingleLine(true);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        return titleView;
    }

    /**
     * 创建tagView
     */
    private TextView createTextView(Integer tag, int position) {
        int drawableId = R.drawable.shape_oval_stroke_gray_bg_white;
        int colorId = R.color.text_white;
        switch (tag) {
            case ANSWERED:
                drawableId = R.drawable.shape_oval_bg_orange;
                colorId = R.color.text_white;
                break;
            case NOT_ANSWER:
                drawableId = R.drawable.shape_oval_stroke_gray_bg_white;
                colorId = R.color.text_black;
                break;
            case ANSWER_TRUE:
                drawableId = R.drawable.shape_oval_bg_theme_color;
                colorId = R.color.text_white;
                break;
            case ANSWER_FALSE:
                drawableId = R.drawable.shape_oval_bg_red;
                colorId = R.color.text_white;
                break;
        }
        TextView tv = new TextView(mContext);
        tv.setText(String.valueOf(position + 1));
        tv.setTextSize(13);
        tv.setTextColor(ContextCompat.getColor(mContext, colorId));
        tv.setGravity(Gravity.CENTER);
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setBackground(ContextCompat.getDrawable(mContext, drawableId));
        tv.setOnClickListener(v -> {
            if (tagClickListener != null) {
                tagClickListener.OnTagClick(position);
            }
        });
        return tv;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 自身宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        // 使用了的高度
        int usedHeight = 0;
        // 一行已经使用了的宽度
        int usedWidth = 0;
        // 真实宽度
        mRealWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        // 每个子View可占区域大小
        int perWidth = mRealWidth / colNum;
        // 每个子View自身实际宽度
        int childWidth = perWidth - mHorizontalSpacing;
        if (withTitle) {
            int tagIndex = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View childView = getChildAt(i);
                // 如果是标题
                if (childView.getTag() != null && childView.getTag().equals(TITLE_TAG)) {
                    tagIndex = 0;
                    childView.measure(MeasureSpec.makeMeasureSpec(width, AT_MOST), MeasureSpec.makeMeasureSpec(titleHeight, EXACTLY));
                    childLeft.add(mHorizontalSpacing / 2);
                    childRight.add(width - mHorizontalSpacing / 2);
                    childTop.add(usedHeight);
                    childBottom.add(usedHeight + titleHeight);
                    usedHeight = usedHeight + titleHeight;
                } else {
                    childView.measure(MeasureSpec.makeMeasureSpec(childWidth, EXACTLY), MeasureSpec.makeMeasureSpec(childWidth, EXACTLY));
                    if (tagIndex % colNum == 0) { // 一行的第一个
                        usedWidth = 0;
                        usedHeight += childWidth + mVerticalSpacing;
                    }
                    childLeft.add(usedWidth + mHorizontalSpacing / 2);
                    childRight.add(usedWidth + mHorizontalSpacing / 2 + childWidth);
                    childTop.add(usedHeight - childWidth - mVerticalSpacing / 2);
                    childBottom.add(usedHeight - mVerticalSpacing / 2);
                    usedWidth += perWidth;
                    tagIndex++;
                }
            }
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), usedHeight);
        } else {
            for (int i = 0; i < getChildCount(); i++) {
                View childView = getChildAt(i);
                childView.measure(MeasureSpec.makeMeasureSpec(childWidth, EXACTLY), MeasureSpec.makeMeasureSpec(childWidth, EXACTLY));
                if (i % colNum == 0) { // 一行的第一个
                    usedWidth = 0;
                }
                childLeft.add(usedWidth + mHorizontalSpacing / 2);
                childRight.add(usedWidth + mHorizontalSpacing / 2 + childWidth);
                childTop.add((i / colNum) * (childWidth + mVerticalSpacing) + mVerticalSpacing / 2);
                childBottom.add((i / colNum + 1) * (childWidth + mVerticalSpacing) - mVerticalSpacing / 2);
                usedWidth += perWidth;
                setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), perWidth * ((getChildCount() - 1) / colNum + 1));
            }
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), perWidth * ((getChildCount() - 1) / colNum + 1));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.layout(childLeft.get(i), childTop.get(i), childRight.get(i), childBottom.get(i));
        }
    }

    /**
     * 设置tag点击监听
     */
    public void setOnTagClickListener(TagClickListener tagClickListener) {
        this.tagClickListener = tagClickListener;
    }

    public interface TagClickListener {
        void OnTagClick(int index);
    }

    private int dip2px(int dip) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }
}
