package com.edusoho.yunketang.ui.me;

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
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Examination;
import com.edusoho.yunketang.databinding.ActivityMyBoughtVideoBinding;
import com.edusoho.yunketang.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.activity_my_bought_video, title = "我购买的视频")
public class MyBoughtVideoActivity extends BaseActivity<ActivityMyBoughtVideoBinding> {
    private int pageNo = 1;
    private boolean isLoading;

    public ObservableField<Boolean> isLogin = new ObservableField<>(false);
    public ObservableField<Boolean> hasData = new ObservableField<>(true);

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (getDataBinding() != null) {
            pageNo = 1;
            getDataBinding().swipeView.setRefreshing(true);
            loadData();
        }
    };

    private List<Examination> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            return view;
        }
    };
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter.init(this, R.layout.item_question_collect, list);
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

    }

    /**
     * 去登录
     */
    public View.OnClickListener onLoginClicked = v -> {
        startActivity(new Intent(this, LoginActivity.class));
    };
}
