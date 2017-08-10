package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wh.photo.photopicker.decoration.LineItemDecoration;
import cn.com.wh.ring.R;
import cn.com.wh.ring.helper.LoadHelper;
import cn.com.wh.ring.network.response.Page;
import cn.com.wh.ring.network.response.PostType;
import cn.com.wh.ring.network.response.Response;
import cn.com.wh.ring.network.retrofit.ListenerCallBack;
import cn.com.wh.ring.network.retrofit.NetWorkException;
import cn.com.wh.ring.network.service.Services;
import cn.com.wh.ring.ui.adapter.SelectPostTypeAdapter;
import cn.com.wh.ring.ui.view.ListSwipeRefreshLayout;
import cn.com.wh.ring.ui.view.inter.OnLoadMoreListener;
import cn.com.wh.ring.utils.ToastUtils;
import retrofit2.Call;

/**
 * Created by Hui on 2017/8/1.
 */

public class SelectPostTypeActivity extends TitleActivity implements LoadHelper.OnLoadListener {
    @BindView(R.id.listSwipeRefreshLayout)
    ListSwipeRefreshLayout mListSwipeRefreshLayout;
    private int mCurrentPage = 1;

    SelectPostTypeAdapter mAdapter;
    private List<PostType> mData = new ArrayList<>();
    private LoadHelper loadHelper = new LoadHelper();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(loadHelper.generateRoot(this, R.layout.activity_select_post_type));
        setTitle(R.string.select_post_type);
        unbinder = ButterKnife.bind(this);

        RecyclerView recyclerView = mListSwipeRefreshLayout.getRecyclerView();
        recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SelectPostTypeAdapter(mData);
        recyclerView.setAdapter(mAdapter);

        mListSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPage(1, false);
            }
        });
        mListSwipeRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadPage(mCurrentPage + 1, false);
            }
        });

        loadHelper.setOnLoadListener(this);
        loadHelper.showLoading();
    }

    @Override
    public void OnLoad() {
        loadPage(1, true);
    }

    private void loadPage(final int page, final boolean isPage) {
        Call<Response<Page<PostType>>> call = Services.postTypeService.get(0L, page, Page.DEAFULT_PAGE_SIZE);
        call.enqueue(new ListenerCallBack<Page<PostType>>(this) {
            @Override
            public void onSuccess(Page<PostType> postTypePage) {
                mCurrentPage = postTypePage.getPageNum();

                List<PostType> data = postTypePage.getList();
                if (page == 1) {

                    mData.clear();

                    if (data == null || data.isEmpty()) {
                        loadHelper.showEmpty();
                        return;
                    }
                    if (isPage) {
                        loadHelper.showSuccess();
                    } else {
                        mListSwipeRefreshLayout.setRefreshing(false);
                    }
                } else {

                    mListSwipeRefreshLayout.finishLoadingMore();

                    if (data == null || data.isEmpty()){
                        ToastUtils.showShortToast("暂无数据");
                    }
                }
                mData.addAll(postTypePage.getList());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(NetWorkException e) {
                if (page == 1) {
                    if (!isPage) {
                        mListSwipeRefreshLayout.setRefreshing(false);
                    }
                    if (mData == null || mData.isEmpty()) {
                        loadHelper.showFail();
                        return;
                    }
                } else {
                    mListSwipeRefreshLayout.finishLoadingMore();
                }
                ToastUtils.showShortToast(e.getMessage());
            }
        });
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SelectPostTypeActivity.class);
        context.startActivity(intent);
    }

}
