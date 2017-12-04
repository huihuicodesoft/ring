package cn.com.wh.ring.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wh.ring.MainApplication;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.bean.Address;
import cn.com.wh.ring.database.bean.PostPublish;
import cn.com.wh.ring.database.dao.AddressDao;
import cn.com.wh.ring.database.dao.PostPublishDao;
import cn.com.wh.ring.event.PostPublishEvent;
import cn.com.wh.ring.network.response.Page;
import cn.com.wh.ring.network.response.Post;
import cn.com.wh.ring.network.response.Response;
import cn.com.wh.ring.network.retrofit.ListenerCallBack;
import cn.com.wh.ring.network.retrofit.NetWorkException;
import cn.com.wh.ring.network.service.Services;
import cn.com.wh.ring.network.task.Executor;
import cn.com.wh.ring.ui.adapter.PostPublishAdapter;
import cn.com.wh.ring.ui.decoration.SpaceBottomItemDecoration;
import cn.com.wh.ring.ui.fragment.base.ButterKnifeFragment;
import cn.com.wh.ring.ui.view.LoadMoreRecyclerView;
import cn.com.wh.ring.ui.view.inter.OnLoadMoreListener;
import cn.com.wh.ring.utils.LogUtils;
import cn.com.wh.ring.utils.ToastUtils;
import retrofit2.Call;

/**
 * Created by Hui on 2017/12/1.
 */

public class PostPublishFragment extends ButterKnifeFragment {
    private static final String TAG = PostPublishFragment.class.getName();

    @BindView(R.id.loadMoreRecyclerView)
    LoadMoreRecyclerView mLoadMoreRecyclerView;
    private PostPublishAdapter mAdapter;

    private Long mMaxId = 0L;
    private int mPageNumber = 1;

    private List<Post> mData = new ArrayList<>();

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
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        mLoadMoreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PostPublishAdapter(mData);
        mLoadMoreRecyclerView.setAdapter(mAdapter);
        mLoadMoreRecyclerView.addItemDecoration(new SpaceBottomItemDecoration(10));
        mLoadMoreRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadFromNet(mPageNumber + 1, true);
            }
        });

        loadFromDb();
        loadFromNet(mPageNumber, false);
        mAdapter.notifyDataSetChanged();
    }

    private void loadFromDb() {
        PostPublishDao postPublishDao = MainApplication.getInstance().getDaoSession().getPostPublishDao();
        List<PostPublish> dbPostPublishList = postPublishDao.queryBuilder().orderDesc(PostPublishDao.Properties.Time).list();
        if (dbPostPublishList != null) {
            for (PostPublish dbPostPublish : dbPostPublishList) {

                LogUtils.logV(TAG, "开始发布帖子 = " + dbPostPublish.getId());
                if (dbPostPublish.getState() == PostPublish.STATE_PUBLISHING)
                    Executor.execute(dbPostPublish.getId());

                Post post = new Post(dbPostPublish);
                AddressDao addressDao = MainApplication.getInstance().getDaoSession().getAddressDao();
                Address address = addressDao.load(dbPostPublish.getAddressId());
                if (address != null)
                    post.setRegion(address.getCity());
                mData.add(post);
            }
        }
    }

    private void loadFromNet(int page, final boolean isNeedFinish) {
        Call<Response<Page<Post>>> call = Services.postService.get(null, mMaxId, page, Page.DEFAULT_PAGE_SIZE);
        call.enqueue(new ListenerCallBack<Page<Post>>(getContext()) {
            @Override
            public void onSuccess(Page<Post> postPage) {
                if (postPage != null) {
                    mMaxId = postPage.getMaxId();
                    if (postPage.getPageNum() <= postPage.getPages()) {
                        mPageNumber = postPage.getPageNum();
                        mData.addAll(postPage.getList());
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        ToastUtils.showShortToast("没有更多数据");
                    }

                    if (mLoadMoreRecyclerView != null && isNeedFinish) {
                        mLoadMoreRecyclerView.finishLoadingMore();
                    }
                }
            }

            @Override
            public void onFailure(NetWorkException e) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostPublishEvent postPublishEvent) {
        if (postPublishEvent.type == PostPublishEvent.TYPE_REQUEST_FAIL || postPublishEvent.type == PostPublishEvent.TYPE_REQUEST_SUCCESS) {
            for (Post post : mData) {
                if (post.getId() == postPublishEvent.id && !TextUtils.isEmpty(post.getUuid())) {
                    if (postPublishEvent.type == PostPublishEvent.TYPE_REQUEST_FAIL) {
                        LogUtils.logV(TAG, "帖子发布失败 = " + postPublishEvent.id);
                        post.setState(PostPublish.STATE_FAIL);
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        LogUtils.logV(TAG, "帖子发布成功 = " + postPublishEvent.id);
                        post.setState(PostPublish.STATE_SUCCESS);
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
