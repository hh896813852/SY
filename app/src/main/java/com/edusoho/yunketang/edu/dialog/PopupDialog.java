package com.edusoho.yunketang.edu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.edusoho.yunketang.R;

public class PopupDialog extends Dialog {

    protected TextView popTitle;
    protected TextView popMessage;

    public static final int CANCEL = 0001;
    public static final int OK     = 0002;

    protected PopupClickListener mClickListener;

    public PopupDialog(Context context, int style, int theme, PopupClickListener clickListener) {
        super(context, theme);
        setContentView(style);
        mClickListener = clickListener;
        initView();
    }

    private TextView mPopupOkBtn;
    private TextView mPopupCancelBtn;

    protected void initView() {
        popTitle = (TextView) findViewById(R.id.popup_title);
        popMessage = (TextView) findViewById(R.id.popup_message);
        mPopupOkBtn = (TextView) findViewById(R.id.popup_ok_btn);
        mPopupCancelBtn = (TextView) findViewById(R.id.popup_cancel_btn);

        popMessage.setMovementMethod(new ScrollingMovementMethod());
        mPopupOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.onClick(OK);
                }
                dismiss();
            }
        });
        mPopupCancelBtn = (TextView) findViewById(R.id.popup_cancel_btn);
        if (mPopupCancelBtn != null) {
            mPopupCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null) {
                        mClickListener.onClick(CANCEL);
                    }
                    dismiss();
                }
            });
        }
    }

    public void setOkListener(PopupClickListener clickListener) {
        mClickListener = clickListener;
        mPopupOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.onClick(OK);
                }
                dismiss();
            }
        });
    }

    public void setTitle(String title) {
        popTitle.setText(title);
    }

    public void setMessage(String message) {
        popMessage.setText(message);
    }

    public static PopupDialog createMuilt(
            Context context, String title, String msg, PopupClickListener clickListener) {
        PopupDialog dlg = new PopupDialog(
                context, R.layout.popup_muilt, R.style.loadDlgTheme, clickListener);
        dlg.setTitle(title);
        dlg.setMessage(msg);
        return dlg;
    }

    public void setCancelText(String text) {
        mPopupCancelBtn.setText(text);
    }

    public void setOkText(String text) {
        mPopupOkBtn.setText(text);
    }

    public static PopupDialog createNormal(Context context, String title, String msg) {
        PopupDialog dlg = new PopupDialog(
                context, R.layout.popup, R.style.loadDlgTheme, null);
        dlg.setTitle(title);
        dlg.setMessage(msg);

        return dlg;
    }

    public interface PopupClickListener {
        void onClick(int button);
    }
}
