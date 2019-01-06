package com.edusoho.yunketang.ui.course;

import android.databinding.ObservableField;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.databinding.ActivityCourseEvaluateBinding;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.AppUtil;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.NotchUtil;

import org.json.JSONException;
import org.json.JSONObject;

@Layout(value = R.layout.activity_course_evaluate)
public class CourseEvaluateActivity extends BaseActivity<ActivityCourseEvaluateBinding> {
    public static final String COURSE_TYPE = "course_type";
    public static final String COURSE_PROJECT_ID = "course_project_id";
    private int courseType; // 1、上元在线  2、上元会计
    private int courseProjectId;

    public ObservableField<Integer> rating = new ObservableField<>(0);
    public ObservableField<String> evaluate = new ObservableField<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseType = getIntent().getIntExtra(COURSE_TYPE, 0);
        courseProjectId = getIntent().getIntExtra(COURSE_PROJECT_ID, 0);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getDataBinding().evaluateTitleLayout.getLayoutParams();
        params.setMargins(0, NotchUtil.getNotchHeight(this), 0, 0);
        getDataBinding().evaluateTitleLayout.setLayoutParams(params);
    }

    /**
     * 星星点击
     */
    public void onStarClick(View view) {
        rating.set(Integer.valueOf(view.getTag().toString()));
    }

    /**
     * 发布
     */
    public void onReleaseClick(View view) {
        if (rating.get() == 0) {
            showSingleToast("请为课程评分！");
            return;
        }
        if (TextUtils.isEmpty(evaluate.get())) {
            showSingleToast("请填写您对课程的建议");
            return;
        }
        SYDataTransport.create(String.format(courseType == 1 ? SYConstants.ONLINE_EVALUATE : SYConstants.ACCOUNTANT_EVALUATE, courseProjectId))
                .isJsonPost(false)
                .directReturn()
                .addHead("User-Agent", String.format("%s%s%s", Build.MODEL, " Android-kuozhi ", Build.VERSION.SDK_INT))
                .addParam("rating", String.valueOf(rating.get()))
                .addParam("content", evaluate.get())
                .execute(new SYDataListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            if (jsonObject.has("id")) {
                                showToast("评价成功！");
                                finish();
                            } else {
                                showToast("评价失败！");
                            }
                            AppUtil.closeSoftInputKeyBoard(CourseEvaluateActivity.this);
                        } catch (JSONException e1) {

                        }
                    }
                });
    }
}
