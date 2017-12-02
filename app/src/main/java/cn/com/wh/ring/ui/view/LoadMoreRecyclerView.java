package cn.com.wh.ring.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import cn.com.wh.ring.ui.view.inter.OnLoadMoreListener;

/**
 * Created by Hui on 2017/12/1.
 */

public class LoadMoreRecyclerView extends RecyclerView {
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoadingMore;

    public LoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0 || isLoadingMore)
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
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void finishLoadingMore() {
        isLoadingMore = false;
    }

}
