package com.edusoho.yunketang.ui.common;

import android.app.Activity;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.base.annotation.Translucent;
import com.edusoho.yunketang.bean.PayParams;
import com.edusoho.yunketang.databinding.ActivityPaymentBinding;
import com.edusoho.yunketang.helper.PayHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.RequestCodeUtil;

/**
 * @author huhao on 2017/6/9 0009.
 */
@Translucent
@Layout(value = R.layout.activity_payment)
public class PaymentActivity extends BaseActivity<ActivityPaymentBinding> {
    public static final int FROM_PAYMENT_CODE = RequestCodeUtil.next();
    public static final String GOODS_TYPE = "goods_type";
    public static final String BUSINESS_ID = "business_id";
    public static final String MODULE_Id = "module_id";
    public static final String GOODS_NAME = "goods_name";
    public static final String GOODS_ID = "goods_id";
    public static final String GOODS_PRICE = "goods_price";
    private int goodsType;
    private int businessId;
    private int moduleId;
    private String goodsId;

    public ObservableField<String> goodsName = new ObservableField<>();
    public ObservableField<String> goodsPrice = new ObservableField<>();
    public ObservableField<Integer> payType = new ObservableField<>(PayParams.PAY_TYPE_WECHAT);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_from_bottom);
        getDataBinding().llView.startAnimation(animation);
    }

    private void initData() {
        goodsType = getIntent().getIntExtra(GOODS_TYPE, 0);
        businessId = getIntent().getIntExtra(BUSINESS_ID, 0);
        moduleId = getIntent().getIntExtra(MODULE_Id, 0);
        goodsId = getIntent().getStringExtra(GOODS_ID);

        goodsName.set(getIntent().getStringExtra(GOODS_NAME));
        goodsPrice.set("¥" + getIntent().getStringExtra(GOODS_PRICE));
    }

    /**
     * 关闭支付方式选择窗口
     */
    public void onPayTypePickCloseClick(View view) {
        finishActivity();
    }

    /**
     * 支付方式选择
     */
    public void onPayTypeSelectClick(View view) {
        switch (view.getTag().toString()) {
            case "1": // 微信
                payType.set(PayParams.PAY_TYPE_WECHAT);
                break;
            case "2": // 支付宝
                payType.set(PayParams.PAY_TYPE_ALIPAY);
                break;
        }
    }

    /**
     * 提交订单
     */
    public void onCommitOrderClick(View view) {
        SYDataTransport.create(payType.get() == PayParams.PAY_TYPE_WECHAT ? SYConstants.SY_WXPAY : SYConstants.SY_ALIPAY)
                .addParam("goodsType", goodsType) // 商品分类 1:视频，2:试卷
                .addParam("businessType", businessId)
                .addParam("moduleId", moduleId)
                .addParam("goodsName", goodsName.get())
                .addParam("goodsId", goodsId)
                .addProgressing(this, "正在创建订单，请稍后...")
                .execute(new SYDataListener<PayParams>() {

                    @Override
                    public void onSuccess(PayParams data) {
                        data.setPayType(payType.get());
                        PayHelper.getInstance().pay(PaymentActivity.this, data, isSuccess -> {
                            if (isSuccess) {
                                showSingleToast("支付成功！");
                                setResult(Activity.RESULT_OK);
                            } else {
                                showToast("支付失败！");
                            }
                            finish();
                        });
                    }
                }, PayParams.class);
    }

    /**
     * 底部退出动画
     */
    Animation animation = null;

    public void finishActivity() {
        if (null == animation) {
            animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    getDataBinding().layout.setBackgroundColor(Color.TRANSPARENT);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            getDataBinding().llView.startAnimation(animation);
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }
}
