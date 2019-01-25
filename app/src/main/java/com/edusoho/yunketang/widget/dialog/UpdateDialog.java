package com.edusoho.yunketang.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseDialog;

/**
 * @author huhao on 2018/7/30
 */
public class UpdateDialog extends BaseDialog {
    private boolean isForce;
    private String version;
    private String content;
    private OnClickListener simpleClickListener;

    public UpdateDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView updateVersion = findViewById(R.id.updateVersion);
        TextView contentView = findViewById(R.id.updateContentView);
        View line = findViewById(R.id.line);
        TextView notUpdateView = findViewById(R.id.notUpdateView);
        TextView updateView = findViewById(R.id.updateView);
        updateVersion.setText("发现软件新版本  V" + version);
        contentView.setText(content);
        if (isForce) {
            notUpdateView.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            updateView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_theme_color_bottom_corner_6));
        }
        if (simpleClickListener != null) {
            notUpdateView.setOnClickListener(v -> simpleClickListener.OnNotUpdateClicked(this));
            updateView.setOnClickListener(v -> simpleClickListener.OnUpdateClicked(this));
        }
    }

    public UpdateDialog setVersionText(String version) {
        this.version = version;
        return this;
    }

    public UpdateDialog setContentText(String content) {
        this.content = content;
        return this;
    }

    public UpdateDialog setOnClickListener(OnClickListener simpleClickListener) {
        this.simpleClickListener = simpleClickListener;
        return this;
    }

    public UpdateDialog setIsForce(boolean isForce) {
        this.isForce = isForce;
        return this;
    }

    public interface OnClickListener {
        void OnNotUpdateClicked(UpdateDialog dialog);

        void OnUpdateClicked(UpdateDialog dialog);
    }
}
