package com.edusoho.yunketang.base.mvvm.textview;

import android.databinding.BindingAdapter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.edusoho.yunketang.utils.BitmapUtil;
import com.edusoho.yunketang.utils.html.MyHtmlTagHandler;

public class TextViewBindingAdapter {

    @BindingAdapter({"htmlText"})
    public static void htmlText(TextView textView, String htmlText) {
        // 如果不在HTML标签最前面加入其他HTML元素，此函数可能不会生效，原因未知。
//        String htmlStr = "<br/><font size=\"15px\" color=\"red\">This is some text!</font><br/><font size=\"18px\" color=\"blue\">This is some text!</font>";
        if (!TextUtils.isEmpty(htmlText)) {
            htmlText = htmlText.replace("font", "syfont");
            showHtml(textView, htmlText);
        }
    }

    private static void showHtml(TextView textView, String content) {
        CharSequence html = Html.fromHtml(content, source -> {
            Drawable drawable = new BitmapDrawable(textView.getContext().getResources(), BitmapUtil.base64ToBitmap(source));
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return drawable;
        }, new MyHtmlTagHandler("syfont"));
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(html);
        // 如果含有{{包裹文字}}，则将"包裹文字"添加下划线
        if (spanBuilder.toString().contains("{{") && spanBuilder.toString().contains("}}")) {
            int start = spanBuilder.toString().indexOf("{{");
            int end = spanBuilder.toString().indexOf("}}");
            spanBuilder.delete(start, start + 2);
            spanBuilder.delete(end - 2, end);
            SpannableString spannableString = new SpannableString(spanBuilder);
            UnderlineSpan underlineSpan = new UnderlineSpan();
            spannableString.setSpan(underlineSpan, start, end - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        } else {
            textView.setText(spanBuilder);
        }
        //设置可以点击超连接
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
