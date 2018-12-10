package com.edusoho.yunketang.ui.testlib;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.BaseRecycleAdapter;
import com.edusoho.yunketang.adapter.SuperViewHolder;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.GridItemDecoration;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.PayParams;
import com.edusoho.yunketang.databinding.ActivityPastExamBinding;
import com.edusoho.yunketang.helper.PayHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.activity_past_exam, title = "历年真题")
public class PastExamActivity extends BaseActivity<ActivityPastExamBinding> {

    public ObservableField<Integer> payType = new ObservableField<>(PayParams.PAY_TYPE_WECHAT);

    private int pageNo;
    private List<Integer> list = new ArrayList<>();
    public BaseRecycleAdapter adapter = new BaseRecycleAdapter() {

        @Override
        public void onBindViewHolder(@NonNull SuperViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);

        }
    };
    public BaseRecycleAdapter.OnItemClickListener onItemClick = (view, position) -> {
//        Intent intent = new Intent(PastExamActivity.this, AnswerReportActivity.class);
//        startActivity(intent);
        showPayTypePickView();
    };
    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (getDataBinding() != null) {
            getDataBinding().swipeView.setRefreshing(true);
            pageNo = 0;
            loadData();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化
        initView();
        // 加载数据
        loadData();
    }

    /**
     * 初始化
     */
    private void initView() {
        // 设置布局管理器
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        getDataBinding().recycleView.setLayoutManager(layoutManager);
        // 设置item间距
        int verticalSpace = DensityUtil.dip2px(PastExamActivity.this, 10);
        int horizontalSpace = DensityUtil.dip2px(PastExamActivity.this, 10);
        getDataBinding().recycleView.addItemDecoration(new GridItemDecoration(verticalSpace, horizontalSpace));
        // 初始化适配器
        adapter.init(this, R.layout.item_past_exam, list);
        // 设置适配器
        getDataBinding().recycleView.setAdapter(adapter);
        getDataBinding().recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) getDataBinding().recycleView.getLayoutManager();
                // 当前第一个完全见的position
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                LogUtil.i("recycleView", "第一个完全见的position：" + firstCompletelyVisibleItemPosition);
                if (firstCompletelyVisibleItemPosition == 0) {
                    // 滑到顶部了
                }
                // 当前最后完全可见的position
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                LogUtil.i("recycleView", "最后完全可见的position：" + lastCompletelyVisibleItemPosition);
                if (lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    // 滑到底部了
                    pageNo++;
                    loadData();
                }
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData() {
        if (pageNo == 0) {
            list.clear();
        }
        getDataBinding().swipeView.setRefreshing(false);
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        adapter.setDataList(list);
    }

    /**
     * 显示支付方式选择窗口
     */
    private void showPayTypePickView() {
        getDataBinding().layout.setVisibility(View.VISIBLE);
        getDataBinding().bgLayout.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_from_bottom);
        getDataBinding().layout.startAnimation(animation);
    }

    /**
     * 关闭支付方式选择窗口
     */
    public void onPayTypePickCloseClick(View view) {
        onBgClick(null);
    }

    /**
     * 提交订单
     */
    public void onCommitOrderClick(View view) {
        SYDataTransport.create(payType.get() == PayParams.PAY_TYPE_WECHAT ? SYConstants.SY_WXPAY : SYConstants.SY_ALIPAY)
                .addParam("goodsType", 2)
                .addParam("goodsName", "2019高考数学试卷")
                .addParam("amountStr", "0.01")
                .addParam("goodsId", 1)
                .addProgressing(this,"正在创建订单，请稍后...")
                .execute(new SYDataListener<PayParams>() {

                    @Override
                    public void onSuccess(PayParams data) {
                        data.setPayType(payType.get());
                        PayHelper.getInstance().pay(PastExamActivity.this, data, isSuccess -> {
                            if (isSuccess) {
                                showSingleToast("支付成功！");
                            } else {
                                showSingleToast("支付失败！");
                            }
                            onBgClick(null);
                        });
                    }
                }, PayParams.class);
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
     * 背景点击
     */
    public void onBgClick(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom);
        getDataBinding().layout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getDataBinding().layout.setVisibility(View.GONE);
                getDataBinding().bgLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
