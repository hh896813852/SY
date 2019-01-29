package com.edusoho.yunketang.widget.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseDialog;
import com.edusoho.yunketang.utils.BitmapUtil;
import com.edusoho.yunketang.utils.html.MyHtmlTagHandler;

import java.util.Arrays;

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
        if (!TextUtils.isEmpty(content)) {
            content = content.replace("font", "syfont");
            showHtml(contentView, content);
        }
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

    private static void showHtml(TextView textView, String content) {
        CharSequence html = Html.fromHtml(content, source -> {
            Drawable drawable = new BitmapDrawable(textView.getContext().getResources(), BitmapUtil.base64ToBitmap(source));
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return drawable;
        }, new MyHtmlTagHandler("syfont"));
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(html);
        textView.setText(spanBuilder);
        //设置可以点击超连接
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
