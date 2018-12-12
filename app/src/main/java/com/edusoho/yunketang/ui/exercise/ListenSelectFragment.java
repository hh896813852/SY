package com.edusoho.yunketang.ui.exercise;

import android.databinding.ObservableField;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.databinding.FragmentListenSelectBinding;
import com.edusoho.yunketang.utils.DateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Layout(value = R.layout.fragment_listen_select)
public class ListenSelectFragment extends BaseFragment<FragmentListenSelectBinding> {
    private Question question;
    private MediaPlayer mediaPlayer; // 音频播放器
    private boolean isPrepared;      // 音频播放器是否准备好了
    private TimeThread timeThread; // 时间进度线程
    public ObservableField<Boolean> isPlaying = new ObservableField<>(false);// 音频播放器是否正在播放
    public ObservableField<String> audioCurrentTime = new ObservableField<>("00:00");// 音频播放了的时间
    public ObservableField<String> audioDuration = new ObservableField<>();         // 音频时长

    public ObservableField<String> questionTopic = new ObservableField<>(); // 题目

    public List<Question.QuestionDetails.Option> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter();
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
        // 是否是第一次选择
        boolean isFirstPick = true;
        for (int i = 0; i < list.size(); i++) {
            // 有选项选过，则不是第一次选择
            if (list.get(i).isPicked) {
                isFirstPick = false;
            }
            list.get(i).isPicked = position == i;
        }
        adapter.notifyDataSetChanged();
        if (isFirstPick && getActivity() != null) {
            // 第一次选择，显示下一页
            ((ExerciseActivity) getActivity()).showNextPage();
        }
    };

    public static ListenSelectFragment newInstance(Question question) {
        ListenSelectFragment fragment = new ListenSelectFragment();
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
        questionTopic.set(question.questionSort + "、" + question.topic);

        list.addAll(question.details.get(0).options);
        adapter.init(getSupportedActivity(), R.layout.item_single_option, list);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
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

        String path = "http://other.web.ri01.sycdn.kuwo.cn/resource/n2/96/19/2142954236.mp3 ";
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
}
