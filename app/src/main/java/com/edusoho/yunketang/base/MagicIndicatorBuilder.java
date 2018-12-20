package com.edusoho.yunketang.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.utils.DensityUtil;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.time.format.TextStyle;

/**
 * Created by any on 16/7/26.
 */
public class MagicIndicatorBuilder {

    public interface OnNavigatorClickListener {
        void onNavigatorClickListener(int index);
    }

    public static class MagicIndicatorConfiguration {
        public String[] labels;
        public int labelTextSize = 14;
        public int titleNormalColor;
        public int titleSelectedColor;
        public int textStyle = Typeface.NORMAL;
        /**
         * LinePagerIndicator.MODE_MATCH_EDGE 直线宽度 == 均分
         * LinePagerIndicator.MODE_WRAP_CONTENT  直线宽度 == title宽度
         * LinePagerIndicator.MODE_EXACTLY 直线宽度 == mLineWidth
         */
        public int lineMode = LinePagerIndicator.MODE_MATCH_EDGE;
        public int lineColor = Color.parseColor("#17A884");
        public int lineHeight;
        public int lineWidth;
        public boolean isAdjustMode = true;

        public MagicIndicatorConfiguration(Context context) {
            this.titleNormalColor = R.color.text_gray;
            this.titleSelectedColor = R.color.theme_color;
            this.lineHeight = DensityUtil.dip2px(context, 1.5f);
        }
    }

    /**
     * 采用的默认的 MagicIndicatorConfiguration 进行构建
     *
     * @param context
     * @param labels
     * @param listener
     * @return
     */
    public static CommonNavigator buildCommonNavigator(Context context,
                                                       String[] labels,
                                                       OnNavigatorClickListener listener) {
        MagicIndicatorConfiguration configuration = new MagicIndicatorConfiguration(context);
        configuration.labels = labels;
        return buildCommonNavigator(context, configuration, listener);
    }

    public static CommonNavigator buildCommonNavigator(Context context,
                                                       MagicIndicatorConfiguration configuration,
                                                       OnNavigatorClickListener listener) {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdjustMode(configuration.isAdjustMode);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return configuration.labels == null ? 0 : configuration.labels.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(configuration.labels[index]);
                colorTransitionPagerTitleView.setTextSize(configuration.labelTextSize);
                colorTransitionPagerTitleView.setTypeface(colorTransitionPagerTitleView.getTypeface(), configuration.textStyle);
                colorTransitionPagerTitleView.setNormalColor(ContextCompat.getColor(context, configuration.titleNormalColor));
                colorTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(context, configuration.titleSelectedColor));
                colorTransitionPagerTitleView.setOnClickListener(view -> {
                    listener.onNavigatorClickListener(index);
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(configuration.lineMode);
                indicator.setLineHeight(configuration.lineHeight);
                if(configuration.lineMode == LinePagerIndicator.MODE_EXACTLY) {
                    indicator.setLineWidth(configuration.lineWidth);
                }
                indicator.setColors(configuration.lineColor);
                return indicator;
            }
        });
        return commonNavigator;
    }

    public static CommonNavigator buildCommonBottomTabNavigator(Context context, MainTabItem[] mainTabItems, OnNavigatorClickListener listener) {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mainTabItems.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                commonPagerTitleView.setContentView(R.layout.view_main_tab_item);

                // 初始化
                final ImageView iconTabItem = commonPagerTitleView.findViewById(R.id.iconTabItem);
                final TextView txtvTabItem = commonPagerTitleView.findViewById(R.id.txtvTabItem);
                txtvTabItem.setText(mainTabItems[index].title);
                if(index == 0) {
                    iconTabItem.setImageResource(mainTabItems[index].imgSrcSelected);
                } else {
                    iconTabItem.setImageResource(mainTabItems[index].imgSrcNormal);
                }
                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        iconTabItem.setImageResource(mainTabItems[index].imgSrcSelected);
                        txtvTabItem.setTextColor(context.getResources().getColor(R.color.theme_color));
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        iconTabItem.setImageResource(mainTabItems[index].imgSrcNormal);
                        txtvTabItem.setTextColor(context.getResources().getColor(R.color.text_gray));
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                    }
                });

                commonPagerTitleView.setOnClickListener(view -> listener.onNavigatorClickListener(index));

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        return commonNavigator;
    }
}