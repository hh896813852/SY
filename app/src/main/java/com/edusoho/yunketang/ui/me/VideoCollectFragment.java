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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Course;
import com.edusoho.yunketang.databinding.FragmentVideoCollectBinding;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.course.CourseDetailsActivity;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.StringUtils;
import com.edusoho.yunketang.utils.glide.GlideRoundTransform;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.fragment_video_collect)
public class VideoCollectFragment extends BaseFragment<FragmentVideoCollectBinding> {
    private static final int COURSE = 0;
    private static final int LIVE = 1;
    private static final int CLASSROOM = 2;
    private boolean isSyzxLoading;
    private boolean isSykjLoading;

    public ObservableField<Boolean> isLogin = new ObservableField<>(false);
    public ObservableField<Boolean> hasData = new ObservableField<>(true);

    private List<Course> syzxList = new ArrayList<>();
    private List<Course> sykjList = new ArrayList<>();
    private List<Course> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ImageView courseImage = view.findViewById(R.id.courseImage);
            Glide.with(getSupportedActivity()).load(list.get(position).cover.middle).placeholder(R.drawable.bg_load_default_4x3).transform(new GlideRoundTransform(getSupportedActivity(), 4)).into(courseImage);
            TextView courseTitleView = view.findViewById(R.id.courseTitleView);
            courseTitleView.setText(list.get(position).title);
            TextView moneyView = view.findViewById(R.id.moneyView);
            if (COURSE == getItemType(position) || LIVE == getItemType(position)) {
                if (list.get(position).minCoursePrice > 0) {
                    moneyView.setText(list.get(position).minCoursePrice2.getPriceWithUnit());
                } else {
                    moneyView.setText("免费");
                }
            } else {
                if (list.get(position).price > 0) {
                    moneyView.setText(list.get(position).price2.getPriceWithUnit());
                } else {
                    moneyView.setText("免费");
                }
            }
            TextView joinCountView = view.findViewById(R.id.joinCountView);
            joinCountView.setText(list.get(position).studentNum + "人参与");
            return view;
        }

        private int getItemType(int position) {
            switch (list.get(position).type) {
                case "course":
                    return COURSE;
                case "classroom":
                    return CLASSROOM;
                case "live":
                    return LIVE;
            }
            return COURSE;
        }
    };
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
        // 跳转课程详情页
        Intent intent = new Intent(getSupportedActivity(), CourseDetailsActivity.class);
        intent.putExtra(CourseDetailsActivity.COURSE_TYPE, list.get(position).courseType); // 1、上元在线 2、上元会计
        intent.putExtra(CourseDetailsActivity.COURSE_ID, list.get(position).id);
        getSupportedActivity().startActivityForResult(intent, CourseDetailsActivity.FROM_COURSE_CODE);
    };
    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (getActivity() != null && getDataBinding() != null) {
            getDataBinding().swipeView.setRefreshing(true);
            loadSYZXData();
            loadSYKJData();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CourseDetailsActivity.FROM_COURSE_CODE) {
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
        adapter.init(getSupportedActivity(), R.layout.item_video_collect, list);
        loadSYZXData();
        loadSYKJData();
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogin.set(SYApplication.getInstance().getUser() != null);
    }

    private void loadSYZXData() {
        isSyzxLoading = true;
        SYDataTransport.create(SYConstants.ONLINE_VIDEO_COLLECTION)
                .isGET()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        isSyzxLoading = false;
                        // 将数据中富文本内容转成可被解析的json数据
                        String json = StringUtils.jsonStringConvert(StringUtils.replaceBlank(data));
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            List<Course> courses = JsonUtil.fromJson(jsonObject.getString("data"), new TypeToken<List<Course>>() {
                            });
                            syzxList.clear();
                            for (Course c : courses) {
                                c.courseType = 1;
                                syzxList.add(c);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        refreshView();
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        isSyzxLoading = false;
                        refreshView();
                    }
                });
    }

    private void loadSYKJData() {
        isSykjLoading = true;
        SYDataTransport.create(SYConstants.ACCOUNTANT_VIDEO_COLLECTION)
                .isGET()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        isSykjLoading = false;
                        // 将数据中富文本内容转成可被解析的json数据
                        String json = StringUtils.jsonStringConvert(StringUtils.replaceBlank(data));
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            List<Course> courses = JsonUtil.fromJson(jsonObject.getString("data"), new TypeToken<List<Course>>() {
                            });
                            sykjList.clear();
                            for (Course c : courses) {
                                c.courseType = 2;
                                sykjList.add(c);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        refreshView();
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        isSykjLoading = false;
                        refreshView();
                    }
                });
    }

    private void refreshView() {
        if (!isSyzxLoading && !isSykjLoading) {
            list.clear();
            list.addAll(syzxList);
            list.addAll(sykjList);
            hasData.set(list.size() > 0);
            adapter.notifyDataSetChanged();
            if (getDataBinding() != null) {
                getDataBinding().swipeView.setRefreshing(false);
            }
        }
    }

    /**
     * 去登录
     */
    public View.OnClickListener onLoginClicked = v -> {
        startActivityForResult(new Intent(getSupportedActivity(), LoginActivity.class), LoginActivity.LOGIN_REQUEST_CODE);
    };
}
