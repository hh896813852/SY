package com.edusoho.yunketang.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.edusoho.yunketang.R;

public class SYVideoPlayer extends StandardGSYVideoPlayer {
    private ImageView syPlay;

    public SYVideoPlayer(Context context) {
        super(context);
    }

    public SYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        syPlay = findViewById(R.id.sy_play);
        syPlay.setOnClickListener(v -> clickStartIcon());
    }

    @Override
    public int getLayoutId() {
        return R.layout.sy_video_layout;
    }

    @Override
    public void resetProgressAndTime() {
        super.resetProgressAndTime();
    }

    @Override
    protected void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        syPlay.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_pause));
    }

    @Override
    protected void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        syPlay.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_play));
    }
}