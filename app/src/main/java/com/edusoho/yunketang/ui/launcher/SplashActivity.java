package com.edusoho.yunketang.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.helper.AppPreferences;
import com.edusoho.yunketang.ui.MainTabActivity;

/**
 * @author huhao on 2018/8/21
 */
@Layout(value = R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHandler().postDelayed(() -> {
            if (AppPreferences.isFirstOpenApp()) {
                startActivity(GuideActivity.class, true);
            } else {
                Intent intent = new Intent(this, MainTabActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
            }
            finish();
        }, 2000);
    }
}
