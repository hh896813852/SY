package com.edusoho.yunketang.edu.order.payments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.bean.seting.PaymentSetting;
import com.edusoho.yunketang.edu.base.BaseActivity;
import com.edusoho.yunketang.edu.bean.MessageEvent;
import com.edusoho.yunketang.edu.bean.OrderInfo;
import com.edusoho.yunketang.edu.order.alipay.AlipayActivity;
import com.edusoho.yunketang.edu.utils.AppUtils;
import com.edusoho.yunketang.edu.utils.Constants;
import com.edusoho.yunketang.edu.utils.InputUtils;
import com.edusoho.yunketang.edu.utils.SettingHelper;
import com.edusoho.yunketang.edu.utils.SharedPreferencesHelper;
import com.edusoho.yunketang.edu.utils.ToastUtils;
import com.edusoho.yunketang.edu.widget.ESAlertDialog;
import com.edusoho.yunketang.ui.course.CoursePlayerActivity;
import com.edusoho.yunketang.utils.ProgressDialogUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by DF on 2017/4/7.
 * 支付选择界面
 */
public class PaymentsActivity extends BaseActivity<PaymentsContract.Presenter> implements View.OnClickListener, PaymentsContract.View {
    private static final String ORDER_INFO = "order_info";
    private static final String COUPON_POSITION_IN_COUPONS = "position";
    private static final String TARGET_TYPE = "target_type";
    private static final String TARGET_ID = "target_id";
    private static final String ORDER_SN = "order_sn";
    private static final String PRICE = "price";
    private static final String ACTIVITY_PURPOSE = "activity_purpose";
    private static final int CREATE = 0;
    private static final int PAID = 1;
    private static final int FULL_COIN_PAYABLE = 1;

    private Toolbar mToolbar;
    private LinearLayout mPaymentPanel;
    private LinearLayout mPaymentPanel1;
    private View mAlipay;
    private View mWxPay;
    private TextView mPay;
    private TextView mVirtualCoin;
    private TextView mDiscount;
    private TextView mBalance;
    private TextView mAvailableName;
    private Dialog mDialog;
    private EditText mInputPw;
    private View mAvailableLayout;

    private OrderInfo mOrderInfo;
    private int mPosition;
    private int mTargetId;
    private String mTargetType;
    private String mOrderSn;
    private float mPrice;
    private int mPurpose;

    public static void launch(Context context, OrderInfo orderInfo, int position) {
        Intent intent = new Intent(context, PaymentsActivity.class);
        intent.putExtra(ORDER_INFO, orderInfo);
        intent.putExtra(COUPON_POSITION_IN_COUPONS, position);
        intent.putExtra(ACTIVITY_PURPOSE, CREATE);
        context.startActivity(intent);
    }

    public static void launchForUnfinishedOrder(Context context, String targetType, int targetId, String orderSn, float price) {
        Intent intent = new Intent(context, PaymentsActivity.class);
        intent.putExtra(TARGET_TYPE, targetType);
        intent.putExtra(TARGET_ID, targetId);
        intent.putExtra(ORDER_SN, orderSn);
        intent.putExtra(PRICE, price);
        intent.putExtra(ACTIVITY_PURPOSE, PAID);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_way);
        mOrderInfo = (OrderInfo) getIntent().getSerializableExtra(ORDER_INFO);
        mPosition = getIntent().getIntExtra(COUPON_POSITION_IN_COUPONS, -1);
        mTargetId = getIntent().getIntExtra(TARGET_ID, 0);
        mTargetType = getIntent().getStringExtra(TARGET_TYPE);
        mOrderSn = getIntent().getStringExtra(ORDER_SN);
        mPrice = getIntent().getFloatExtra(PRICE, 0f);
        mPurpose = getIntent().getIntExtra(ACTIVITY_PURPOSE, CREATE);
        init();
    }

    private void init() {
        mPaymentPanel = findViewById(R.id.rg_pay_way);
        mPaymentPanel1 = findViewById(R.id.rg_pay_way1);
        mAlipay = findViewById(R.id.iv_alipay);
        mWxPay = findViewById(R.id.iv_wx_pay);
        mAvailableLayout = findViewById(R.id.available_layout);
        mToolbar = findViewById(R.id.tb_toolbar);
        mDiscount = findViewById(R.id.tv_discount);
        mBalance = findViewById(R.id.tv_available_balance);
        mAvailableName = findViewById(R.id.tv_available_name);
        mPay = findViewById(R.id.tv_pay);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        if (mOrderInfo != null) {
            mPresenter = new PaymentsPresenter(this, mOrderInfo, mPosition);
        } else {
            mPresenter = new PaymentsPresenter(this, mTargetId, mTargetType, mPrice);
        }
        mPresenter.subscribe();
    }

    private void initEvent() {
        mWxPay.setOnClickListener(this);
        mAlipay.setOnClickListener(this);
        mVirtualCoin.setOnClickListener(this);
        mPay.setOnClickListener(this);
        for (int i = 0; i < mPaymentPanel.getChildCount(); i++) {
            View child = mPaymentPanel.getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                child.performClick();
                break;
            }
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onDisplay(OrderInfo orderInfo) {
        mOrderInfo = orderInfo;
        PaymentSetting paymentSetting = SettingHelper.getSetting(PaymentSetting.class, this,
                SharedPreferencesHelper.SchoolSetting.PAYMENT_SETTING);
        if (paymentSetting != null && !paymentSetting.isNull()) {
            mAlipay.setVisibility(paymentSetting.getAlipayEnabled() ? View.VISIBLE : View.GONE);
            mWxPay.setVisibility(paymentSetting.getWxpayEnabled() ? View.VISIBLE : View.GONE);
        } else {
            mAlipay.setVisibility(View.VISIBLE);
            mWxPay.setVisibility(View.GONE);
        }
        if (mAlipay.getVisibility() == View.VISIBLE && mWxPay.getVisibility() == View.VISIBLE) {
            mPaymentPanel1.setVisibility(View.VISIBLE);
            mVirtualCoin = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_virtual_coin,
                    mPaymentPanel1, false);
            mPaymentPanel1.addView(mVirtualCoin);
        } else if (mAlipay.getVisibility() == View.VISIBLE || mWxPay.getVisibility() == View.VISIBLE) {
            mPaymentPanel1.setVisibility(View.GONE);
            mVirtualCoin = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_virtual_coin,
                    mPaymentPanel, false);
            mPaymentPanel.addView(mVirtualCoin);
        } else {
            mPaymentPanel1.setVisibility(View.GONE);
            mVirtualCoin = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_virtual_coin,
                    mPaymentPanel, false);
            mPaymentPanel.addView(mVirtualCoin);
        }
        if (FULL_COIN_PAYABLE == mOrderInfo.fullCoinPayable) {
            mVirtualCoin.setVisibility(View.VISIBLE);
            mAvailableLayout.setVisibility(View.VISIBLE);
            mVirtualCoin.setText(mOrderInfo.coinName.length() != 0 ? mOrderInfo.coinName : getString(R.string.virtual_coin_pay));
            mAvailableName.setText(String.format(getString(R.string.available_balance),
                    mOrderInfo.coinName.length() != 0 ? mOrderInfo.coinName : getString(R.string.virtual_coin)));
            if (mOrderInfo.account.cash == 0) {
                mBalance.setText("0");
            } else {
                mBalance.setText(String.format("%.2f", mOrderInfo.account.cash));
            }
        } else {
            mPaymentPanel1.setVisibility(View.GONE);
            mVirtualCoin.setVisibility(View.GONE);
        }
        initEvent();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_alipay) {
            selectPayment(v);
            clickThirdPartyPay();
        } else if (id == R.id.tv_virtual_coin) {
            selectPayment(v);
            clickVirtual();
        } else if (id == R.id.iv_wx_pay) {
            selectPayment(v);
            clickThirdPartyPay();
        }

        if (id == R.id.tv_pay) {
            goPay();
        }
    }

    private void selectPayment(View clickView) {
        for (int i = 0; i < mPaymentPanel.getChildCount(); i++) {
            View child = mPaymentPanel.getChildAt(i);
            child.setSelected(false);
        }
        for (int i = 0; i < mPaymentPanel1.getChildCount(); i++) {
            View child = mPaymentPanel1.getChildAt(i);
            child.setSelected(false);
        }
        clickView.setSelected(true);
    }

    private void clickThirdPartyPay() {
        mPay.setEnabled(true);
        mPay.setText(R.string.go_pay);
        mPay.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        mDiscount.setText(mOrderInfo.getSumPriceByTypeWithUnit(OrderInfo.RMB));
    }

    private void clickVirtual() {
        if (FULL_COIN_PAYABLE == mOrderInfo.fullCoinPayable && mOrderInfo.getSumPriceByType(OrderInfo.COIN) > mOrderInfo.account.cash) {
            mPay.setBackgroundColor(ContextCompat.getColor(this, R.color.secondary_font_color));
            mPay.setText(R.string.insufficient_balance);
            mPay.setEnabled(false);
        }
        if (FULL_COIN_PAYABLE == mOrderInfo.fullCoinPayable) {
            mDiscount.setText(mOrderInfo.getSumPriceByTypeWithUnit(OrderInfo.COIN));
        }
    }

    private void goPay() {
        if (mAlipay.isSelected()) {
            showProcessDialog();
            if (mPurpose == CREATE) {
                mPresenter.createOrderAndPay(PaymentsPresenter.ALIPAY, null, -1);
            } else if (mPurpose == PAID) {
                mPresenter.payOrderByRMB(Constants.TradeGetway.ALIPAY, mOrderSn);
            }
        } else if (mWxPay.isSelected()) {
            if (!AppUtils.isInstall(this, "com.tencent.mm")) {
                ToastUtils.show(this, "未安装微信客户端");
                return;
            }
            showProcessDialog();
            if (mPurpose == CREATE) {
                mPresenter.createOrderAndPay(PaymentsPresenter.WXPAY, null, -1);
            } else if (mPurpose == PAID) {
                mPresenter.payOrderByRMB(Constants.TradeGetway.WECHAT_PAY, mOrderSn);
            }
        } else {
            showPasswordDialog();
        }
    }

    private void showPasswordDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(this, R.style.dialog_custom);
            mDialog.setContentView(R.layout.dialog_input_pay_pw);
            mDialog.setCanceledOnTouchOutside(true);
            Window window = mDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = getResources().getDisplayMetrics().widthPixels;
                window.setAttributes(lp);
                window.setGravity(Gravity.BOTTOM);
            }
            mInputPw = mDialog.findViewById(R.id.et_input_pw);
            mInputPw.setOnEditorActionListener(getOnEditorActionListener());
        }
        InputUtils.showKeyBoard(mInputPw, this);
        mDialog.show();
    }

    @NonNull
    private TextView.OnEditorActionListener getOnEditorActionListener() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String pw = mInputPw.getText().toString().trim();
                if (mOrderInfo.hasPayPassword != 1) {
                    showToast(R.string.unset_pw_hint);
                    return true;
                }
                if (pw.length() < 5) {
                    showToast(R.string.pw_long_wrong_hint);
                    return true;
                }
                showProcessDialog();
                if (mPurpose == CREATE) {
                    mPresenter.createOrderAndPay(PaymentsPresenter.COIN, mInputPw.getText().toString().trim(),
                            mOrderInfo.getSumPriceByType(OrderInfo.COIN) > 0 ? mOrderInfo.getSumPriceByType(OrderInfo.COIN) : 0);
                } else if (mPurpose == PAID) {
                    mPresenter.payOrderByCoin(mOrderSn, mOrderInfo.getSumPriceByType(OrderInfo.COIN) + "", mInputPw.getText().toString().trim());
                }
                mDialog.dismiss();
                return true;
            }
        };
    }

    @Override
    public void onAlipay(final String data, String orderSn) {
        AlipayActivity.launch(this, data, mOrderInfo.targetId, mOrderInfo.targetType, orderSn, PaymentsPresenter.ALIPAY);
    }


    @Override
    public void onWxPay(String url, String orderSn) {
        AlipayActivity.launchForResult(this, url, orderSn, PaymentsPresenter.WXPAY);
    }

    protected void showProcessDialog() {
        ProgressDialogUtil.showProgress(this);
    }

    protected void hideProcessDialog() {
        ProgressDialogUtil.hideProgress();
    }

    @Override
    public void showLoadDialog(boolean isShow) {
        if (isShow) {
            showProcessDialog();
        } else {
            hideProcessDialog();
        }
    }

    @Override
    public void onPayFinish() {
        if (CREATE == mPurpose && "course".equals(mOrderInfo.targetType)) {
            Intent intent = new Intent(this, CoursePlayerActivity.class);
            intent.putExtra(CoursePlayerActivity.COURSE_PROJECT_ID, mOrderInfo.targetId);
        }
        finishAffinity();
    }

    @Override
    public void updateOrderList(String orderSn) {
        EventBus.getDefault().postSticky(new MessageEvent<>(orderSn, MessageEvent.UPDATE_ORDER_VIEW));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AlipayActivity.CODE_WXPAY_FINISH == resultCode) {
            ESAlertDialog
                    .newInstance(null, getString(R.string.confirm_payment_finish),
                            getString(R.string.pay_finished), getString(R.string.pay_error_and_repay))
                    .setConfirmListener(new ESAlertDialog.DialogButtonClickListener() {
                        @Override
                        public void onClick(DialogFragment dialog) {
                            onPayFinish();
                        }
                    })
                    .setCancelListener(new ESAlertDialog.DialogButtonClickListener() {
                        @Override
                        public void onClick(DialogFragment dialog) {
                            dialog.dismiss();
                        }
                    })
                    .show(getSupportFragmentManager(), "ESAlertDialog");
        }
    }

    @Override
    public void showWxPayNotSupportToast() {
        ToastUtils.show(this, getString(R.string.wxpay_not_support));
    }
}
