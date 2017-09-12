package cn.com.wh.ring.ui.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.wh.ring.R;

/**
 * Created by Hui on 2017/7/13.
 */

public class AttentionFragment extends Fragment {
    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attention, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
//        mListSwipeRefreshLayout.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
//        //mListSwipeRefreshLayout.getRecyclerView().setAdapter();
//        mListSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//            }
//        });
//        mListSwipeRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
