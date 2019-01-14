package com.edusoho.yunketang.ui.exercise;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.edusoho.yunketang.helper.PicLoadHelper;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.LogUtil;
import com.edusoho.yunketang.utils.LuBanUtil;
import com.edusoho.yunketang.utils.ProgressDialogUtil;
import com.edusoho.yunketang.utils.RequestCodeUtil;
import com.edusoho.yunketang.utils.ViewUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Layout(value = R.layout.fragment_child_integrated_exercises)
public class ChildIntegratedExercisesFragment extends BaseFragment<FragmentChildIntegratedExercisesBinding> {
    private static final int REQUEST_IMAGE = RequestCodeUtil.next();
    private Question.QuestionDetails childQuestion;

    // 答案解析部分
    public ObservableField<Boolean> isShowAnswerAnalysis = new ObservableField<>(false);
    public ObservableField<String> correctAnswer = new ObservableField<>();
    public ObservableField<String> teacherNotes = new ObservableField<>();
    public ObservableField<String> userAnswer = new ObservableField<>();
    public ObservableField<String> answerAnalysis = new ObservableField<>();
    public List<String> correctAnswerPicList = new ArrayList<>();
    public List<String> teacherNotePicList = new ArrayList<>();
    public List<String> answerAnalysisPicList = new ArrayList<>();

    public ObservableField<String> questionTopic = new ObservableField<>();
    public ObservableField<String> answerContent = new ObservableField<>();

    public List<String> picList = new ArrayList<>();

    public List<String> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ImageView answerImage = view.findViewById(R.id.answerImage);
            ImageView deleteView = view.findViewById(R.id.deleteView);
            FrameLayout pickView = view.findViewById(R.id.pickView);
            // 答案解析不可更改作答，隐藏删除照片按钮
            if ((getParentFragment() != null && ((IntegratedExercisesFragment) getParentFragment()).isTeacherNotation) || (getActivity() != null && ((ExerciseActivity) getActivity()).isAnswerAnalysis)) {
                deleteView.setVisibility(View.GONE);
                pickView.setVisibility(View.GONE);
                Glide.with(getSupportedActivity()).load(list.get(position)).placeholder(R.drawable.bg_load_default_3x4).into(answerImage);
            } else {
                if (position == list.size() - 1) { // 添加照片
                    pickView.setVisibility(View.VISIBLE);
                    pickView.setOnClickListener(v -> permissionCheck());
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
            }
            return view;
        }
    };

    /**
     * 权限检测申请
     */
    private void permissionCheck() {
        RxPermissions rxPermissions = new RxPermissions(getSupportedActivity());
        rxPermissions.requestEach(Manifest.permission.CAMERA)
                .subscribe(permission -> {
                    if (permission.granted) {
                        // 权限允许
                        onPicPickClick();
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 权限拒绝，等待下次询问
                        LogUtil.i("RxPermissions", "权限拒绝，等待下次询问：" + permission.name);
                    } else {
                        // 拒绝权限，不再弹出询问框，请前往APP应用设置打开此权限
                        showSingleToast("相机权限被拒绝，请前往APP应用设置打开此权限。");
                    }
                });
    }

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
                List<String> pathList = Matisse.obtainPathResult(data);
                ProgressDialogUtil.showProgress(getSupportedActivity(), "正在上传图片...");
                // 先压缩，后上传
                LuBanUtil.luBanCompress(getSupportedActivity(), pathList, compressFiles -> ImageUploadHelper.uploadImageAnsy(compressFiles, (urls, urlList) -> {
                    if (urlList.size() != pathList.size()) {
                        if (urlList.size() > 0) {
                            showSingleToast("有图片上传失败，请检查上传失败的图片。");
                        } else {
                            showSingleToast("图片上传失败！");
                        }
                    }
                    ProgressDialogUtil.hideProgress();
                    list.remove(list.size() - 1);
                    list.addAll(urlList);
                    list.add("");
                    adapter.notifyDataSetChanged();
                    // 子题答案图片保存
                    childQuestion.myAnswerPicUrl = getQuestionPicUrl();
                }));
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
            for (String url : picList) {
                View innerView = LayoutInflater.from(getSupportedActivity()).inflate(R.layout.item_pic, null);
                ImageView imageView = innerView.findViewById(R.id.imageView);
                PicLoadHelper.load(getSupportedActivity(), url, imageView);
                getDataBinding().topicPicContainer.addView(innerView);
            }
        }

        // 是否显示答案解析
        isShowAnswerAnalysis.set((getParentFragment() != null && ((IntegratedExercisesFragment) getParentFragment()).isTeacherNotation) || (getActivity() != null && ((ExerciseActivity) getActivity()).isAnswerAnalysis));
        // 如果显示答案解析
        if (isShowAnswerAnalysis.get()) {
            // 正确答案
            correctAnswer.set(childQuestion.correctResult);
            // 正确答案图片
            String correctResultUrl = childQuestion.correctResultUrl;
            if (!TextUtils.isEmpty(correctResultUrl)) {
                correctAnswerPicList.addAll(Arrays.asList(correctResultUrl.split(",")));
                for (String url : correctAnswerPicList) {
                    View innerView = LayoutInflater.from(getSupportedActivity()).inflate(R.layout.item_pic, null);
                    ImageView imageView = innerView.findViewById(R.id.imageView);
                    PicLoadHelper.load(getSupportedActivity(), url, imageView);
                    getDataBinding().correctAnswerPicContainer.addView(innerView);
                }
            }

            if(!(getParentFragment() != null && ((IntegratedExercisesFragment) getParentFragment()).isTeacherNotation)) {
                // 老师批注
                teacherNotes.set(childQuestion.postil);
                // 老师批注图片
                String postilUrl = childQuestion.postilUrl;
                if (!TextUtils.isEmpty(postilUrl)) {
                    teacherNotePicList.addAll(Arrays.asList(postilUrl.split(",")));
                    for (String url : teacherNotePicList) {
                        View innerView = LayoutInflater.from(getSupportedActivity()).inflate(R.layout.item_pic, null);
                        ImageView imageView = innerView.findViewById(R.id.imageView);
                        PicLoadHelper.load(getSupportedActivity(), url, imageView);
                        getDataBinding().teacherNotePicContainer.addView(innerView);
                    }
                }
            }

            // 答案解析
            answerAnalysis.set(childQuestion.resultResolve);
            // 答案解析图片
            String resultResolveUrl = childQuestion.resultResolveUrl;
            if (!TextUtils.isEmpty(resultResolveUrl)) {
                answerAnalysisPicList.addAll(Arrays.asList(resultResolveUrl.split(",")));
                for (String url : answerAnalysisPicList) {
                    View innerView = LayoutInflater.from(getSupportedActivity()).inflate(R.layout.item_pic, null);
                    ImageView imageView = innerView.findViewById(R.id.imageView);
                    PicLoadHelper.load(getSupportedActivity(), url, imageView);
                    getDataBinding().answerAnalysisPicContainer.addView(innerView);
                }
            }
        }

        // 用户作答内容（还原）
        answerContent.set(childQuestion.myAnswerContent);
        // 用户作答图片初始化（还原）
        if (!TextUtils.isEmpty(childQuestion.myAnswerPicUrl)) {
            list.addAll(Arrays.asList(childQuestion.myAnswerPicUrl.split(",")));
        }
        // 答案解析禁止输入内容、不显示添加照片item
        if ((getParentFragment() != null && ((IntegratedExercisesFragment) getParentFragment()).isTeacherNotation) || (getActivity() != null && ((ExerciseActivity) getActivity()).isAnswerAnalysis)) {
            // 禁止输入内容
            getDataBinding().answerContentView.setEnabled(false);
            if (TextUtils.isEmpty(answerContent.get())) {
                getDataBinding().answerContentView.setVisibility(View.GONE);
            }
        } else {
            // 显示添加照片item
            list.add("");
        }
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
            if (!TextUtils.isEmpty(url)) {
                builder.append(url).append(",");
            }
        }
        if (TextUtils.isEmpty(builder)) {
            return "";
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

//    private SparseIntArray

//    /**
//     * 显示老师批注
//     */
//    private void showTeacherNotation() {
//        getDataBinding().scrollLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                getDataBinding().scrollLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                int scrollLayoutHeight = getDataBinding().scrollLayout.getHeight(); // scrollLayout高度
//            }
//        });
//        getDataBinding().notationTitleView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                getDataBinding().notationTitleView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                int notationTitleHeight = getDataBinding().notationTitleView.getHeight(); // notationTitle高度
//            }
//        });
//        getDataBinding().notationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                getDataBinding().notationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                int notationHeight = getDataBinding().notationView.getHeight(); // notation高度
//            }
//        });
//    }
}
