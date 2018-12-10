package com.edusoho.yunketang.base;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.edusoho.yunketang.base.animation.BaseAnimatorSet;
import com.edusoho.yunketang.utils.ScreenUtil;


public class BaseDialog extends Dialog {
    private Context mContext;
    /**
     * 对话框显示动画
     */
    private BaseAnimatorSet mShowAnim;
    /**
     * 对话框消失动画
     */
    private BaseAnimatorSet mDismissAnim;
    /**
     * 显示动画是否正在执行
     */
    private boolean mIsShowAnim;
    /**
     * 消失动画是否正在执行
     */
    private boolean mIsDismissAnim;
    /**
     * 在给定时间后,自动消失
     */
    private boolean mAutoDismiss;
    /**
     * 对话框消失延时时间,毫秒值
     */
    private long mAutoDismissDelay = 1500;
    /**
     * 最上层容器
     */
    protected LinearLayout mLlTop;
    /**
     * 用于控制对话框高度
     */
    protected LinearLayout mLlControlHeight;
    /**
     * 设备密度
     */
    protected DisplayMetrics mDisplayMetrics;
    /**
     * 最大高度
     */
    protected float mMaxHeight;
    /**
     * 设置点击对话框以外区域,是否dismiss
     */
    protected boolean mCancel;
    // 自定的布局
    private View customLayout;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public BaseDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        setDialogTheme();
    }

    /**
     * 设置对话框主题
     */
    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        mMaxHeight = mDisplayMetrics.heightPixels - ScreenUtil.getStatusBarHeight(mContext);

        customLayout.setClickable(true);

        mLlTop = new LinearLayout(mContext);
        mLlTop.setGravity(Gravity.CENTER);

        mLlControlHeight = new LinearLayout(mContext);
        mLlControlHeight.setOrientation(LinearLayout.VERTICAL);

        mLlControlHeight.addView(customLayout);
        mLlTop.addView(mLlControlHeight);

        setContentView(mLlTop, new ViewGroup.LayoutParams(mDisplayMetrics.widthPixels, (int) mMaxHeight));

        mLlTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancel) {
                    dismiss();
                }
            }
        });
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        this.mCancel = cancel;
        super.setCanceledOnTouchOutside(cancel);
    }

    /**
     * dialog width scale(宽度比例)
     */
    protected float mWidthScale = 1;
    /**
     * dialog height scale(高度比例)
     */
    protected float mHeightScale;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        int width;
        if (mWidthScale == 0) {
            width = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            width = (int) (mDisplayMetrics.widthPixels * mWidthScale);
        }

        int height;
        if (mHeightScale == 0) {
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else if (mHeightScale == 1) {
            height = (int) mMaxHeight;
        } else {
            height = (int) (mMaxHeight * mHeightScale);
        }

        mLlControlHeight.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        if (mShowAnim != null) {
            mShowAnim.listener(new BaseAnimatorSet.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mIsShowAnim = true;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mIsShowAnim = false;
                    delayDismiss();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mIsShowAnim = false;
                }
            }).playOn(mLlControlHeight);
        } else {
            BaseAnimatorSet.reset(mLlControlHeight);
            delayDismiss();
        }
    }

    @Override
    public void dismiss() {
        if (mDismissAnim != null) {
            mDismissAnim.listener(new BaseAnimatorSet.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mIsDismissAnim = true;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mIsDismissAnim = false;
                    superDismiss();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mIsDismissAnim = false;
                    superDismiss();
                }
            }).playOn(mLlControlHeight);
        } else {
            superDismiss();
        }
    }

    /**
     * 无动画dismiss
     */
    public void superDismiss() {
        super.dismiss();
    }

    public BaseDialog setCustomLayout(int layoutId) {
        customLayout = View.inflate(mContext, layoutId, null);
        return this;
    }

    private void delayDismiss() {
        if (mAutoDismiss && mAutoDismissDelay > 0) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, mAutoDismissDelay);
        }
    }

    /**
     * 设置显示的动画
     */
    public BaseDialog showAnim(BaseAnimatorSet showAnim) {
        mShowAnim = showAnim;
        return this;
    }

    /**
     * 动画弹出对话框,style动画资源
     */
    public void show(int animStyle) {
        Window window = getWindow();
        window.setWindowAnimations(animStyle);
        show();
    }

    /**
     * 设置隐藏的动画
     */
    public BaseDialog dismissAnim(BaseAnimatorSet dismissAnim) {
        mDismissAnim = dismissAnim;
        return this;
    }

    /**
     * 在给定时间后,自动消失
     */
    public BaseDialog autoDismiss(boolean autoDismiss) {
        mAutoDismiss = autoDismiss;
        return this;
    }

    /**
     * 对话框消失延时时间,毫秒值
     */
    public BaseDialog autoDismissDelay(long autoDismissDelay) {
        mAutoDismissDelay = autoDismissDelay;
        return this;
    }

    /**
     * 去除退出动画
     */
    public void removeDismissAnim() {
        mDismissAnim = null;
    }

    /**
     * 设置对话框宽度,占屏幕宽的比例0-1
     */
    public BaseDialog widthScale(float widthScale) {
        this.mWidthScale = widthScale;
        return this;
    }

    /**
     * 设置对话框高度,占屏幕宽的比例0-1
     */
    public BaseDialog heightScale(float heightScale) {
        mHeightScale = heightScale;
        return this;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsDismissAnim || mIsShowAnim || mAutoDismiss) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (mIsDismissAnim || mIsShowAnim || mAutoDismiss) {
            return;
        }
        super.onBackPressed();
    }
}
