package com.edusoho.yunketang.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseDialog;
import com.edusoho.yunketang.helper.PicLoadHelper;

/**
 * @author huhao on 2018/7/30
 */
public class SXYDialog extends BaseDialog {
    private String percent;
    private TextView contentView1;
    private TextView contentView2;
    private TextView contentView3;
    private ImageView scoreBgImage;
    private OnClickListener onClickListener;

    public SXYDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView1 = findViewById(R.id.content1);
        contentView2 = findViewById(R.id.content2);
        contentView3 = findViewById(R.id.content3);
        scoreBgImage = findViewById(R.id.scoreBgImage);
        Button doAgainBtn = findViewById(R.id.doAgainBtn);
        if (onClickListener != null) {
            doAgainBtn.setOnClickListener(v -> onClickListener.doAgainClicked(this));
        }
        setContent();
    }

    private void setContent() {
        if (Integer.valueOf(percent) >= 80) {
            contentView1.setText("小元的主人真棒！");
            contentView2.setText("本次答题正确率为" + percent + "%");
            contentView3.setText("继续加油哦！！！");
            scoreBgImage.setImageResource(R.drawable.bg_high_score);
            return;
        }
        if (Integer.valueOf(percent) >= 40) {
            contentView1.setText("小元的主人好样的！");
            contentView2.setText("本次答题正确率为" + percent + "%");
            contentView3.setText("不要灰心，小元陪你一起努力！");
            scoreBgImage.setImageResource(R.drawable.bg_middle_score);
        } else {
            contentView1.setText("小元的主人偷懒！");
            contentView2.setText("本次答题正确率为" + percent + "%");
            contentView3.setText("要加油哦！小元陪你一起努力！");
            scoreBgImage.setImageResource(R.drawable.bg_low_score);
        }
    }

    public SXYDialog setCorrectPercent(String percent) {
        this.percent = percent;
        return this;
    }

    public SXYDialog setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public interface OnClickListener {
        void doAgainClicked(SXYDialog dialog);
    }
}
