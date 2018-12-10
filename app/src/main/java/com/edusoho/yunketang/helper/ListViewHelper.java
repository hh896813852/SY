package com.edusoho.yunketang.helper;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewHelper {

    /**
     * 动态获取ListView的高度
     * <p>
     * 注意;
     * 1、listview的item的根布局一定要是LinearLayout；
     * 2、调用这个方法需要在适配器数据加载更新之后；代码如下：
     * mAdapter.notifyDataSetChanged();  
     * ListViewHelper.getTotalHeightOfListView(mListView); 
     */
    public static int getTotalHeightOfListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//        listView.requestLayout();
        return totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
    }

    /**
     * 动态获取ListView指定item的高度
     * <p>
     * 注意;
     * 1、listview的item的根布局一定要是LinearLayout；
     * 2、调用这个方法需要在适配器数据加载更新之后；代码如下：
     * mAdapter.notifyDataSetChanged();  
     * ListViewHelper.getTotalHeightOfListView(mListView); 
     */
    public static int getPositionHeightOfListView(ListView listView, int position) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int positionHeight = 0;
        for (int i = 0; i <= position; i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            positionHeight += mView.getMeasuredHeight();
        }
        return positionHeight + (listView.getDividerHeight() * position);
    }
}
