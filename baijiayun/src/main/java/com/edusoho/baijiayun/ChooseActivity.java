package com.edusoho.baijiayun;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.baijia.playbackui.PBRoomUI;
import com.baijiahulian.live.ui.LiveSDKWithUI;
import com.baijiahulian.livecore.context.LPConstants;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.isEmpty()) {
            finish();
            return;
        }

        boolean replayState = bundle.getBoolean("replayState", false);
        if (replayState) {
            String roomId = bundle.getString("classId");
            String token = bundle.getString("token");
            PBRoomUI.enterPBRoom(ChooseActivity.this, roomId, token, "-1", LPConstants.LPDeployType.Product, new PBRoomUI.OnEnterPBRoomFailedListener() {
                @Override
                public void onEnterPBRoomFailed(String msg) {
                    Toast.makeText(ChooseActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            String roomId = bundle.getString("roomId");
            String userName = bundle.getString("userName");
            String userNumber = bundle.getString("userNumber");
            String userType = bundle.getString("userType");
            String userAvatar = bundle.getString("userAvatar");
            String sign = bundle.getString("sign");
            LiveSDKWithUI.LiveRoomUserModel userModel = new LiveSDKWithUI.LiveRoomUserModel(
                    userName, userAvatar, userNumber, LPConstants.LPUserType.from(Integer.parseInt(userType)));
            LiveSDKWithUI.enterRoom(this, Long.parseLong(roomId), sign, userModel, new LiveSDKWithUI.LiveSDKEnterRoomListener() {
                @Override
                public void onError(String msg) {
                    Toast.makeText(ChooseActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });

        }
        finish();
    }
}
