package com.edusoho.yunketang.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ValidateImageView extends View {

    Context mContext;
    Bitmap  mBitmapBackground;    //背景图
    Bitmap  mBitmapVerified;      //验证图
    Paint   mPaint;
    float   mPortion;

    int mBackgroundWidth;
    int mBackgroundHeight;
    int mVerifiedWidth;
    int mVerifiedHeight;

    int mMoveX;
    int mMoveMax;

    public ValidateImageView(Context context) {
        super(context);
        init(context);
    }

    public ValidateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ValidateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmapBackground == null || mBitmapVerified == null) {
            return;
        }
        try {
            canvas.drawBitmap(mBitmapBackground, 0, 0, mPaint);
            canvas.drawBitmap(mBitmapVerified, mMoveX, 0, mPaint);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setResource(Bitmap bitmap, Bitmap verifiedBitmap, int width) {
        mPortion = bitmap.getWidth() * 1.0f / width;
        mBackgroundWidth = width;
        mBackgroundHeight = (int) (bitmap.getHeight() / mPortion);
        mBitmapBackground = Bitmap.createScaledBitmap(bitmap, mBackgroundWidth, mBackgroundHeight, false);
        mVerifiedWidth = (int) (verifiedBitmap.getWidth() / mPortion);
        mVerifiedHeight = (int) (verifiedBitmap.getHeight() / mPortion);
        mBitmapVerified = Bitmap.createScaledBitmap(verifiedBitmap, mVerifiedWidth, mVerifiedHeight, false);
        mMoveMax = mBackgroundWidth - mVerifiedWidth;
    }

    public void setMove(double percent) {
        if (percent >= 0 && percent <= 1) {
            mMoveX = (int) (mMoveMax * percent);
            invalidate();
        }
    }

    public void init() {
        mMoveX = 0;
    }

    public double getBitmapDeltaX() {
        return mMoveX * 1.0d / mBackgroundWidth;
    }
}
