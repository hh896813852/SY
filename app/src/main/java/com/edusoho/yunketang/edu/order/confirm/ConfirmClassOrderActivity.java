package com.edusoho.yunketang.edu.order.confirm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.edu.bean.Classroom;
import com.edusoho.yunketang.edu.bean.CourseSet;

/**
 * Created by RexXiang on 2017/7/5.
 */

public class ConfirmClassOrderActivity extends ConfirmOrderActivity {

    private static final String CLASSROOM_ID = "classroom_id";

    private int mClassroomId;
    private TextView mFromHint;
    private View mFromLine;

    public static void launch(Context context, int classroomId) {
        Intent intent = new Intent(context, ConfirmClassOrderActivity.class);
        intent.putExtra(CLASSROOM_ID, classroomId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mClassroomId = getIntent().getIntExtra(CLASSROOM_ID, 0);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        mFromLine = findViewById(R.id.from_line);
        mFromHint = (TextView) findViewById(R.id.tv_from);
    }

    @Override
    protected void initData() {
        ConfirmOrderContract.Presenter mPresenter = new ConfirmClassOrderPresenter(this, mClassroomId);
        mPresenter.subscribe();
    }

    @Override
    public void showTopView(Classroom classroom) {
        Glide.with(this).load(classroom.cover.large).placeholder(R.drawable.bg_load_default_4x3).error(R.drawable.bg_load_default_4x3).into(mCourseImg);
        mCourseProjectFrom.setVisibility(View.GONE);
        mFromHint.setVisibility(View.GONE);
        mFromLine.setVisibility(View.GONE);
    }

    @Override
    public void showTopView(CourseSet courseSet) {

    }
}
