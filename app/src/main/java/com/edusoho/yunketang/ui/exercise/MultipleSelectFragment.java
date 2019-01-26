package com.edusoho.yunketang.ui.exercise;

import android.databinding.ObservableField;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.databinding.FragmentMultipleSelectBinding;
import com.edusoho.yunketang.helper.PicLoadHelper;
import com.edusoho.yunketang.helper.PicPreviewHelper;
import com.edusoho.yunketang.utils.DateUtils;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.ScreenUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Layout(value = R.layout.fragment_multiple_select)
public class MultipleSelectFragment extends BaseFragment<FragmentMultipleSelectBinding> {
    private Question question;
    private MediaPlayer mediaPlayer; // 音频播放器
    private boolean isPrepared;      // 音频播放器是否准备好了
    private MultipleSelectFragment.TimeThread timeThread;   // 时间进度线程
    public ObservableField<Boolean> hasAudio = new ObservableField<>(false); // 是否有音频
    public ObservableField<Boolean> isPlaying = new ObservableField<>(false);// 音频播放器是否正在播放
    public ObservableField<String> audioCurrentTime = new ObservableField<>("00:00");// 音频播放了的时间
    public ObservableField<String> audioDuration = new ObservableField<>("00:00");   // 音频时长

    public ObservableField<String> questionTopic = new ObservableField<>();

    // 答案解析部分
    public ObservableField<Boolean> isShowAnswerAnalysis = new ObservableField<>(false);
    public ObservableField<Boolean> isUserAnswerCorrect = new ObservableField<>(false);
    public ObservableField<String> correctAnswer = new ObservableField<>();
    public ObservableField<String> userAnswer = new ObservableField<>();
    public ObservableField<String> answerAnalysis = new ObservableField<>();

    public List<String> picList = new ArrayList<>();
    public List<String> answerAnalysisPicList = new ArrayList<>();

    public List<Question.QuestionDetails.Option> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            // 手动添加选项内容和图片，防止ScrollView下ListView内容高度不确定的情况下被限制
            LinearLayout optionContainer = view.findViewById(R.id.optionContainer);
            optionContainer.removeAllViews();
            if (list.get(position).choiceType == 0) {
                TextView optionContent = new TextView(getSupportedActivity());
                optionContent.setText(list.get(position).optionContent);
                optionContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                optionContent.setGravity(Gravity.CENTER_VERTICAL); // 垂直居中
                optionContent.setTextColor(ContextCompat.getColor(getSupportedActivity(), R.color.text_light_black));
                optionContainer.addView(optionContent);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) optionContent.getLayoutParams();
                params.setMargins(DensityUtil.dip2px(getSupportedActivity(), 10), DensityUtil.dip2px(getSupportedActivity(), 5), DensityUtil.dip2px(getSupportedActivity(), 10), DensityUtil.dip2px(getSupportedActivity(), 5));
                optionContent.setLayoutParams(params);
                view.setOnClickListener(v -> {
                    // 答案解析不可更改选项
                    if (getActivity() != null && ((ExerciseActivity) getActivity()).isAnswerAnalysis) {
                        return;
                    }
                    list.get(position).isPicked = !list.get(position).isPicked;
                    adapter.notifyDataSetChanged();
                });
            } else {
                ImageView optionImage = new ImageView(getSupportedActivity());
                optionImage.setScaleType(ImageView.ScaleType.FIT_XY);
                optionContainer.addView(optionImage);
                PicLoadHelper.load_16_10(getSupportedActivity(), ScreenUtil.getScreenWidth(getSupportedActivity()) - DensityUtil.dip2px(getSupportedActivity(), 110), list.get(position).optionPicUrl, optionImage);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) optionImage.getLayoutParams();
                params.setMargins(DensityUtil.dip2px(getSupportedActivity(), 10), DensityUtil.dip2px(getSupportedActivity(), 5), 0, DensityUtil.dip2px(getSupportedActivity(), 5));
                optionImage.setLayoutParams(params);
                optionImage.setOnClickListener(v -> {
                    PicPreviewHelper.getInstance().setUrl(optionImage, list.get(position).optionPicUrl).preview(getSupportedActivity(), 0);
                });
            }
            // 选项背景
            TextView optionView = view.findViewById(R.id.optionView);
            if (getActivity() != null && ((ExerciseActivity) getActivity()).isAnswerAnalysis) {
                if (list.get(position).isPicked) {
                    optionView.setBackground(ContextCompat.getDrawable(getSupportedActivity(), isUserAnswerCorrect.get() ? R.drawable.bg_theme_color_corner_4 : R.drawable.bg_red_corner_4));
                } else {
                    optionView.setBackground(ContextCompat.getDrawable(getSupportedActivity(), R.drawable.bg_white_stroke_dark_gray_corner_4));
                }
            } else {
                optionView.setBackground(ContextCompat.getDrawable(getSupportedActivity(), list.get(position).isPicked ? R.drawable.bg_orange_corner_4 : R.drawable.bg_white_stroke_dark_gray_corner_4));
            }
            // 选项点击
            FrameLayout optionLayout = view.findViewById(R.id.optionLayout);
            optionLayout.setOnClickListener(v -> {
                // 答案解析不可更改选项
                if (getActivity() != null && ((ExerciseActivity) getActivity()).isAnswerAnalysis) {
                    return;
                }
                list.get(position).isPicked = !list.get(position).isPicked;
                adapter.notifyDataSetChanged();
            });
            return view;
        }
    };

    public static MultipleSelectFragment newInstance(Question question) {
        MultipleSelectFragment fragment = new MultipleSelectFragment();
        Bundle args = new Bundle();
        args.putSerializable("question", question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        question = (Question) getArguments().getSerializable("question");
        initView();
    }

    private void initView() {
        // 题目序号 + 题目
        questionTopic.set(question.questionSort + "、" + (TextUtils.isEmpty(question.topic) ? "" : question.topic));
        // 是否包含音频文件
        hasAudio.set(!TextUtils.isEmpty(question.topicVoiceUrl));
        // 题目图片adapter
        if (!TextUtils.isEmpty(question.topicPictureUrl)) {
            getDataBinding().topicPicContainer.setVisibility(View.VISIBLE);
            picList.addAll(Arrays.asList(question.topicPictureUrl.split(",")));
            for (int i = 0; i < picList.size(); i++) {
                View innerView = LayoutInflater.from(getSupportedActivity()).inflate(R.layout.item_pic, null);
                ImageView imageView = innerView.findViewById(R.id.imageView);
                imageView.setTag(String.valueOf(i));
                PicLoadHelper.load(getSupportedActivity(), ScreenUtil.getScreenWidth(getSupportedActivity()) - DensityUtil.dip2px(getSupportedActivity(), 20), picList.get(i), imageView);
                imageView.setOnClickListener(v -> {
                    int position = Integer.valueOf(imageView.getTag().toString());
                    PicPreviewHelper.getInstance().setUrl(imageView, picList.get(position)).preview(getSupportedActivity(), 0);
                });
                getDataBinding().topicPicContainer.addView(innerView);
            }
        } else {
            getDataBinding().topicPicContainer.setVisibility(View.GONE);
        }
        // 是否显示答案解析
        isShowAnswerAnalysis.set(getActivity() != null && ((ExerciseActivity) getActivity()).isAnswerAnalysis);
        // 如果显示答案解析
        if (isShowAnswerAnalysis.get()) {
            // 正确答案
            correctAnswer.set(question.details.get(0).getCorrectResult());
            // 如果用户作答了
            if (!TextUtils.isEmpty(question.userResult)) {
//                // 解析用户答案
//                List<MyAnswer> userAnswerList = JsonUtil.fromJson(question.userResult, new TypeToken<List<MyAnswer>>() {
//                });
                // 用户答案
                if (!TextUtils.isEmpty(question.userResult)) {
                    userAnswer.set(question.details.get(0).getUserResult(question.userResult));
                } else {
                    userAnswer.set("未作答");
                }
            } else {
                userAnswer.set("未作答");
            }
            // 用户答案是否正确
            isUserAnswerCorrect.set(TextUtils.equals(userAnswer.get(), correctAnswer.get()));
            // 答案解析
            answerAnalysis.set(question.details.get(0).resultResolve);
            // 答案解析图片
            String resultResolveUrl = question.details.get(0).resultResolveUrl;
            if (!TextUtils.isEmpty(resultResolveUrl)) {
                answerAnalysisPicList.addAll(Arrays.asList(resultResolveUrl.split(",")));
                for (String url : answerAnalysisPicList) {
                    View innerView = LayoutInflater.from(getSupportedActivity()).inflate(R.layout.item_pic, null);
                    ImageView imageView = innerView.findViewById(R.id.imageView);
                    PicLoadHelper.load(getSupportedActivity(), ScreenUtil.getScreenWidth(getSupportedActivity()) - DensityUtil.dip2px(getSupportedActivity(), 20), url, imageView);
                    getDataBinding().answerAnalysisPicContainer.addView(innerView);
                }
            }
        }
        // 选项adapter
        list.addAll(question.details.get(0).options);
        adapter.init(getSupportedActivity(), R.layout.item_multiple_option, list);
        // 初始化音频播放器
        if (hasAudio.get() && mediaPlayer == null) {
            initMediaPlayer();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (hasAudio.get()) {
            if (isVisibleToUser) { // 界面可见，则创建播放器
                if (mediaPlayer == null) {
                    initMediaPlayer();
                }
            } else { // 界面不可见，则释放播放器
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    isPrepared = false;
                    mediaPlayer.release();
                    mediaPlayer = null;
                    isPlaying.set(false);
                    timeThread = null;
                    getDataBinding().seekBar.setProgress(0);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (mediaPlayer != null && !mediaPlayer.isPlaying() && isPrepared) {
//            mediaPlayer.start();
//            isPlaying.set(true);
//            timeThread = new TimeThread();
//            timeThread.start();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying() && isPrepared) {
            mediaPlayer.pause();
            isPlaying.set(false);
            timeThread = null;
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying.set(false);
            timeThread = null;
        }
        super.onDestroy();
    }

    /**
     * 初始化MediaPlayer
     */
    private void initMediaPlayer() {
        if (getDataBinding() == null) {
            return;
        }
        // 给进度条设置监听(以次来实现快进功能)
        getDataBinding().seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //首先获取seekbar拖动后的位置
                int progress = getDataBinding().seekBar.getProgress();
                //跳转到某个位置播放
                mediaPlayer.seekTo(progress);
            }
        });
        // 语音Url
        String path = question.topicVoiceUrl;
        mediaPlayer = new MediaPlayer();
        // 设置音频流的类型
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            // 设置播放url
            mediaPlayer.setDataSource(path);
            // 通过异步的方式装载媒体资源
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 准备完毕回调
        mediaPlayer.setOnPreparedListener(mp -> {
            // 装载完毕回调
            isPrepared = true;
            // 给seekBar设置总时长
            getDataBinding().seekBar.setMax(mediaPlayer.getDuration());
            audioDuration.set(DateUtils.second2Min(mediaPlayer.getDuration() / 1000));
        });
        // 缓冲回调
        mediaPlayer.setOnBufferingUpdateListener((mp, percent) -> {
            getDataBinding().seekBar.setSecondaryProgress(mediaPlayer.getDuration() / 100 * percent);
        });
        // 播放完成回调
        mediaPlayer.setOnCompletionListener(mp -> {
            timeThread = null;
            isPlaying.set(false);
            audioCurrentTime.set(DateUtils.second2Min(mediaPlayer.getDuration() / 1000));
        });
    }

    /**
     * 播放/暂停
     */
    public View.OnClickListener onPlayAudioClicked = v -> {
        if (!mediaPlayer.isPlaying()) { // 不是播放状态
            if (isPrepared) {
                mediaPlayer.start();
                isPlaying.set(true);
                timeThread = new MultipleSelectFragment.TimeThread();
                timeThread.start();
            } else {
                showSingleToast("数据暂未缓冲好，请稍等...");
            }
        } else {
            mediaPlayer.pause();
            isPlaying.set(false);
            timeThread = null;
        }
    };

    class TimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (getDataBinding().seekBar.getProgress() <= getDataBinding().seekBar.getMax()) {
                if (mediaPlayer != null && isPlaying.get()) {
                    // 设置进度条的进度
                    // 得到当前音乐的播放位置
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    audioCurrentTime.set(DateUtils.second2Min(currentPosition / 1000));
                    getDataBinding().seekBar.setProgress(currentPosition);
                    // 让进度条每一秒向前移动
                    SystemClock.sleep(1000);
                } else {
                    break;
                }
            }
        }
    }
}
