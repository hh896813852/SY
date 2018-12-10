package com.edusoho.yunketang.ui.course;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.EvaluateData;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.databinding.FragmentEvaluateBinding;
import com.edusoho.yunketang.helper.DialogHelper;
import com.edusoho.yunketang.helper.RegisterOtherPlatformHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.common.ValidateActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2018/11/9 0009.
 */
@Layout(value = R.layout.fragment_evaluate)
public class EvaluateFragment extends BaseFragment<FragmentEvaluateBinding> {
    private int pageNo;
    private int courseType;
    private int courseId;

    private boolean isLoading;
    private List<EvaluateData.Evaluate> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ImageView headImage = view.findViewById(R.id.headImage);
            Glide.with(getSupportedActivity()).load(list.get(position).user.avatar.small).placeholder(R.drawable.icon_default_man_avatar).into(headImage);
            return view;
        }
    };
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
    };
    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (getDataBinding() != null) {
            pageNo = 0;
            getDataBinding().swipeView.setRefreshing(true);
            getDataBinding().listView.setCanLoadMore(true);
            loadEvaluateData(courseType, courseId);
        }
    };

    public ObservableField<Boolean> hasData = new ObservableField<>(true);

    private TextView sendView; // dialog发送验证码按钮

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LoginActivity.LOGIN_REQUEST_CODE) {
                pageNo = 0;
                loadEvaluateData(courseType, courseId);
            }
            if (requestCode == ValidateActivity.VALIDATE_CODE) {
                String dragCaptchaToken = data.getStringExtra("dragCaptchaToken");
                RegisterOtherPlatformHelper.setToken(dragCaptchaToken);
                sendView.performClick(); // 发送验证码点击
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.init(getSupportedActivity(), R.layout.item_evaluate, list);
        // 加载更多
        getDataBinding().listView.setOnLoadMoreListener(() -> {
            if (!isLoading && !getDataBinding().swipeView.isRefreshing()) {
                isLoading = true;
                loadEvaluateData(courseType, courseId);
            }
        });
    }

    /**
     * 加载评论数据
     */
    public void loadEvaluateData(int courseType, int courseId) {
        this.courseType = courseType;
        this.courseId = courseId;
        SYDataTransport.create(String.format(courseType == 1 ? SYConstants.ONLINE_COURSE_EVALUATE : SYConstants.ACCOUNTANT_COURSE_EVALUATE, courseId, pageNo, SYConstants.PAGE_SIZE), false)
                .isGET()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        isLoading = false;
                        if (pageNo == 0) {
                            list.clear();
                        }
                        EvaluateData evaluateData = JsonUtil.fromJson(data, EvaluateData.class);
                        pageNo = evaluateData.paging.offset + SYConstants.PAGE_SIZE;
                        if (evaluateData.data != null) {
                            list.addAll(evaluateData.data);
                        }
                        hasData.set(list.size() > 0);
                        adapter.notifyDataSetChanged();
                        if (getDataBinding() != null) {
                            // stop loading
                            getDataBinding().swipeView.setRefreshing(false);
                            // set load more or can't
                            getDataBinding().listView.setCanLoadMore(evaluateData.data != null && evaluateData.data.size() == SYConstants.PAGE_SIZE);
                        }
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        getDataBinding().swipeView.setRefreshing(false);
                        isLoading = false;
                    }
                });
    }

    /**
     * 去评价
     */
    public View.OnClickListener onEvaluateClicked = v -> {
        User loginUser = SYApplication.getInstance().getUser();
        if (loginUser == null) {
            showSingleToast("请先登录");
            // 去登陆
            toLoginActivity();
        } else {
            if (SYApplication.getInstance().isHelpRegister(courseType)) {
                DialogHelper.showHelpRegisterDialog(getSupportedActivity(), courseType, new DialogHelper.OnRegisterOtherPlatformListener() {
                    @Override
                    public void registerSuccess() {
                        // 去评价Activity
                        toEvaluateActivity();
                    }

                    @Override
                    public void onSMSError(TextView sendCodeView) {
                        sendView = sendCodeView;
                        // 去验证图片Activity
                        toValidateActivity();
                    }
                });
            } else {
                // 去评价Activity
                toEvaluateActivity();
            }
        }
    };

    /**
     * 去登录
     */
    private void toLoginActivity() {
        Intent intent = new Intent(getSupportedActivity(), LoginActivity.class);
        startActivityForResult(intent, LoginActivity.LOGIN_REQUEST_CODE);
    }

    /**
     * 去评价Activity
     */
    private void toEvaluateActivity() {
        Intent intent = new Intent(getSupportedActivity(), CourseEvaluateActivity.class);
        intent.putExtra(CourseEvaluateActivity.COURSE_PROJECT_ID, ((CourseDetailsActivity)getActivity()).courseProject.id);
        intent.putExtra(CourseEvaluateActivity.COURSE_TYPE, courseType);
        startActivity(intent);
    }

    /**
     * 去验证图片Activity
     */
    private void toValidateActivity() {
        Intent intent = new Intent(getSupportedActivity(), ValidateActivity.class);
        intent.putExtra(ValidateActivity.COURSE_TYPE, courseType);
        startActivityForResult(intent, ValidateActivity.VALIDATE_CODE);
    }
}
