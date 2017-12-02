package cn.com.wh.ring.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wh.ring.MainApplication;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.dao.PostPublishDao;
import cn.com.wh.ring.network.response.PostPublish;
import cn.com.wh.ring.ui.decoration.SpaceBottomItemDecoration;
import cn.com.wh.ring.ui.fragment.base.ButterKnifeFragment;
import cn.com.wh.ring.ui.view.LoadMoreRecyclerView;
import cn.com.wh.ring.ui.view.inter.OnLoadMoreListener;
import cn.com.wh.ring.utils.ToastUtils;

/**
 * Created by Hui on 2017/12/1.
 */

public class PostPublishFragment extends ButterKnifeFragment {
    @BindView(R.id.loadMoreRecyclerView)
    LoadMoreRecyclerView mLoadMoreRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_load_more, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mLoadMoreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //mLoadMoreRecyclerView.setAdapter(new PostPublishAdapter());
        mLoadMoreRecyclerView.addItemDecoration(new SpaceBottomItemDecoration(10));
        mLoadMoreRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                ToastUtils.showShortToast("加载更多");
                mLoadMoreRecyclerView.finishLoadingMore();
            }
        });
    }

    private List<PostPublish> getFromDb(){
        PostPublishDao postPublishDao = MainApplication.getInstance().getDaoSession().getPostPublishDao();
        List<cn.com.wh.ring.database.bean.PostPublish> dbPostPublishs = postPublishDao.queryBuilder().orderDesc(PostPublishDao.Properties.Time).list();
        if (dbPostPublishs == null || dbPostPublishs.isEmpty()){}
        return null;
    }
}
