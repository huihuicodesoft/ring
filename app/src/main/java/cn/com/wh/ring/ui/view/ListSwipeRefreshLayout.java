package cn.com.wh.ring.ui.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import cn.com.wh.ring.ui.view.inter.OnLoadMoreListener;

/**
 * Created by Hui on 2017/7/14.
 */

public class ListSwipeRefreshLayout extends SwipeRefreshLayout {
    private RecyclerView recyclerView;
    private boolean isLoadingMore;
    private OnLoadMoreListener onLoadMoreListener;
    private OnRefreshListener onRefreshListener;

    public ListSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public ListSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        createRecyclerView();
    }

    public void notifyRefresh() {
        if (!isRefreshing()) {
            setRefreshing(true);
            if (onRefreshListener != null) {
                onRefreshListener.onRefresh();
            }
        }
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        onRefreshListener = listener;
        super.setOnRefreshListener(onRefreshListener);
    }

    private void createRecyclerView() {
        recyclerView = new RecyclerView(getContext());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0 || isRefreshing() || isLoadingMore)
                    return;

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (layoutManager != null && layoutManager instanceof LinearLayoutManager && adapter != null) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    int count = adapter.getItemCount();
                    if (lastVisibleItemPosition + 1 == count) {
                        if (onLoadMoreListener != null) {
                            isLoadingMore = true;
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
            }
        });
        recyclerView.setContentDescription("");
        addView(recyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void finishLoadingMore() {
        isLoadingMore = false;
    }
}
