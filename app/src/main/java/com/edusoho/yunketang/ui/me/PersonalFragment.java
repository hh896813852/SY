package com.edusoho.yunketang.ui.me;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.databinding.FragmentPersonalBinding;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.utils.statusbar.StatusBarUtil;
import com.edusoho.yunketang.wxapi.WXshare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;

/**
 * @author huhao on 2018/7/4
 */
@Layout(value = R.layout.fragment_personal)
public class PersonalFragment extends BaseFragment<FragmentPersonalBinding> {

    public ObservableField<String> avatar = new ObservableField<>();
    public ObservableField<String> nickname = new ObservableField<>("上小元");
    public ObservableField<String> personSign = new ObservableField<>("态度决定一切，相信相信的力量。");
    public ObservableField<Boolean> isLogin = new ObservableField<>(false);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        User loginUser = SYApplication.getInstance().getUser();
        isLogin.set(loginUser != null);
        if (loginUser != null) {
            avatar.set(loginUser.syjyUser.headImg);
            nickname.set(TextUtils.isEmpty(loginUser.syjyUser.nickName) ? "上小元" : loginUser.syjyUser.nickName);
            personSign.set(TextUtils.isEmpty(loginUser.syjyUser.personSign) ? "态度决定一切，相信相信的力量。" : loginUser.syjyUser.personSign);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getSupportedActivity() != null) {
            StatusBarUtil.setTranslucentStatus(getSupportedActivity());
        }
    }

    /**
     * 登录/注册
     */
    public View.OnClickListener onLoginOrRegisterClicked = v -> {
        startActivity(new Intent(getSupportedActivity(), LoginActivity.class));
    };

    /**
     * 头像点击
     */
    public View.OnClickListener onHeadImageClicked = v -> {
        if (SYApplication.getInstance().isLogin()) {
            startActivity(new Intent(getSupportedActivity(), PersonalInfoActivity.class));
        } else {
            startActivity(new Intent(getSupportedActivity(), LoginActivity.class));
            showSingleToast("请先登录！");
        }
    };

    /**
     * 我的收藏
     */
    public View.OnClickListener onMyCollectionClicked = v -> {
        startActivity(new Intent(getSupportedActivity(), MyCollectionActivity.class));
    };

    /**
     * 我的学习
     */
    public View.OnClickListener onMyStudyClicked = v -> {
        startActivity(new Intent(getSupportedActivity(), MyStudyActivity.class));
    };

    /**
     * 购买的视频
     */
    public View.OnClickListener onBuyVideoClicked = v -> {
        startActivity(new Intent(getSupportedActivity(), MyBoughtVideoActivity.class));
    };

    /**
     * 购买的试卷
     */
    public View.OnClickListener onBuyPaperClicked = v -> {
        startActivity(new Intent(getSupportedActivity(), MyBoughtExamActivity.class));
    };

    /**
     * 上元在线元宝
     */
    public View.OnClickListener onSYZXGoldClicked = v -> {
        Intent intent = new Intent(getSupportedActivity(), MyIngotActivity.class);
        intent.putExtra(MyIngotActivity.INGOT_TYPE, 1);
        startActivity(intent);
    };

    /**
     * 上元会计元宝
     */
    public View.OnClickListener onSYKJGoldClicked = v -> {
        Intent intent = new Intent(getSupportedActivity(), MyIngotActivity.class);
        intent.putExtra(MyIngotActivity.INGOT_TYPE, 2);
        startActivity(intent);
    };

    /**
     * 设置
     */
    public View.OnClickListener onSetClicked = v -> {
        startActivity(new Intent(getSupportedActivity(), SettingActivity.class));
    };
}
