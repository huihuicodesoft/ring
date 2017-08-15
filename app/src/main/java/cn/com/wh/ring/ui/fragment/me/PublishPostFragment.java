package cn.com.wh.ring.ui.fragment.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.wh.photo.photopicker.decoration.LineItemDecoration;
import cn.com.wh.ring.R;
import cn.com.wh.ring.network.response.PostType;
import cn.com.wh.ring.ui.adapter.SelectPostTypeAdapter;
import cn.com.wh.ring.ui.view.ListSwipeRefreshLayout;
import cn.com.wh.ring.ui.view.inter.OnLoadMoreListener;

/**
 * Created by Hui on 2017/8/10.
 */

public class PublishPostFragment extends Fragment {
    Unbinder unbinder;

    @BindView(R.id.listSwipeRefreshLayout)
    ListSwipeRefreshLayout mListSwipeRefreshLayout;

    SelectPostTypeAdapter mAdapter;
    List<PostType> mData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_publish_post, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        RecyclerView recyclerView = mListSwipeRefreshLayout.getRecyclerView();
        recyclerView.addItemDecoration(new LineItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SelectPostTypeAdapter(mData);
        recyclerView.setAdapter(mAdapter);
        mListSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addData(true);
                mListSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mListSwipeRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                addData(false);
                mListSwipeRefreshLayout.finishLoadingMore();
            }
        });
    }

    private void addData(boolean isClear) {
        if (isClear)
            mData.clear();

        for (int i = 0; i < 20; i++) {
            PostType postType = new PostType();
            postType.setName(""+i);
            mData.add(postType);
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
