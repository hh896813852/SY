package com.edusoho.yunketang.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Examination;
import com.edusoho.yunketang.databinding.FragmentQuestionCollectBinding;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.exercise.ExerciseActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.fragment_question_collect)
public class QuestionCollectFragment extends BaseFragment<FragmentQuestionCollectBinding> {
    private int pageNo = 1;
    private boolean isLoading;

    public ObservableField<Boolean> isLogin = new ObservableField<>(false);
    public ObservableField<Boolean> hasData = new ObservableField<>(true);

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (getActivity() != null && getDataBinding() != null) {
            pageNo = 1;
            getDataBinding().swipeView.setRefreshing(true);
            loadData();
        }
    };
    private List<Examination> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter();
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
        Intent intent = new Intent(getSupportedActivity(), ExerciseActivity.class);
        intent.putExtra(ExerciseActivity.IS_MY_COLLECTION, true);
        intent.putExtra(ExerciseActivity.EXAMINATION_ID, list.get(position).examinationId);
        startActivityForResult(intent, ExerciseActivity.FROM_EXERCISE_CODE);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ExerciseActivity.FROM_EXERCISE_CODE) {
            onRefreshListener.onRefresh();
        }
        if (requestCode == LoginActivity.LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            isLogin.set(SYApplication.getInstance().getUser() != null);
            onRefreshListener.onRefresh();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.init(getSupportedActivity(), R.layout.item_question_collect, list);
        // 加载更多
        getDataBinding().listView.setOnLoadMoreListener(() -> {
            if (!isLoading && !getDataBinding().swipeView.isRefreshing()) {
                pageNo++;
                isLoading = true;
            }
        });
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogin.set(SYApplication.getInstance().getUser() != null);
    }

    private void loadData() {
        if (SYApplication.getInstance().getUser() == null) {
            hasData.set(false);
            return;
        }
        SYDataTransport.create(SYConstants.MY_COLLECT_HOMEWORK)
                .addParam("userId", SYApplication.getInstance().getUser().syjyUser.id)
                .addParam("page", pageNo)
                .addParam("limit", SYConstants.PAGE_SIZE)
                .execute(new SYDataListener<List<Examination>>() {

                    @Override
                    public void onSuccess(List<Examination> data) {
                        if (pageNo == 1) {
                            list.clear();
                        }
                        list.addAll(data);
                        adapter.notifyDataSetChanged();
                        hasData.set(list.size() > 0);
                        if (getDataBinding() != null) {
                            getDataBinding().swipeView.setRefreshing(false);
                            getDataBinding().listView.setCanLoadMore(data.size() == SYConstants.PAGE_SIZE);
                        }
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        if (getDataBinding() != null) {
                            getDataBinding().swipeView.setRefreshing(false);
                            getDataBinding().listView.setCanLoadMore(false);
                        }
                    }
                }, new TypeToken<List<Examination>>() {
                });
    }

    /**
     * 去登录
     */
    public View.OnClickListener onLoginClicked = v -> {
        startActivityForResult(new Intent(getSupportedActivity(), LoginActivity.class), LoginActivity.LOGIN_REQUEST_CODE);
    };
}
