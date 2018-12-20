package com.edusoho.yunketang.ui.testlib;

import android.databinding.ObservableField;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.AnswerReport;
import com.edusoho.yunketang.databinding.ActivityAnswerReportBinding;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.widget.AnswerResultLayout;
import com.edusoho.yunketang.widget.CircleBarView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Layout(value = R.layout.activity_answer_report, title = "答题报告", rightButton = "再做一遍")
public class AnswerReportActivity extends BaseActivity<ActivityAnswerReportBinding> {
    public static final String HOMEWORK_ID = "homework_id";
    private String homeworkId;

    public ObservableField<String> trueNum = new ObservableField<>();
    public ObservableField<String> falseNum = new ObservableField<>();

    private List<AnswerReport.AnswerDetails> answerDetailsList;

    public List<List<Integer>> list = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView correctView = view.findViewById(R.id.correctView);
            TextView falseView = view.findViewById(R.id.falseView);
            TextView questionTypeView = view.findViewById(R.id.questionTypeView);
            correctView.setText("对" + answerDetailsList.get(position).correctCount);
            falseView.setText("错" + answerDetailsList.get(position).falseCount);
            questionTypeView.setText(answerDetailsList.get(position).alias);
            AnswerResultLayout answerResultLayout = view.findViewById(R.id.answerResultLayout);
            answerResultLayout.setTags(list.get(position));
            return view;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeworkId = getIntent().getStringExtra(HOMEWORK_ID);
        adapter.init(this, R.layout.item_answer_report, list);
        initView();
        loadData();
    }

    private void initView() {
        // TODO 暂不显示再做一遍
        setRightButtonTextView("");
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
    }

    private void loadData() {
        SYDataTransport.create(SYConstants.CHECK_REPORT)
                .addParam("homeworkId", homeworkId)
                .execute(new SYDataListener<AnswerReport>() {

                    @Override
                    public void onSuccess(AnswerReport data) {
                        answerDetailsList = data.returnList;
                        refreshView(data);
                        // 刷新答题报告
                        refreshAnswerReport();
                    }
                }, AnswerReport.class);
    }

    /**
     * 刷新界面
     */
    private void refreshView(AnswerReport data) {
        trueNum.set(String.valueOf(data.correctSum));
        falseNum.set(String.valueOf(data.falseSum));
        // 设置进度和动画执行时间，并开始动画
        getDataBinding().circleView.setProgressNum(Float.valueOf(data.percent), 2000);
    }

    /**
     * 刷新答题报告
     */
    private void refreshAnswerReport() {
        list.clear();
        for (AnswerReport.AnswerDetails details : answerDetailsList) {
            List<Integer> child = new ArrayList<>();
            for (int i = 0; i < details.falseCount + details.correctCount; i++) {
                child.add(AnswerResultLayout.ANSWER_TRUE);
            }
            for (AnswerReport.AnswerDetails.Mistake mistake : details.homeworkMistakes) {
                child.set(mistake.idex - 1, mistake.type == 0 ? AnswerResultLayout.ANSWER_FALSE : AnswerResultLayout.NOT_ANSWER);
            }
            list.add(child);
        }
        adapter.notifyDataSetChanged();
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
