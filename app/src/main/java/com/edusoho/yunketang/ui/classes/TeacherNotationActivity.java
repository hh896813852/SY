package com.edusoho.yunketang.ui.classes;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.CommonViewPagerAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.MagicIndicatorBuilder;
import com.edusoho.yunketang.base.MagicIndicatorPageListener;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.MyAnswer;
import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.databinding.ActivityTeacherNotationBinding;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.exercise.IntegratedExercisesFragment;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.ProgressDialogUtil;
import com.edusoho.yunketang.utils.RequestCodeUtil;
import com.google.gson.reflect.TypeToken;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.List;

@Layout(value = R.layout.activity_teacher_notation)
public class TeacherNotationActivity extends BaseActivity<ActivityTeacherNotationBinding> {
    public static final int FROM_TEACHER_NOTATION_CODE = RequestCodeUtil.next();
    public static final String MESSAGE_ID = "message_id";
    private String messageId;
    private Question question;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        // 加载老师批注的题目
        loadTeacherNotation();
    }

    private void initView() {
        messageId = getIntent().getStringExtra(MESSAGE_ID);

        MagicIndicatorBuilder.MagicIndicatorConfiguration configuration = new MagicIndicatorBuilder.MagicIndicatorConfiguration(this);
        configuration.labels = new String[]{"老师批注", "我的答题"};
        configuration.labelTextSize = 14;
        configuration.titleNormalColor = R.color.text_black;
        configuration.lineMode = LinePagerIndicator.MODE_WRAP_CONTENT;
        configuration.lineHeight = DensityUtil.dip2px(this, 3f);
        // set MagicIndicator
        CommonNavigator commonNavigator = MagicIndicatorBuilder.buildCommonNavigator2(this, configuration, (index, textViews) -> {
            for (int i = 0; i < textViews.size(); i++) {
                if (index == i) {
                    textViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    textViews.get(i).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                } else {
                    textViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                    textViews.get(i).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }
            getDataBinding().vpMain.setCurrentItem(index, true);
        });
        getDataBinding().tabIndicator.setNavigator(commonNavigator);
    }

    /**
     * 加载老师批注题目
     */
    private void loadTeacherNotation() {
        ProgressDialogUtil.showProgress(this, "正在加载批注信息，请稍后...", false);
        SYDataTransport.create(SYConstants.TEACHER_POSLIL)
                .addParam("id", messageId)
                .execute(new SYDataListener<Question>() {

                    @Override
                    public void onSuccess(Question data) {
                        ProgressDialogUtil.hideProgress();
                        setResult(Activity.RESULT_OK);
                        if (data == null) {
                            showSingleToast("未查询到老师批注的题目！");
                            finish();
                            return;
                        }
                        data.questionSort = 1;
                        data.questionSum = "1";
                        data.alias = data.subclassificationName;
                        data.userResult = data.homeworkMistake.userResult;
                        data.score = data.homeworkMistake.score;
                        for (Question.QuestionDetails details : data.details) {
                            details.postil = data.homeworkMistake.postil;
                            details.postilUrl = data.homeworkMistake.postilUrl;
                        }
                        List<MyAnswer> userResultList = JsonUtil.fromJson(data.userResult, new TypeToken<List<MyAnswer>>() {
                        });
                        // 遍历子题
                        for (int i = 0; i < data.details.size(); i++) {
                            // 存在用户作答
                            if (!TextUtils.isEmpty(userResultList.get(i).result)) {
                                data.details.get(i).myAnswerContent = userResultList.get(i).result;
                            }
                            // 存在用户作答图片
                            if (!TextUtils.isEmpty(userResultList.get(i).userResultUrl)) {
                                data.details.get(i).myAnswerPicUrl = userResultList.get(i).userResultUrl;
                            }
                        }
                        question = data;
                        // 刷新界面
                        refreshView();
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                        ProgressDialogUtil.hideProgress();
                    }
                }, Question.class);
    }

    /**
     * 刷新界面
     */
    private void refreshView() {
        TeacherNotationFragment teacherNotationFragment = TeacherNotationFragment.newInstance(question);
        IntegratedExercisesFragment integratedExercisesFragment = IntegratedExercisesFragment.newInstance(question);
        integratedExercisesFragment.getArguments().putBoolean("isTeacherNotation", true);
        // init ViewPager
        getDataBinding().vpMain.setOffscreenPageLimit(1);
        getDataBinding().vpMain.setAdapter(new CommonViewPagerAdapter(getSupportFragmentManager(), teacherNotationFragment, integratedExercisesFragment));
        getDataBinding().vpMain.addOnPageChangeListener(new MagicIndicatorPageListener(getDataBinding().tabIndicator));
    }
}
