package com.edusoho.yunketang.ui.testlib;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.BaseRecycleAdapter;
import com.edusoho.yunketang.adapter.SuperViewHolder;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.BaseDialog;
import com.edusoho.yunketang.base.GridItemDecoration;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.EducationCourse;
import com.edusoho.yunketang.bean.Examination;
import com.edusoho.yunketang.bean.PayParams;
import com.edusoho.yunketang.databinding.ActivityPastExamBinding;
import com.edusoho.yunketang.helper.PayHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.exercise.ExerciseActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.DialogUtil;
import com.edusoho.yunketang.utils.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.activity_past_exam, title = "历年真题")
public class PastExamActivity extends BaseActivity<ActivityPastExamBinding> {
    public static final String TITLE_NAME = "title_name";
    public static final String MODULE_ID = "module_id";
    public static final String SELECTED_COURSE = "selected_course";
    private int moduleId;
    private EducationCourse selectedCourse;

    private Examination prePayExam; // 准备支付的试卷
    private int prePayExamIndex;    // 准备支付的试卷的下标
    public ObservableField<String> examinationName = new ObservableField<>();
    public ObservableField<String> examinationPrice = new ObservableField<>();
    public ObservableField<Integer> payType = new ObservableField<>(PayParams.PAY_TYPE_WECHAT);

    private boolean isLoading;
    private boolean canLoadMore;
    private int pageNo = 1;
    private List<Examination> list = new ArrayList<>();
    public BaseRecycleAdapter<Examination> adapter = new BaseRecycleAdapter<Examination>() {

        @Override
        public void onBindViewHolder(@NonNull SuperViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            // 状态按钮
            holder.getView(R.id.statusView).setOnClickListener(v -> {
                if (getLoginUser() == null || TextUtils.isEmpty(getLoginUser().syjyToken)) { // 未登录，去登录
                    BaseDialog dialog = DialogUtil.showAnimationDialog(PastExamActivity.this, R.layout.dialog_not_login);
                    dialog.findViewById(R.id.cancelView).setOnClickListener(v1 -> dialog.dismiss());
                    dialog.findViewById(R.id.loginView).setOnClickListener(v1 -> startActivity(LoginActivity.class));
                } else {
                    if (list.get(position).finishState == 2) { // 已完成，去答题报告
                        Intent intent = new Intent(PastExamActivity.this, AnswerReportActivity.class);
                        intent.putExtra(AnswerReportActivity.HOMEWORK_ID, list.get(position).homeworkId);
                        startActivity(intent);
                    } else { // 开始（此处无继续状态）
                        if (list.get(position).chargeMode == 0 || list.get(position).isPay) { // 免费 或者 已经购买
                            Intent intent = new Intent(PastExamActivity.this, ExerciseActivity.class);
                            intent.putExtra(ExerciseActivity.EXAMINATION_ID, list.get(position).examinationId);
                            intent.putExtra(ExerciseActivity.EXAMINATION_MINUTE, list.get(position).sumMinute);
                            intent.putExtra(ExerciseActivity.SELECTED_COURSE, selectedCourse);
                            intent.putExtra(ExerciseActivity.MODULE_ID, moduleId);
                            intent.putExtra(ExerciseActivity.IS_EXAM_TEST, true);
                            startActivityForResult(intent, ExerciseActivity.FROM_EXERCISE_CODE);
                        } else { // 去购买
                            prePayExamIndex = position;
                            prePayExam = list.get(position);
                            examinationName.set(prePayExam.examinationName);
                            examinationPrice.set("¥" + prePayExam.price);
                            // 显示支付选择框
                            showPayTypePickView();
                        }
                    }
                }
            });
        }
    };

    public BaseRecycleAdapter.OnItemClickListener onItemClick = (view, position) -> {
    };
    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (getDataBinding() != null && !isLoading) {
            pageNo = 1;
            loadData();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ExerciseActivity.FROM_EXERCISE_CODE) {
            onRefreshListener.onRefresh();
        }
    }

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
        setTitleView(getIntent().getStringExtra(TITLE_NAME));
        moduleId = getIntent().getIntExtra(MODULE_ID, 0);
        selectedCourse = (EducationCourse) getIntent().getSerializableExtra(SELECTED_COURSE);
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
                if (canLoadMore && lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    // 滑到底部了
                    pageNo++;
                    isLoading = true;
                    loadData();
                }
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData() {
        SYDataTransport.create(SYConstants.MODULE_EXERCISE)
                .addParam("businessType", selectedCourse.businessId)
                .addParam("levelId", selectedCourse.levelId)
                .addParam("courseId", selectedCourse.courseId)
                .addParam("moduleId", moduleId)
                .addParam("userId", getLoginUser().syjyUser.id)
                .addParam("page", pageNo)
                .addParam("limit", SYConstants.PAGE_SIZE)
                .execute(new SYDataListener<List<Examination>>() {

                    @Override
                    public void onSuccess(List<Examination> data) {
                        isLoading = false;
                        if (pageNo == 1) {
                            list.clear();
                        }
                        list.addAll(data);
                        adapter.setDataList(list);
                        // 防止界面已关闭，请求才回来导致getDataBinding == null
                        if (getDataBinding() != null) {
                            getDataBinding().swipeView.setRefreshing(false);
                        }
                        canLoadMore = data.size() == SYConstants.PAGE_SIZE;
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        // 防止界面已关闭，请求才回来导致getDataBinding == null
                        if (getDataBinding() != null) {
                            getDataBinding().swipeView.setRefreshing(false);
                        }
                        isLoading = false;
                        canLoadMore = false;
                    }
                }, new TypeToken<List<Examination>>() {
                });
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
        if (prePayExam == null) {
            showSingleToast("当前试卷不存在！");
            return;
        }
        SYDataTransport.create(payType.get() == PayParams.PAY_TYPE_WECHAT ? SYConstants.SY_WXPAY : SYConstants.SY_ALIPAY)
                .addParam("goodsType", 2) // 商品分类 1:视频，2:试卷
                .addParam("moduleId", moduleId)
                .addParam("goodsName", prePayExam.examinationName)
                .addParam("amountStr", prePayExam.price)
                .addParam("goodsId", prePayExam.examinationId)
                .addProgressing(this, "正在创建订单，请稍后...")
                .execute(new SYDataListener<PayParams>() {

                    @Override
                    public void onSuccess(PayParams data) {
                        data.setPayType(payType.get());
                        PayHelper.getInstance().pay(PastExamActivity.this, data, isSuccess -> {
                            if (isSuccess) {
                                showSingleToast("支付成功！");
                                prePayExam.isPay = true;
                                adapter.notifyItemChanged(prePayExamIndex);
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
