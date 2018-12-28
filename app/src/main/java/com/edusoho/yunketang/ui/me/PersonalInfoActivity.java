package com.edusoho.yunketang.ui.me;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.User;
import com.edusoho.yunketang.helper.ImageUploadHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.CropUtils;
import com.edusoho.yunketang.utils.ProgressDialogUtil;
import com.edusoho.yunketang.utils.RequestCodeUtil;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.activity_personal_info, title = "个人信息")
public class PersonalInfoActivity extends BaseActivity {
    private static final int REQUEST_IMAGE = RequestCodeUtil.next();

    public ObservableField<String> avatar = new ObservableField<>();
    public ObservableField<String> nickname = new ObservableField<>();
    public ObservableField<String> sex = new ObservableField<>();
    public ObservableField<String> personSign = new ObservableField<>();

    private List<String> optionsItems = new ArrayList<>();
    private User loginUser;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) { // 照片选择返回
                CropUtils.startUCrop(PersonalInfoActivity.this, Matisse.obtainResult(data).get(0), 1, 1);
            }
            if (requestCode == UCrop.REQUEST_CROP) { // 裁剪返回
                ProgressDialogUtil.showProgress(this, "正在更新头像...");
                avatar.set(CropUtils.getCropPath());
                ImageUploadHelper.uploadImage(new File(CropUtils.getCropPath()), url -> {
                    ProgressDialogUtil.hideProgress();
                    avatar.set(url);
                });
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        loginUser = getLoginUser().syjyUser;
        optionsItems.add("男");
        optionsItems.add("女");

        avatar.set(TextUtils.isEmpty(loginUser.headImg) ? "" : loginUser.headImg);
        nickname.set(TextUtils.isEmpty(loginUser.nickName) ? "" : loginUser.nickName);
        sex.set(loginUser.sex == 0 ? "" : getSex(loginUser.sex));
        personSign.set(TextUtils.isEmpty(loginUser.personSign) ? "" : loginUser.personSign);
    }

    /**
     * 头像选择
     */
    public void onAvatarClick(View view) {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG)) // 显示图片的类型
                .countable(false) // 是否有序选择图片
                .maxSelectable(1)// 最大选择数量
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
     * 性别选择
     */
    public void onSexPickClick(View view) {
        closeSoftInputKeyBoard();

        PickerOptions options = new PickerOptions(PickerOptions.TYPE_PICKER_OPTIONS);
        options.context = this;
        options.optionsSelectListener = (options1, options2, options3, v) -> {
            sex.set(optionsItems.get(options1));
        };
        // 条件选择器
        OptionsPickerView<String> optionsPickerView = new OptionsPickerView<>(options);

        optionsPickerView.setPicker(optionsItems);
        optionsPickerView.show();
    }

    /**
     * 保存
     * "id": 0,
     * "name": "string",
     * "studentType": "string",
     * "sex": "string",
     * "mobile": "string",
     * "nickName": "string",
     * "personSign": "string",
     * "headImg": "string"
     */
    public void onSaveClick(View view) {
        SYDataTransport.create(SYConstants.SAVE_PERSONAL_INFO)
                .addParam("id", getLoginUser().syjyUser.id)
                .addParam("sex", getSexCode(sex.get()))
                .addParam("nickName", nickname.get())
                .addParam("personSign", personSign.get())
                .addParam("headImg", avatar.get())
                .execute(new SYDataListener() {

                    @Override
                    public void onSuccess(Object data) {
                        loginUser.headImg = avatar.get();
                        loginUser.nickName = nickname.get();
                        loginUser.sex = getSexCode(sex.get());
                        loginUser.personSign = personSign.get();
                        SYApplication.getInstance().reSaveUser();
                        finish();
                    }
                });
    }

    /**
     * 获取性别代号
     */
    private int getSexCode(String sex) {
        switch (sex) {
            case "男":
                return 177;
            case "女":
                return 178;
            default:
                return 179;
        }
    }

    /**
     * 获取性别
     */
    private String getSex(int SexCode) {
        switch (SexCode) {
            case 177:
                return "男";
            case 178:
                return "女";
            default:
                return "";
        }
    }

    /**
     * 关闭软键盘
     */
    private void closeSoftInputKeyBoard() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im.isActive()) {
            im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
