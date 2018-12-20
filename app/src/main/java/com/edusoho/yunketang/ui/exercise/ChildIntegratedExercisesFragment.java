package com.edusoho.yunketang.ui.exercise;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.databinding.FragmentChildIntegratedExercisesBinding;
import com.edusoho.yunketang.helper.ImageUploadHelper;
import com.edusoho.yunketang.utils.ProgressDialogUtil;
import com.edusoho.yunketang.utils.RequestCodeUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Layout(value = R.layout.fragment_child_integrated_exercises)
public class ChildIntegratedExercisesFragment extends BaseFragment<FragmentChildIntegratedExercisesBinding> {
    private static final int REQUEST_IMAGE = RequestCodeUtil.next();
    private Question.QuestionDetails childQuestion;

    public ObservableField<String> questionTopic = new ObservableField<>();
    public ObservableField<String> answerContent = new ObservableField<>();

    public List<String> picList = new ArrayList<>();
    public SYBaseAdapter picAdapter = new SYBaseAdapter() {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ImageView imageView = view.findViewById(R.id.imageView);
            Glide.with(getSupportedActivity()).load(picList.get(position)).placeholder(R.drawable.bg_load_default_4x3).into(imageView);
            return view;
        }
    };

    public List<String> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ImageView answerImage = view.findViewById(R.id.answerImage);
            ImageView deleteView = view.findViewById(R.id.deleteView);
            FrameLayout pickView = view.findViewById(R.id.pickView);
            if (position == list.size() - 1) { // 添加照片
                pickView.setVisibility(View.VISIBLE);
                pickView.setOnClickListener(v -> onPicPickClick());
                deleteView.setVisibility(View.GONE);
            } else { // 已添加并显示的照片
                Glide.with(getSupportedActivity()).load(list.get(position)).placeholder(R.drawable.bg_load_default_3x4).into(answerImage);
                pickView.setVisibility(View.GONE);
                deleteView.setVisibility(View.VISIBLE);
                deleteView.setOnClickListener(v -> {
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                    // 子题答案图片修改
                    childQuestion.myAnswerPicUrl = getQuestionPicUrl();
                });
            }
            return view;
        }
    };

    public static ChildIntegratedExercisesFragment newInstance(Question.QuestionDetails childQuestion) {
        ChildIntegratedExercisesFragment fragment = new ChildIntegratedExercisesFragment();
        Bundle args = new Bundle();
        args.putSerializable("childQuestion", childQuestion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) { // 照片选择返回
                List<Uri> uriList = Matisse.obtainResult(data);
                ProgressDialogUtil.showProgress(getSupportedActivity(),"正在上传图片...");
                ImageUploadHelper.uploadImage(getSupportedActivity(), uriList.get(0), uriList, (urls, urlList) -> {
                    ProgressDialogUtil.hideProgress();
                    list.remove(list.size() - 1);
                    list.addAll(urlList);
                    list.add("");
                    adapter.notifyDataSetChanged();
                    // 子题答案图片保存
                    childQuestion.myAnswerPicUrl = getQuestionPicUrl();
                });
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        childQuestion = (Question.QuestionDetails) getArguments().getSerializable("childQuestion");
        initView();
    }

    private void initView() {
        if (!TextUtils.isEmpty(childQuestion.topicSubsidiary)) {
            questionTopic.set(childQuestion.childQuestionSort + "、" + childQuestion.topicSubsidiary);
        }

        if (!TextUtils.isEmpty(childQuestion.topicSubsidiaryUrl)) {
            picList.addAll(Arrays.asList(childQuestion.topicSubsidiaryUrl.split(",")));
        }
        picAdapter.init(getSupportedActivity(), R.layout.item_pic, picList);
        // 用户作答内容（还原）
        answerContent.set(childQuestion.myAnswerContent);
        // 用户作答图片初始化（还原）
        if(!TextUtils.isEmpty(childQuestion.myAnswerPicUrl)) {
            list.addAll(Arrays.asList(childQuestion.myAnswerPicUrl.split(",")));
        }
        list.add("");
        adapter.init(getSupportedActivity(), R.layout.item_pic_pick, list);

        // 输入答案内容监听，实时更新
        getDataBinding().answerContentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    childQuestion.myAnswerContent = "";
                } else {
                    childQuestion.myAnswerContent = s.toString();
                }
            }
        });
    }

    /**
     * 图片选择
     */
    private void onPicPickClick() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG)) // 显示图片的类型
                .countable(false) // 是否有序选择图片
                .maxSelectable(99)// 最大选择数量
                .capture(true)    // 是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.edusoho.yunketang.fileprovider"))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size)) // 图片显示表格的大小
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // 图像选择和预览活动所需的方向。
                .thumbnailScale(0.85f)          // 缩放比例
                .theme(R.style.Matisse_Zhihu)   // 主题  暗色主题 R.style.Matisse_Dracula
                .imageEngine(new GlideEngine()) // 加载方式
                .forResult(REQUEST_IMAGE);      // 请求码
    }

    /**
     * 获取题目答案图片urls
     */
    private String getQuestionPicUrl() {
        StringBuilder builder = new StringBuilder();
        for (String url : list) {
            if(!TextUtils.isEmpty(url)) {
                builder.append(url).append(",");
            }
        }
        if(TextUtils.isEmpty(builder)) {
            return "";
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }
}
