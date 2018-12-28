package com.edusoho.yunketang.ui.classes;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.ClassCourse;
import com.edusoho.yunketang.databinding.FragmentClassScheduleBinding;
import com.edusoho.yunketang.helper.ListViewHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.fragment_class_schedule)
public class ClassScheduleFragment extends BaseFragment<FragmentClassScheduleBinding> {
    private int status; // 0、未上课 1、已上课
    public List<ClassCourse> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter();

    private int pageNo = 1;
    private boolean isLoading;

    public void refresh() {
        if (getActivity() != null && getDataBinding() != null) {
            pageNo = 1;
            ((ClassScheduleActivity) getActivity()).getDataBinding().swipeView.setRefreshing(true);
            getDataBinding().listView.setCanLoadMore(true);
            loadData();
        }
    }

    public static ClassScheduleFragment newInstance(int status) {
        ClassScheduleFragment fragment = new ClassScheduleFragment();
        Bundle args = new Bundle();
        args.putInt("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        status = getArguments().getInt("status");
        adapter.init(getSupportedActivity(), R.layout.item_class_schedule, list);
        loadData();
        // 加载更多
        getDataBinding().listView.setOnLoadMoreListener(() -> {
            if (!isLoading && !((ClassScheduleActivity) getActivity()).getDataBinding().swipeView.isRefreshing()) {
                pageNo++;
                isLoading = true;
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData() {
        SYDataTransport.create(SYConstants.CLASS_COURSE_LIST)
                .addParam("type", status)
                .addParam("classId", ((ClassScheduleActivity) getActivity()).classId)
                .addParam("page", pageNo)
                .addParam("limit", SYConstants.PAGE_SIZE)
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        if (getActivity() == null || getDataBinding() == null) {
                            return;
                        }
                        isLoading = false;
                        if (pageNo == 1) {
                            list.clear();
                        }
                        try {
                            List<ClassCourse> dataList = JsonUtil.fromJson(new JSONObject(data).getString("records"), new TypeToken<List<ClassCourse>>() {
                            });
                            list.addAll(dataList);
                            adapter.notifyDataSetChanged();

                            // 如果在当前页面加载完成，则按当前页面来设置高度
                            if (status == ((ClassScheduleActivity) getActivity()).getDataBinding().vpMain.getCurrentItem()) {
                                // 因为notifyDataSetChanged()方法是异步的，并且没有方法监听其什么结束
                                // 而我们需要界面绘制完成获取item高度，所以延迟300ms，进行假同步操作。
                                new Handler().postDelayed(() -> resetViewPagerHeight(), 300);
                            }
                            // set load more or can't
                            getDataBinding().listView.setCanLoadMore(dataList.size() == SYConstants.PAGE_SIZE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // stop loading
                        ((ClassScheduleActivity) getActivity()).getDataBinding().swipeView.setRefreshing(false);
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        if (getDataBinding() != null) {
                            ((ClassScheduleActivity) getActivity()).getDataBinding().swipeView.setRefreshing(false);
                            getDataBinding().listView.setCanLoadMore(false);
                        }
                    }
                }, String.class);
    }

    /**
     * scrollview嵌套viewpager，导致viewpager内容不可见。
     * 需要给定viewpager高度。
     * 设置courseFragment的viewpager高度，
     */
    public void resetViewPagerHeight() {
        int listViewHeight = ListViewHelper.getTotalHeightOfListView(getDataBinding().listView);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ((ClassScheduleActivity) getActivity()).getDataBinding().vpMain.getLayoutParams();
        linearParams.height = listViewHeight - DensityUtil.dip2px(getSupportedActivity(), 20);
        ((ClassScheduleActivity) getActivity()).getDataBinding().vpMain.setLayoutParams(linearParams);
    }
}
