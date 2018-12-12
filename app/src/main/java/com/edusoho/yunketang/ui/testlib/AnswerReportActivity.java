package com.edusoho.yunketang.ui.testlib;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.databinding.ActivityAnswerReportBinding;
import com.edusoho.yunketang.widget.CircleBarView;
import com.edusoho.yunketang.widget.AnswerResultLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.activity_answer_report, title = "答题报告", rightButton = "再做一遍")
public class AnswerReportActivity extends BaseActivity<ActivityAnswerReportBinding> {

    public List<Integer> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            AnswerResultLayout answerResultLayout = view.findViewById(R.id.answerResultLayout);
            answerResultLayout.setTags(list);
            return view;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter.init(this, R.layout.item_answer_report, list);
        initView();
        loadData();
    }

    private void loadData() {
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        // 设置根据进度改变文字的TextView
        getDataBinding().circleView.setTextView(getDataBinding().progressText);
        // 设置动画进度监听来改变文字和进度条颜色
        getDataBinding().circleView.setOnAnimationListener(new CircleBarView.OnAnimationListener() {
            @Override
            public String howToChangeText(float interpolatedTime, float progressNum, float maxNum) {
                DecimalFormat decimalFormat = new DecimalFormat("0");
                return decimalFormat.format(interpolatedTime * progressNum / maxNum * 100);
            }

            @Override
            public void howToChangeProgressColor(Paint paint, float interpolatedTime, float updateNum, float maxNum) {

            }
        });
        // 设置最大值
        getDataBinding().circleView.setMaxNum(100);
        // 设置进度和动画执行时间，并开始动画
        getDataBinding().circleView.setProgressNum(80, 2000);
    }

    /**
     * 全部解析
     */
    public void onAllAnalysisClick(View view) {

    }

    /**
     * 错题解析
     */
    public void onFaultsAnalysisClick(View view) {

    }
}
