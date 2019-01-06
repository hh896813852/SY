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
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Examination;
import com.edusoho.yunketang.databinding.ActivityMyBoughtVideoBinding;
import com.edusoho.yunketang.edu.api.UserApi;
import com.edusoho.yunketang.edu.bean.DataPageResult;
import com.edusoho.yunketang.edu.bean.StudyCourse;
import com.edusoho.yunketang.edu.http.HttpUtils;
import com.edusoho.yunketang.edu.http.SubscriberProcessor;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.course.CourseDetailsActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.LogUtil;
import com.edusoho.yunketang.utils.StringUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Layout(value = R.layout.activity_my_bought_video, title = "我购买的视频")
public class MyBoughtVideoActivity extends BaseActivity<ActivityMyBoughtVideoBinding> {
    public ObservableField<Boolean> isLogin = new ObservableField<>(false);
    public ObservableField<Boolean> hasData = new ObservableField<>(true);

    private boolean isSYZXDataLoading;
    private boolean isSYKJDataLoading;
    private List<StudyCourse> tempList = new ArrayList<>();
    private List<StudyCourse> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter();
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
        // 跳转课程详情页
        Intent intent = new Intent(this, CourseDetailsActivity.class);
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter.init(this, R.layout.item_video_bought, list);
        tempList.clear();
        loadSYZXData();
        loadSYKJData();
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogin.set(SYApplication.getInstance().getUser() != null);
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
                                for (StudyCourse c : dataList) {
                                    if (c.courseSet.maxCoursePrice2.getPrice() > 0) {
                                        c.courseType = 1;
                                        tempList.add(c);
                                    }
                                }
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
                                for (StudyCourse c : dataList) {
                                    if (c.courseSet.maxCoursePrice2.getPrice() > 0) {
                                        c.courseType = 2;
                                        tempList.add(c);
                                    }
                                }
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
        startActivity(new Intent(this, LoginActivity.class));
    };
}
