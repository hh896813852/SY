package com.edusoho.yunketang.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author huhao on 2018/12/4
 * 左右滑，拦截，自己处理；上下滑，不拦截，子View自己处理（如子View含有scrollView）
 */
public class ChildViewPager extends ViewPager {
    /**
     * 触摸时按下的点
     **/
    PointF downP = new PointF();
    /**
     * 触摸时当前的点
     **/
    PointF curP = new PointF();
    OnSingleTouchListener onSingleTouchListener;

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildViewPager(Context context) {
        super(context);
    }

    private int currentY = 0;
    private int currentX = 0;
    private int movedY = 0;
    private int movedX = 0;
    private int diffY = 0;
    private int diffX = 0;
    private boolean isIntercept;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                movedX = (int) event.getX();
                movedY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                currentY = (int) event.getY();
                currentX = (int) event.getX();
                diffY = currentY - movedY;
                diffX = currentX - movedX;
                // 如果横向滑动趋势大于纵向滑动趋势，则拦截
                isIntercept = Math.abs(diffX) > Math.abs(diffY);
                movedY = currentY;
                movedX = currentX;
                return isIntercept;
            case MotionEvent.ACTION_UP:
                movedY = 0;
                movedX = 0;
                currentY = 0;
                currentX = 0;
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //每次进行onTouch事件都记录当前的按下的坐标
        curP.x = event.getX();
        curP.y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //记录按下时候的坐标
            //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
            downP.x = event.getX();
            downP.y = event.getY();
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            //在up时判断是否按下和松手的坐标为一个点
//            //如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
//            if (downP.x == curP.x && downP.y == curP.y) {
//                onSingleTouch();
//                return true;
//            }
//        }

        return super.onTouchEvent(event);
    }

    /**
     * 单击
     */
    public void onSingleTouch() {
        if (onSingleTouchListener != null) {

            onSingleTouchListener.onSingleTouch();
        }
    }

    /**
     * 创建点击事件接口
     *
     * @author wanpg
     */
    public interface OnSingleTouchListener {
        void onSingleTouch();
    }

    public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }

}