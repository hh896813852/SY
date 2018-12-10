package com.edusoho.yunketang.ui.exercise;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Question;
import com.edusoho.yunketang.databinding.FragmentIntegratedChildBinding;
import com.edusoho.yunketang.utils.RequestCodeUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.fragment_integrated_child)
public class IntegratedChildFragment extends BaseFragment<FragmentIntegratedChildBinding> {
    private static final int REQUEST_IMAGE = RequestCodeUtil.next();
    private Question question;

    public List<String> picList = new ArrayList<>();
    public SYBaseAdapter picAdapter = new SYBaseAdapter();

    public List<Object> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ImageView answerImage = view.findViewById(R.id.answerImage);
            ImageView deleteView = view.findViewById(R.id.deleteView);
            FrameLayout pickView = view.findViewById(R.id.pickView);
            if (position == list.size() - 1) { // 添加照片
                pickView.setVisibility(View.VISIBLE);
                pickView.setOnClickListener(v -> onPicPickClick());
                deleteView.setVisibility(View.GONE);
            } else { // 已添加并显示的照片
                Glide.with(getSupportedActivity()).load((Uri) list.get(position)).placeholder(R.drawable.bg_load_default_3x4).into(answerImage);
                pickView.setVisibility(View.GONE);
                deleteView.setVisibility(View.VISIBLE);
                deleteView.setOnClickListener(v -> {
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                });
            }
            return view;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) { // 照片选择返回
                list.remove(list.size() - 1);
                list.addAll(Matisse.obtainResult(data));
                list.add("");
                adapter.notifyDataSetChanged();
            }
        }
    }

    public static IntegratedChildFragment newInstance(Question question) {
        IntegratedChildFragment fragment = new IntegratedChildFragment();
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
        picList.add("");
        picList.add("");
        picAdapter.init(getSupportedActivity(), R.layout.item_pic, picList);

        list.add("");
        adapter.init(getSupportedActivity(), R.layout.item_pic_pick, list);
    }

    /**
     * 图片选择
     */
    private void onPicPickClick() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG)) // 显示图片的类型
                .countable(false) // 是否有序选择图片
                .maxSelectable(99)// 最大选择数量
                .capture(true)    // 是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.edusoho.yunketang.fileprovider"))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size)) // 图片显示表格的大小
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // 图像选择和预览活动所需的方向。
                .thumbnailScale(0.85f)          // 缩放比例
                .theme(R.style.Matisse_Zhihu)   // 主题  暗色主题 R.style.Matisse_Dracula
                .imageEngine(new GlideEngine()) // 加载方式
                .forResult(REQUEST_IMAGE);      // 请求码
    }
}
