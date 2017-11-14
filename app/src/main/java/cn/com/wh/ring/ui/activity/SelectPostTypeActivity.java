package cn.com.wh.ring.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

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
import cn.com.wh.ring.ui.activity.base.TitleActivity;
import cn.com.wh.ring.ui.adapter.SelectPostTypeAdapter;
import cn.com.wh.ring.ui.view.ListSwipeRefreshLayout;
import cn.com.wh.ring.ui.view.inter.OnLoadMoreListener;
import cn.com.wh.ring.utils.ToastUtils;
import retrofit2.Call;

/**
 * Created by Hui on 2017/8/1.
 */

public class SelectPostTypeActivity extends TitleActivity implements LoadHelper.OnLoadListener, SelectPostTypeAdapter.OnItemClickListener {
    private static final String INTENT_DATA = "data";

    @BindView(R.id.listSwipeRefreshLayout)
    ListSwipeRefreshLayout mListSwipeRefreshLayout;

    private long mMaxId = 0L;
    private int mCurrentPage = 1;

    private SelectPostTypeAdapter mAdapter;
    private List<PostType> mData = new ArrayList<>();
    private LoadHelper loadHelper = new LoadHelper();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(loadHelper.generateRoot(this, R.layout.activity_select_post_type));
        unbinder = ButterKnife.bind(this);

        mListSwipeRefreshLayout.addItemDecoration(new LineItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        mListSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SelectPostTypeAdapter(mData);
        mAdapter.setOnItemClickListener(this);
        mListSwipeRefreshLayout.setAdapter(mAdapter);

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
        loadHelper.setState(LoadHelper.LOADING);
    }

    @Override
    public void OnLoad() {
        loadPage(1, true);
    }

    private void loadPage(final int page, final boolean isHelperRequest) {
        Call<Response<Page<PostType>>> call = Services.postTypeService.get(page == 1 ? 0L : mMaxId, page, Page.DEFAULT_PAGE_SIZE);
        call.enqueue(new ListenerCallBack<Page<PostType>>(this) {
            @Override
            public void onSuccess(Page<PostType> postTypePage) {
                mCurrentPage = postTypePage.getPageNum();

                if (mCurrentPage == 1) {
                    List<PostType> list = postTypePage.getList();
                    if (list != null && !list.isEmpty())
                        mMaxId = list.get(0).getId();
                }

                if (loadHelper != null && !loadHelper.interceptSuccess(page, isHelperRequest, postTypePage.getList())) {
                    mData.addAll(postTypePage.getList());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(NetWorkException e) {
                if (loadHelper != null && !loadHelper.interceptError(page, isHelperRequest, true)) {
                    ToastUtils.showShortToast(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, PostType postType) {
        Intent intent = getIntent();
        if (intent != null) {
            intent.putExtra(INTENT_DATA, postType);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    public static void startForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, SelectPostTypeActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    public static PostType getIntentData(Intent intent) {
        return (PostType) intent.getSerializableExtra(INTENT_DATA);
    }
}
