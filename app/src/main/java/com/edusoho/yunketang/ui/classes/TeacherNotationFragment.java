package com.edusoho.yunketang.ui.classes;

import android.databinding.ObservableField;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.databinding.FragmentTeacherNotationBinding;
import com.edusoho.yunketang.helper.PicLoadHelper;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Layout(value = R.layout.fragment_teacher_notation)
public class TeacherNotationFragment extends BaseFragment<FragmentTeacherNotationBinding> {
    private Question question;
    public List<String> teacherNotePicList = new ArrayList<>();

    public ObservableField<String> teacherNotes = new ObservableField<>();
    public ObservableField<String> score = new ObservableField<>();
    private Animation zoomOutAnim;
    private Animation zoomInAnim;
    private boolean isAnimDoing;

    public static TeacherNotationFragment newInstance(Question question) {
        TeacherNotationFragment fragment = new TeacherNotationFragment();
        Bundle args = new Bundle();
        args.putSerializable("question", question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化View
        initView();
        // 初始化动画
        initAnim();
    }

    /**
     * 初始化View
     */
    private void initView() {
        question = (Question) getArguments().getSerializable("question");
        // 老师给的分数
        score.set(question.score);
        // 老师批注
        teacherNotes.set(question.details.get(0).postil);
        // 老师批注图片
        String postilUrl = question.details.get(0).postilUrl;
        if (!TextUtils.isEmpty(postilUrl)) {
            teacherNotePicList.addAll(Arrays.asList(postilUrl.split(",")));
            for (String url : teacherNotePicList) {
                View innerView = LayoutInflater.from(getSupportedActivity()).inflate(R.layout.item_pic, null);
                ImageView imageView = innerView.findViewById(R.id.imageView);
                PicLoadHelper.load(getSupportedActivity(), ScreenUtil.getScreenWidth(getSupportedActivity()) - DensityUtil.dip2px(getSupportedActivity(), 20), url, imageView);
                getDataBinding().teacherNotePicContainer.addView(innerView);
            }
        }
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        zoomOutAnim = AnimationUtils.loadAnimation(getSupportedActivity(), R.anim.zoom_out_from_right);
        zoomOutAnim.setFillAfter(false);
        zoomOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimDoing = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimDoing = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        zoomInAnim = AnimationUtils.loadAnimation(getSupportedActivity(), R.anim.zoom_in_form_right);
        zoomInAnim.setFillAfter(false);
        zoomInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimDoing = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimDoing = false;
                getDataBinding().scoreLayout.setVisibility(View.GONE);
                getDataBinding().scoreView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 显示得分
     */
    public View.OnClickListener onShowScoreClicked = v -> {
        if (!isAnimDoing) {
            getDataBinding().scoreLayout.setVisibility(View.VISIBLE);
            getDataBinding().scoreView.setVisibility(View.GONE);
            getDataBinding().scoreLayout.startAnimation(zoomOutAnim);
        }
    };

    /**
     * 隐藏得分
     */
    public View.OnClickListener onHideScoreClicked = v -> {
        if (!isAnimDoing) {
            getDataBinding().scoreLayout.startAnimation(zoomInAnim);
        }
    };
}
