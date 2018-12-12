package com.edusoho.yunketang.ui.me;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (SYApplication.getInstance().isLogin()) {
//            avatar.set(SYApplication.getInstance().getUser().avatar);
        } else {
            avatar.set("");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getSupportedActivity() != null) {
            StatusBarUtil.setTranslucentStatus(getSupportedActivity());
        }
    }
}
