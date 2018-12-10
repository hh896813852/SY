package com.edusoho.yunketang.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.edusoho.yunketang.R;

/**
 * Created by any on 17/6/27.
 */
public class LoadMoreListView extends ListView {
    View footView;
    View loadMoreView;
    View noMoreView;
    boolean canLoadMore = false;
    public onLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(LoadMoreListView.onLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface onLoadMoreListener {
        void onLoadMore();
    }

    public LoadMoreListView(Context context) {
        super(context);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        footView = inflate(getContext(), R.layout.view_load_more, null);
        loadMoreView = footView.findViewById(R.id.loadMoreFrame);
        noMoreView = footView.findViewById(R.id.noMoreFrame);
        addFooterView(footView, null, false);
        setCanLoadMore(canLoadMore);
        setOnScrollListener(new OnScrollListener());
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
        loadMoreView.setVisibility(canLoadMore ? VISIBLE : GONE);
        noMoreView.setVisibility(canLoadMore ? GONE : VISIBLE);
        footView.setVisibility(getCount() > 1 ? VISIBLE : GONE);
    }

    public void setCanLoadMore(boolean canLoadMore, boolean removeLocadMoreFootView) {
        setCanLoadMore(canLoadMore);
        if (!canLoadMore && removeLocadMoreFootView) {
            removeFooterView(footView);
        }
    }

    public class OnScrollListener implements AbsListView.OnScrollListener {
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (canLoadMore
                    && onLoadMoreListener != null
                    && firstVisibleItem + visibleItemCount >= totalItemCount
                    && totalItemCount != 0
                    && totalItemCount != getHeaderViewsCount() + getFooterViewsCount()
                    ) {
                onLoadMoreListener.onLoadMore();
            }
        }
    }
}
