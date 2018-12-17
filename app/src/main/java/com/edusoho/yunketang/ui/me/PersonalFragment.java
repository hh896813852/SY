package com.edusoho.yunketang.ui.me;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.databinding.FragmentPersonalBinding;
import com.edusoho.yunketang.utils.statusbar.StatusBarUtil;

/**
 * @author huhao on 2018/7/4
 */
@Layout(value = R.layout.fragment_personal)
public class PersonalFragment extends BaseFragment<FragmentPersonalBinding> {

    public ObservableField<String> avatar = new ObservableField<>();
    public ObservableField<Boolean> isLogin = new ObservableField<>(false);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        avatar.set(SYApplication.getInstance().getUser().avatar);
        isLogin.set(SYApplication.getInstance().isLogin());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getSupportedActivity() != null) {
            StatusBarUtil.setTranslucentStatus(getSupportedActivity());
        }
    }

    /**
     * 头像点击
     */
    public View.OnClickListener onHeadImageClicked = v -> {
//        startActivity(new Intent(getSupportedActivity(), SettingActivity.class));
    };

    /**
     * 我的收藏
     */
    public View.OnClickListener onMyCollectionClicked = v -> {
//        startActivity(new Intent(getSupportedActivity(), SettingActivity.class));
    };

    /**
     * 购买的视频
     */
    public View.OnClickListener onBuyVideoClicked = v -> {
//        startActivity(new Intent(getSupportedActivity(), SettingActivity.class));
    };

    /**
     * 购买的试卷
     */
    public View.OnClickListener onBuyPaperClicked = v -> {
//        startActivity(new Intent(getSupportedActivity(), SettingActivity.class));
    };

    /**
     * 上元在线元宝
     */
    public View.OnClickListener onSYZXGoldClicked = v -> {
//        startActivity(new Intent(getSupportedActivity(), SettingActivity.class));
    };

    /**
     * 上元会计元宝
     */
    public View.OnClickListener onSYKJGoldClicked = v -> {
//        startActivity(new Intent(getSupportedActivity(), SettingActivity.class));
    };

    /**
     * 设置
     */
    public View.OnClickListener onSetClicked = v -> {
        startActivity(new Intent(getSupportedActivity(), SettingActivity.class));
    };
}
