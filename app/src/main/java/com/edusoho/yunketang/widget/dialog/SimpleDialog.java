package com.edusoho.yunketang.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseDialog;

/**
 * @author huhao on 2018/7/30
 */
public class SimpleDialog extends BaseDialog {
    private String content;
    private String leftBtnText;
    private String rightBtnText;
    private OnSimpleClickListener simpleClickListener;

    public SimpleDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView contentView = findViewById(R.id.contentView);
        TextView leftView = findViewById(R.id.leftView);
        TextView rightView = findViewById(R.id.rightView);
        contentView.setText(content);
        if (!TextUtils.isEmpty(leftBtnText)) {
            leftView.setText(leftBtnText);
        }
        if (!TextUtils.isEmpty(rightBtnText)) {
            rightView.setText(rightBtnText);
        }
        if (simpleClickListener != null) {
            leftView.setOnClickListener(v -> simpleClickListener.OnLeftBtnClicked(this));
            rightView.setOnClickListener(v -> simpleClickListener.OnRightBtnClicked(this));
        }
    }

    public SimpleDialog setContentText(String content) {
        this.content = content;
        return this;
    }

    public SimpleDialog setLeftBtnText(String leftBtnText) {
        this.leftBtnText = leftBtnText;
        return this;
    }

    public SimpleDialog setRightBtnText(String rightBtnText) {
        this.rightBtnText = rightBtnText;
        return this;
    }

    public SimpleDialog setOnSimpleClickListener(OnSimpleClickListener simpleClickListener) {
        this.simpleClickListener = simpleClickListener;
        return this;
    }

    public interface OnSimpleClickListener {
        void OnLeftBtnClicked(SimpleDialog dialog);

        void OnRightBtnClicked(SimpleDialog dialog);
    }
}
