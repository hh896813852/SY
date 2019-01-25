package com.edusoho.yunketang.ui.me.classroom;

import android.content.Intent;
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
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.databinding.FragmentClassCatalogueBinding;
import com.edusoho.yunketang.edu.bean.CourseProject;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.course.CourseDetailsActivity;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.StringUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huhao on 2018/11/9 0009.
 */
@Layout(value = R.layout.fragment_class_catalogue)
public class ClassCatalogueFragment extends BaseFragment<FragmentClassCatalogueBinding> {
    private int courseType;
    private int classroomId;

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (getDataBinding() != null) {
            getDataBinding().swipeView.setRefreshing(true);
            loadCatalogueData(courseType, classroomId);
        }
    };

    private List<CourseProject> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter();
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
        // 跳转课程详情页
        Intent intent = new Intent(getSupportedActivity(), CourseDetailsActivity.class);
        intent.putExtra(CourseDetailsActivity.COURSE_TYPE, courseType); // 1、上元在线 2、上元会计
        intent.putExtra(CourseDetailsActivity.COURSE_ID, list.get(position).courseSet.id);
        getSupportedActivity().startActivity(intent);
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.init(getSupportedActivity(), R.layout.item_class_course, list);
    }

    /**
     * 加载课程目录
     */
    public void loadCatalogueData(int courseType, int classroomId) {
        if (getActivity() == null) {
            return;
        }
        this.courseType = courseType;
        this.classroomId = classroomId;
        SYDataTransport.create(String.format(courseType == 1 ? SYConstants.ONLINE_CLASS_CATEGORY : SYConstants.ACCOUNTANT_CLASS_CATEGORY, classroomId))
                .isGET()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        getDataBinding().swipeView.setRefreshing(false);
                        list.clear();
                        try {
                            // 将数据中富文本内容转成可被解析的json数据
                            String json = StringUtils.jsonStringConvert(StringUtils.replaceBlank(data));
                            list.addAll(JsonUtil.fromJson(json, new TypeToken<List<CourseProject>>() {
                            }));
                        } catch (Exception ignored) {
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
