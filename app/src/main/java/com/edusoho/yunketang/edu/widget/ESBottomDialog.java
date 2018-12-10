package com.edusoho.yunketang.edu.widget;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.edu.base.BasePresenter;
import com.edusoho.yunketang.edu.base.BaseView;

/**
 * Created by JesseHuang on 2017/4/11.
 */
public class ESBottomDialog<T extends BasePresenter> extends DialogFragment implements BaseView<T> {

    BottomDialogContentView contentView;
    protected FrameLayout flContent;
    protected TextView    tvConfirm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_bottom, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flContent = view.findViewById(R.id.content);
        tvConfirm = view.findViewById(R.id.tv_confirm);
        if (contentView != null) {
            flContent.addView(contentView.getContentView(flContent));
            contentView.setButtonState(tvConfirm);
            tvConfirm.setVisibility(contentView.showConfirm() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void setContent(BottomDialogContentView view) {
        contentView = view;
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void close() {
        dismiss();
    }

    public interface BottomDialogContentView {

        View getContentView(ViewGroup parentView);

        void setButtonState(TextView btn);

        boolean showConfirm();
    }
}
