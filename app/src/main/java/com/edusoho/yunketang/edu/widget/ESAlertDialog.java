package com.edusoho.yunketang.edu.widget;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.edu.utils.StringUtils;

public class ESAlertDialog extends DialogFragment {
    private static final String TITLE               = "title";
    private static final String MESSAGE             = "message";
    private static final String POSITIVE_MESSAGE_ID = "positive_message_id";
    private static final String NEGATIVE_MESSAGE_ID = "negative_message_id";
    private String                    mTitleId;
    private String                    mMessageId;
    private String                    mPositiveMessageId;
    private String                    mNegativeMessageId;
    private DialogButtonClickListener mConfirmListener;
    private DialogButtonClickListener mCancelListener;
    private TextView                  mTitle;
    private TextView                  mMessage;
    private TextView                  mConfirm;
    private TextView                  mCancel;
    private boolean                   isCancel;
    private int                       mPositiveTextColorId;
    private int                       mNegativeTextColorId;

    public ESAlertDialog setConfirmListener(DialogButtonClickListener l) {
        this.mConfirmListener = l;
        return this;
    }

    public ESAlertDialog setCancelListener(DialogButtonClickListener l) {
        this.mCancelListener = l;
        return this;
    }

    public ESAlertDialog setPositiveTextColor(@ColorRes int id) {
        this.mPositiveTextColorId = id;
        return this;
    }

    public ESAlertDialog setNegativeTextColor(@ColorRes int id) {
        this.mNegativeTextColorId = id;
        return this;
    }

    public ESAlertDialog setCanceledOnTouchOutside(boolean cancel) {
        this.isCancel = cancel;
        return this;
    }


    public ESAlertDialog setDialogCancelable(boolean cancel) {
        setCancelable(cancel);
        return this;
    }

    public static ESAlertDialog newInstance(String titleId, String messageId, String positiveMessageId,
                                            String negativeMessageId) {
        Bundle args = new Bundle();
        ESAlertDialog fragment = new ESAlertDialog();
        args.putString(TITLE, titleId);
        args.putString(MESSAGE, messageId);
        args.putString(POSITIVE_MESSAGE_ID, positiveMessageId);
        args.putString(NEGATIVE_MESSAGE_ID, negativeMessageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleId = getArguments().getString(TITLE);
        mMessageId = getArguments().getString(MESSAGE);
        mPositiveMessageId = getArguments().getString(POSITIVE_MESSAGE_ID);
        mNegativeMessageId = getArguments().getString(NEGATIVE_MESSAGE_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(isCancel);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_shape_background);
        return inflater.inflate(R.layout.es_alert_dialog_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.common_dialog_width), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = view.findViewById(R.id.tv_title);
        mMessage = view.findViewById(R.id.tv_message);
        mConfirm = view.findViewById(R.id.btn_confirm);
        mCancel = view.findViewById(R.id.btn_cancel);
        if (StringUtils.isEmpty(mPositiveMessageId) && StringUtils.isEmpty(mNegativeMessageId)) {
            throw new RuntimeException("PositiveMessageId or NegativeMessageId can be null!");
        }
        if (this.mConfirmListener == null && mCancelListener == null) {
            throw new RuntimeException("ConfirmListener or mCancelListener can be null!");
        }

        if (!StringUtils.isEmpty(mTitleId)) {
            mTitle.setText(mTitleId);
            mTitle.setVisibility(View.VISIBLE);
        }
        if (!StringUtils.isEmpty(mMessageId)) {
            mMessage.setText(mMessageId);
            mMessage.setVisibility(View.VISIBLE);
        }
        if (!StringUtils.isEmpty(mPositiveMessageId) && this.mConfirmListener != null) {
            mConfirm.setText(mPositiveMessageId);
            mConfirm.setVisibility(View.VISIBLE);
            mConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mConfirmListener.onClick(ESAlertDialog.this);
                }
            });
        }
        if (!StringUtils.isEmpty(mNegativeMessageId) && this.mCancelListener != null) {
            mCancel.setText(mNegativeMessageId);
            mCancel.setVisibility(View.VISIBLE);
            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCancelListener.onClick(ESAlertDialog.this);
                }
            });
        }
        if (mPositiveTextColorId != 0) {
            mConfirm.setTextColor(getActivity().getResources().getColor(mPositiveTextColorId));
        }
        if (mNegativeTextColorId != 0) {
            mCancel.setTextColor(getActivity().getResources().getColor(mNegativeTextColorId));
        }
    }


    public interface DialogButtonClickListener {
        void onClick(DialogFragment dialog);
    }
}
