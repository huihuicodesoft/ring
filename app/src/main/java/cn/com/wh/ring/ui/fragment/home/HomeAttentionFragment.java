package cn.com.wh.ring.ui.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.ring.R;
import cn.com.wh.ring.ui.adapter.PostAdapter;
import cn.com.wh.ring.ui.decoration.SpaceBottomItemDecoration;
import cn.com.wh.ring.ui.fragment.base.ButterKnifeFragment;
import cn.com.wh.ring.ui.view.ListSwipeRefreshLayout;
import cn.com.wh.ring.ui.view.inter.OnLoadMoreListener;
import cn.com.wh.ring.utils.ToastUtils;

/**
 * Created by Hui on 2017/7/13.
 */

public class HomeAttentionFragment extends ButterKnifeFragment {
    @BindView(R.id.listSwipeRefreshLayout)
    ListSwipeRefreshLayout mListSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_attention, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mListSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        mListSwipeRefreshLayout.setAdapter(new PostAdapter());
        mListSwipeRefreshLayout.addItemDecoration(new SpaceBottomItemDecoration(10));
        mListSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToastUtils.showShortToast("刷新");
                mListSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mListSwipeRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                ToastUtils.showShortToast("加载更多");
                mListSwipeRefreshLayout.finishLoadingMore();
            }
        });
    }

    @OnClick(R.id.refresh_iv)
    void onRefresh() {
        mListSwipeRefreshLayout.toRefresh();
    }

}
