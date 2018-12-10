package com.edusoho.yunketang.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.edusoho.yunketang.R;

/**
 * @author huhao on 2018/7/17
 */
public class DragFrameLayout extends FrameLayout {
    private boolean isCanDrag = false;

    public DragFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public DragFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean isMoved;

    int currentY = 0;
    int movedY = 0;
    int diffY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                isMoved = false;
                int downX = (int) ev.getX();
                int downY = (int) ev.getY();
                // 是否拖动
                isCanDrag = dragView != null && inDragView(downX, downY);
                movedY = downY;
                break;
            }
        }
        // 如果要拖动，则拦截Touch事件，交给自己的onTouchEvent来处理
        if (isCanDrag) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // 如果要拖动，返回true，表示接下来的Touch自己来处理，否则向上传递
                if (isCanDrag) {
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                currentY = (int) ev.getY();
                diffY = currentY - movedY;
                isMoved = isCanDrag;
                if (isCanDrag) {
                    setDragViewLayoutParams(diffY);
                    setScrollViewHeight();
                    setViewPagerHeight();
                }
                movedY = currentY;
                break;
            }
            case MotionEvent.ACTION_UP: {
                isCanDrag = false;
                if (isMoved) {
                    isMoved = false;
                    setDragViewLayoutParams(0);
                    setScrollViewHeight();
                    setViewPagerHeight();
                }
                break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private boolean inDragView(int x, int y) {
//        LogUtil.i("DragFrameLayout", "手指落下的位置(" + x + "," + y + ")");
//        LogUtil.i("DragFrameLayout", "dragView位置" + dragView.getLeft() + "," + dragView.getRight() + "," + dragView.getTop() + "," + dragView.getBottom());
        return x < dragView.getRight() && x > dragView.getLeft() && y < dragView.getBottom() && y > dragView.getTop();
    }

    /**
     * 滑动时，动态控制DragView的位置
     */
    private void setDragViewLayoutParams(int diffY) {
        LayoutParams dragViewParams = (LayoutParams) dragView.getLayoutParams();
        dragViewParams.height = dragView.getBottom() - dragView.getTop();
        int MarginTop = dragView.getTop() + diffY;
        if (MarginTop < 0) {
            MarginTop = 0;
        }
        if(MarginTop > getHeight() - dragViewParams.height) {
            MarginTop = getHeight() - dragViewParams.height;
        }
        dragViewParams.setMargins(0, MarginTop, 0, 0);
        dragView.setLayoutParams(dragViewParams);
    }

    /**
     * 滑动时，动态控制scrollView高度
     */
    private void setScrollViewHeight() {
        LayoutParams params = (LayoutParams) scrollView.getLayoutParams();
        params.height = dragView.getTop();
        scrollView.setLayoutParams(params);
    }

    /**
     * 滑动时，动态控制viewPager高度
     */
    private void setViewPagerHeight() {
        LayoutParams viewPagerParams = (LayoutParams) viewPager.getLayoutParams();
        viewPagerParams.height = getHeight() - dragView.getBottom();
        viewPagerParams.setMargins(0, dragView.getBottom(), 0, 0);
        viewPager.setLayoutParams(viewPagerParams);
    }

    private View dragView;         // 可拖动的View
    private ScrollView scrollView; // scrollView
    private ViewPager viewPager;   // viewPager

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dragView = findViewById(R.id.dragView);
        scrollView = findViewById(R.id.scrollView);
        viewPager = findViewById(R.id.viewPager);
    }
}
