package com.edusoho.yunketang.ui.exercise;

import android.databinding.ObservableField;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.ChildQuestionViewPagerAdapter;
import com.edusoho.yunketang.adapter.QuestionViewPagerAdapter;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.bean.event.ChildPositionEvent;
import com.edusoho.yunketang.databinding.FragmentReadSelectBinding;
import com.edusoho.yunketang.utils.DateUtils;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.LogUtil;
import com.edusoho.yunketang.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Layout(value = R.layout.fragment_read_select)
public class ReadSelectedFragment extends BaseFragment<FragmentReadSelectBinding> {
    public Question question;
    private MediaPlayer mediaPlayer; // 音频播放器
    private boolean isPrepared;      // 音频播放器是否准备好了
    private TimeThread timeThread;   // 时间进度线程
    public ObservableField<Boolean> hasAudio = new ObservableField<>(false); // 是否有音频
    public ObservableField<Boolean> isPlaying = new ObservableField<>(false);// 音频播放器是否正在播放
    public ObservableField<String> audioCurrentTime = new ObservableField<>("00:00");// 音频播放了的时间
    public ObservableField<String> audioDuration = new ObservableField<>();         // 音频时长

    public ObservableField<String> questionTopic = new ObservableField<>(); // 题目

    public List<String> picList = new ArrayList<>();// 题目图片集合
    private List<Question.QuestionDetails> childQuestionList = new ArrayList<>(); // 题目子题集合
    private ChildQuestionViewPagerAdapter viewPagerAdapter;            // 试卷子题adapter
    private boolean isVisibleToUser; // fragment界面是否可见

    public static ReadSelectedFragment newInstance(Question question) {
        ReadSelectedFragment fragment = new ReadSelectedFragment();
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
        initData();
    }

    private void initView() {
        questionTopic.set(question.questionSort + "、" + question.topic);
        hasAudio.set(!TextUtils.isEmpty(question.topicVoiceUrl));
        // 初始化题目图片
        if (!TextUtils.isEmpty(question.topicPictureUrl)) {
            picList.addAll(Arrays.asList(question.topicPictureUrl.split(",")));
        }
        for (String url : picList) {
            View innerView = LayoutInflater.from(getSupportedActivity()).inflate(R.layout.item_pic, null);
            ImageView imageView = innerView.findViewById(R.id.imageView);
            Glide.with(getSupportedActivity()).load(url).placeholder(R.drawable.bg_load_default_4x3).into(imageView);
            getDataBinding().containerLayout.addView(innerView);
        }
        // 初始化viewPager
        viewPagerAdapter = new ChildQuestionViewPagerAdapter(getChildFragmentManager(), childQuestionList);
        getDataBinding().viewPager.setOffscreenPageLimit(3);
        getDataBinding().viewPager.setAdapter(viewPagerAdapter);
        getDataBinding().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (getActivity() != null) {
                    // 告诉Activity当前子题下标，用于Activity展示相应的答案解析
                    ((ExerciseActivity) getActivity()).currentChildQuestionIndex = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 初始化音频播放器
        if (hasAudio.get() && mediaPlayer == null) {
            initMediaPlayer();
        }
        // 初始化containerLayout、dragView、viewPager高度
        initLayoutHeight();
    }

    private void initData() {
        for (int i = 0; i < question.details.size(); i++) {
            question.details.get(i).childQuestionType = 1; // 单选题
            question.details.get(i).childQuestionSort = i + 1;
        }
        childQuestionList.addAll(question.details);
        viewPagerAdapter.notifyDataSetChanged();
    }

    /**
     * 显示下一页
     */
    public void showNextPage() {
        if (getDataBinding().viewPager.getCurrentItem() + 1 < viewPagerAdapter.getCount()) {
            getDataBinding().viewPager.setCurrentItem(getDataBinding().viewPager.getCurrentItem() + 1, true);
        } else {
            // 如果是子题最后一题，则显示下一题
            if (getActivity() != null) {
                // 第一次选择，显示下一页
                ((ExerciseActivity) getActivity()).showNextPage();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChildPositionChangeEvent(ChildPositionEvent event) {
        if (isVisibleToUser && getActivity() != null) {
            getDataBinding().viewPager.setCurrentItem(event.getChildPosition());
            ((ExerciseActivity) getActivity()).currentChildIndexFromAnswerCard = 0;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && getActivity() != null) {
            // 告诉Activity当前子题下标，用于Activity展示相应的答案解析
            ((ExerciseActivity) getActivity()).currentChildQuestionIndex = getDataBinding().viewPager.getCurrentItem();
            // 初始化containerLayout、dragView、viewPager高度
            initLayoutHeight();
        }
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
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && isPrepared) {
            mediaPlayer.start();
            isPlaying.set(true);
            timeThread = new TimeThread();
            timeThread.start();
        }
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
                timeThread = new TimeThread();
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

    /**
     * 初始化containerLayout、dragView、viewPager高度
     */
    private void initLayoutHeight() {
        getDataBinding().containerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getDataBinding().containerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int initHeight = Math.min(getDataBinding().containerLayout.getHeight(), DensityUtil.dip2px(getSupportedActivity(), 240));
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getDataBinding().scrollView.getLayoutParams();
                params.height = initHeight;
                getDataBinding().scrollView.setLayoutParams(params);
                ViewUtils.setMargins(getDataBinding().dragView, 0, initHeight, 0, 0);
                ViewUtils.setMargins(getDataBinding().viewPager, 0, initHeight + DensityUtil.dip2px(getSupportedActivity(), 40), 0, 0);
            }
        });
    }
}
