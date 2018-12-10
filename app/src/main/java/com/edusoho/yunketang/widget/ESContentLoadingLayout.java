package com.edusoho.yunketang.widget;


import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edusoho.yunketang.R;

public class ESContentLoadingLayout extends RelativeLayout {
    private ContentLoadingProgressBar mProgressBar;
    private TextView                  mError;
    private TextView                  mReload;
    private ImageView                 mImageError;

    public ESContentLoadingLayout(Context context) {
        super(context);
    }

    public ESContentLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ESContentLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.content_loading_layout, this);
        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.pb_loading);
        mError = (TextView) findViewById(R.id.tv_error);
        mReload = (TextView) findViewById(R.id.tv_reload);
        mImageError = (ImageView) findViewById(R.id.iv_error);
    }

    public void showLoading() {
        mProgressBar.show();
        mError.setVisibility(GONE);
        mReload.setVisibility(GONE);
        mImageError.setVisibility(GONE);
    }

    public void hideLoading() {
        this.setVisibility(GONE);
    }

    public void showError() {
        mProgressBar.hide();
        mError.setVisibility(VISIBLE);
        mReload.setVisibility(VISIBLE);
        mImageError.setVisibility(VISIBLE);
    }

    public void setReloadClickListener(OnClickListener onClickListener) {
        mReload.setOnClickListener(onClickListener);
    }
}
