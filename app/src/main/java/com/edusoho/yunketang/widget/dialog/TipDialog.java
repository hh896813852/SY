package com.edusoho.yunketang.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseDialog;

/**
 * @author huhao on 2018/7/30
 */
public class TipDialog extends BaseDialog {
    private String content;
    private String title;

    public TipDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView titleView = findViewById(R.id.tipTitleView);
        TextView contentView = findViewById(R.id.tipContentView);
        ImageView closeImage = findViewById(R.id.closeImage);
        contentView.setText(content);
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            contentView.setText(content);
        }
        closeImage.setOnClickListener(v -> dismiss());
    }

    public TipDialog setTitleText(String title) {
        this.title = title;
        return this;
    }

    public TipDialog setContentText(String content) {
        this.content = content;
        return this;
    }
}
