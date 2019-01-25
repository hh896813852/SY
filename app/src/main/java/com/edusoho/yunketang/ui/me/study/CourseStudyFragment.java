package com.edusoho.yunketang.ui.me.study;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.databinding.FragmentMyStudyCourseBinding;
import com.edusoho.yunketang.edu.bean.StudyCourse;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.course.CourseDetailsActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.StringUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.fragment_my_study_course)
public class CourseStudyFragment extends BaseFragment<FragmentMyStudyCourseBinding> {
    public ObservableField<Boolean> hasData = new ObservableField<>(true);

    private boolean isSYZXDataLoading;
    private boolean isSYKJDataLoading;
    private List<StudyCourse> tempList = new ArrayList<>();
    private List<StudyCourse> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter();
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
        // 跳转课程详情页
        Intent intent = new Intent(getSupportedActivity(), CourseDetailsActivity.class);
        intent.putExtra(CourseDetailsActivity.COURSE_TYPE, list.get(position).courseType); // 1、上元在线 2、上元会计
        intent.putExtra(CourseDetailsActivity.COURSE_ID, list.get(position).courseSet.id);
        startActivityForResult(intent, CourseDetailsActivity.FROM_COURSE_CODE);
    };
    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (getDataBinding() != null) {
            getDataBinding().swipeView.setRefreshing(true);
            tempList.clear();
            loadSYZXData();
            loadSYKJData();
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.init(getSupportedActivity(), R.layout.item_video_bought, list);
        tempList.clear();
        loadSYZXData();
        loadSYKJData();
    }

    private void loadSYZXData() {
        isSYZXDataLoading = true;
        SYDataTransport.create(SYConstants.ONLINE_MY_VIDEO)
                .isGET()
                .directReturn()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        getDataBinding().swipeView.setRefreshing(false);
                        isSYZXDataLoading = false;
                        try {
                            // 将数据中富文本内容转成可被解析的json数据
                            String json = StringUtils.jsonStringConvert(StringUtils.replaceBlank(data));
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.has("paging")) {
                                List<StudyCourse> dataList = JsonUtil.fromJson(jsonObject.getString("data"), new TypeToken<List<StudyCourse>>() {
                                });
                                for (StudyCourse c :dataList) {
                                    c.courseType = 1;
                                }
                                tempList.addAll(dataList);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        refreshListView();
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        getDataBinding().swipeView.setRefreshing(false);
                        isSYZXDataLoading = false;
                        refreshListView();
                    }
                });
    }

    private void loadSYKJData() {
        isSYKJDataLoading = true;
        SYDataTransport.create(SYConstants.ACCOUNTANT_MY_VIDEO)
                .isGET()
                .directReturn()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        getDataBinding().swipeView.setRefreshing(false);
                        isSYKJDataLoading = false;
                        try {
                            // 将数据中富文本内容转成可被解析的json数据
                            String json = StringUtils.jsonStringConvert(StringUtils.replaceBlank(data));
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.has("paging")) {
                                List<StudyCourse> dataList = JsonUtil.fromJson(jsonObject.getString("data"), new TypeToken<List<StudyCourse>>() {
                                });
                                for (StudyCourse c :dataList) {
                                    c.courseType = 2;
                                }
                                tempList.addAll(dataList);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        refreshListView();
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        getDataBinding().swipeView.setRefreshing(false);
                        isSYKJDataLoading = false;
                        refreshListView();
                    }
                });
    }

    /**
     * 刷新列表数据
     */
    private void refreshListView() {
        if (!isSYZXDataLoading && !isSYKJDataLoading) {
            list.clear();
            list.addAll(tempList);
            tempList.clear();
            adapter.notifyDataSetChanged();
            hasData.set(list.size() > 0);
        }
    }

    /**
     * 去登录
     */
    public View.OnClickListener onLoginClicked = v -> {
        startActivity(new Intent(getSupportedActivity(), LoginActivity.class));
    };
}
