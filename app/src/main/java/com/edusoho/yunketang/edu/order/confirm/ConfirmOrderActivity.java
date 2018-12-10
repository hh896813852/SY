package com.edusoho.yunketang.edu.order.confirm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.bean.seting.PaymentSetting;
import com.edusoho.yunketang.edu.base.BaseActivity;
import com.edusoho.yunketang.edu.bean.Classroom;
import com.edusoho.yunketang.edu.bean.CourseSet;
import com.edusoho.yunketang.edu.bean.OrderInfo;
import com.edusoho.yunketang.edu.order.dialog.coupons.CouponsDialog;
import com.edusoho.yunketang.edu.order.payments.PaymentsActivity;
import com.edusoho.yunketang.edu.utils.AppUtils;
import com.edusoho.yunketang.edu.utils.SettingHelper;
import com.edusoho.yunketang.edu.utils.SharedPreferencesHelper;
import com.edusoho.yunketang.edu.utils.ToastUtils;
import com.edusoho.yunketang.utils.ProgressDialogUtil;

/**
 * Created by DF on 2017/3/25.
 * 确认订单界面
 */
public class ConfirmOrderActivity extends BaseActivity<ConfirmOrderContract.Presenter>
        implements View.OnClickListener, ConfirmOrderContract.View, CouponsDialog.ModifyView {

    private static final String COURSE_SET_ID = "course_set_id";
    private static final String COURSE_ID = "course_id";

    private Toolbar mToolbar;
    protected ImageView mCourseImg;
    private TextView mCourseProjectTitle;
    private TextView mPriceTextView;
    protected TextView mCourseProjectFrom;
    private ViewGroup mRlCoupon;
    private TextView mCouponValueTextView;
    private View mPay;
    private TextView mTotalPriceTextView;
    private RelativeLayout rlVIPPanel;
    private TextView tvVIPName;
    private TextView tvVIPPrice;
    private RelativeLayout rlCourseOrClassPanel;

    private int mCourseSetId;
    private int mCourseId;
    private OrderInfo.Coupon mSelectedCoupon;
    private OrderInfo mOrderInfo;
    private CouponsDialog mCouponsDialog;
    private int mCouponPosition;

    public static void launch(Context context, int courseSetId, int courseId) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        intent.putExtra(COURSE_SET_ID, courseSetId);
        intent.putExtra(COURSE_ID, courseId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        showProcessDialog();
        Intent intent = getIntent();
        mCourseSetId = intent.getIntExtra(COURSE_SET_ID, 0);
        mCourseId = intent.getIntExtra(COURSE_ID, 0);
        initView();
        initEvent();
    }

    protected void initView() {
        mCourseImg = findViewById(R.id.iv_course_image);
        mCourseProjectTitle = findViewById(R.id.tv_title);
        mPriceTextView = findViewById(R.id.tv_price);
        mCourseProjectFrom = findViewById(R.id.tv_from_course);
        mRlCoupon = findViewById(R.id.rl_coupon);
        mCouponValueTextView = findViewById(R.id.tv_coupon_value);
        mPay = findViewById(R.id.tv_pay);
        rlVIPPanel = findViewById(R.id.rl_vip_panel);
        rlCourseOrClassPanel = findViewById(R.id.rl_course_or_class_panel);
        tvVIPName = findViewById(R.id.tv_vip_name);
        tvVIPPrice = findViewById(R.id.tv_vip_price);
        mTotalPriceTextView = findViewById(R.id.tv_total_price);
        mToolbar = findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        initData();
    }

    protected void initData() {
        mPresenter = new ConfirmOrderPresenter(this, mCourseSetId, mCourseId);
        mPresenter.subscribe();
    }

    private void initEvent() {
        mRlCoupon.setOnClickListener(this);
        mPay.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void showPriceView(OrderInfo orderInfo) {
        mOrderInfo = orderInfo;
        if ("vip".equals(mOrderInfo.targetType)) {
            rlVIPPanel.setVisibility(View.VISIBLE);
            rlCourseOrClassPanel.setVisibility(View.GONE);
            String title = mOrderInfo.title + " x️ ";
            String vipName = String.format("%s %s %s", title, mOrderInfo.duration, AppUtils.getVIPUnitNameByType(mOrderInfo.unitType));
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(vipName);
            CharacterStyle red = new ForegroundColorSpan(Color.RED);
            int start = title.length();
            int end = title.length() + (mOrderInfo.duration + "").length();
            spannableStringBuilder.setSpan(red, start + 1, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvVIPName.setText(spannableStringBuilder);
            tvVIPPrice.setText(mOrderInfo.getPriceWithUnit());
        } else {
            rlVIPPanel.setVisibility(View.GONE);
            rlCourseOrClassPanel.setVisibility(View.VISIBLE);
        }
        mCourseProjectTitle.setText(mOrderInfo.title);
        mPriceTextView.setText(mOrderInfo.getPriceWithUnit());
        if (orderInfo.availableCoupons != null && orderInfo.availableCoupons.size() != 0) {
            mSelectedCoupon = orderInfo.availableCoupons.get(0);
            mSelectedCoupon.isSelector = true;
            showCouponPrice(mSelectedCoupon);
            mOrderInfo.check(mSelectedCoupon);
        } else {
            mOrderInfo.check(null);
        }
        mTotalPriceTextView.setText(String.format(getString(R.string.order_sum_price), mOrderInfo.getSumPriceWithUnit()));
    }

    private void showCouponPrice(OrderInfo.Coupon coupon) {
        mRlCoupon.setVisibility(View.VISIBLE);
        if (coupon != null) {
            mCouponValueTextView.setText(coupon.toString(mOrderInfo));
        } else {
            mCouponValueTextView.setText(String.format(getString(R.string.no_coupon)));
        }
    }

    @Override
    public void showTopView(CourseSet courseSet) {
        Glide.with(this).load(courseSet.cover.large).placeholder(R.drawable.bg_load_default_4x3).error(R.drawable.bg_load_default_4x3).into(mCourseImg);
        mCourseProjectFrom.setText(courseSet.title);
    }

    @Override
    public void showTopView(Classroom classroom) {

    }

    @Override
    public void showToastAndFinish(int content) {
        showToast(content);
        finish();
    }

    @Override
    public void showToastAndFinish(String content) {
        showToast(content);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_coupon) {
            showCouponDialog();
        } else if (id == R.id.tv_pay) {
            PaymentSetting paymentSetting = SettingHelper.getSetting(PaymentSetting.class, this, SharedPreferencesHelper.SchoolSetting.PAYMENT_SETTING);
            if (paymentSetting != null && paymentSetting.isNull()) {
                PaymentsActivity.launch(this, mOrderInfo, mCouponPosition);
            } else if (paymentSetting != null && !paymentSetting.isNull()
                    && !paymentSetting.getAlipayEnabled()
                    && !paymentSetting.getWxpayEnabled()
                    && 1 != mOrderInfo.fullCoinPayable) {
                ToastUtils.show(this, "没有设置任何支付方式，请联系管理员");
            } else {
                PaymentsActivity.launch(this, mOrderInfo, mCouponPosition);
            }
        }
    }

    private void showCouponDialog() {
        if (mCouponsDialog == null) {
            mCouponsDialog = new CouponsDialog();
            mCouponsDialog.setData(mOrderInfo.availableCoupons, mOrderInfo);
        }
        mCouponsDialog.show(getSupportFragmentManager(), "CouponsDialog");
    }

    @Override
    public void showProcessDialog(boolean isShow) {
        if (isShow) {
            showProcessDialog();
        } else {
            hideProcessDialog();
        }
    }

    protected void showProcessDialog() {
        ProgressDialogUtil.showProgress(this);
    }

    protected void hideProcessDialog() {
        ProgressDialogUtil.hideProgress();
    }

    @Override
    public void setPriceView(OrderInfo.Coupon coupon) {
        mCouponPosition = mOrderInfo.availableCoupons.indexOf(coupon);
        showCouponPrice(coupon);
        mOrderInfo.check(coupon);
        mTotalPriceTextView.setText(String.format(getString(R.string.order_sum_price), mOrderInfo.getSumPriceWithUnit()));
    }
}
