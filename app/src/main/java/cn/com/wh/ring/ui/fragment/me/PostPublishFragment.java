package cn.com.wh.ring.ui.fragment.me;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.wh.photo.photopicker.decoration.LineItemDecoration;
import cn.com.wh.ring.MainApplication;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.bean.PostPublish;
import cn.com.wh.ring.database.dao.PostPublishDao;
import cn.com.wh.ring.event.PostPublishEvent;
import cn.com.wh.ring.network.response.Page;
import cn.com.wh.ring.network.response.Post;
import cn.com.wh.ring.network.response.PostType;
import cn.com.wh.ring.network.response.Response;
import cn.com.wh.ring.ui.adapter.PostPublishAdapter;
import cn.com.wh.ring.ui.fragment.base.ScrollAbleFragment;
import cn.com.wh.ring.ui.view.ListSwipeRefreshLayout;
import cn.com.wh.ring.ui.view.inter.OnLoadMoreListener;
import cn.com.wh.ring.utils.ToastUtils;
import retrofit2.Call;

/**
 * Created by Hui on 2017/8/10.
 */

public class PostPublishFragment extends ScrollAbleFragment {
    Unbinder unbinder;

    @BindView(R.id.listSwipeRefreshLayout)
    ListSwipeRefreshLayout mListSwipeRefreshLayout;

    private long mMaxId = 0L;
    private int mCurrentPage = 1;

    PostPublishAdapter mAdapter;
    List<Post> mDbData = new ArrayList<>();
    List<Post> mData = new ArrayList<>();

    Call<Response<Page<Post>>> mPostCall;

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
        loadAllDb();
        initView();
        EventBus.getDefault().register(this);

        mListSwipeRefreshLayout.notifyRefresh();
    }

    private void initView() {
        RecyclerView recyclerView = mListSwipeRefreshLayout.getRecyclerView();
        recyclerView.addItemDecoration(new LineItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PostPublishAdapter(mData);
        recyclerView.setAdapter(mAdapter);
        mListSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPage(1);
            }
        });
        mListSwipeRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadPage(mCurrentPage + 1);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostPublishEvent event) {
        if (event.type == PostPublishEvent.TYPE_SKIP_POST) {
            if (event.id != null)
                loadDb(event.id);
        } else if (event.type == PostPublishEvent.TYPE_REQUEST_FAIL) {
            ToastUtils.showShortToast("发表失败");
            changeDataState(event.id, PostPublish.STATE_FAIL);
        } else if (event.type == PostPublishEvent.TYPE_REQUEST_SUCCESS) {
            ToastUtils.showShortToast("发表成功");
            if (changeDataState(event.id, PostPublish.STATE_SUCCESS)) {
                PostPublishDao postPublishDao = MainApplication.getInstance().getDaoSession().getPostPublishDao();
                postPublishDao.deleteByKey(event.id);
            }
        }
    }

    public boolean changeDataState(long id, int state) {
        for (int i = 0; i < mData.size(); i++) {
            Post post = mData.get(i);
            if (post.getId() == id) {
                post.setState(state);
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    private void loadAllDb() {
        PostPublishDao postPublishDao = MainApplication.getInstance().getDaoSession().getPostPublishDao();
        // 通过构建 QueryBuilder 来实现查询功能
        QueryBuilder<PostPublish> queryBuilder = postPublishDao.queryBuilder().orderDesc(PostPublishDao.Properties.Time);
        List<PostPublish> list = queryBuilder.list();
        if (list != null && !list.isEmpty()) {
            mDbData.clear();
            for (int i = 0; i < list.size(); i++) {
                PostPublish postPublish = list.get(i);
                Post post = postPublishToPost(postPublish);
                mDbData.add(post);
            }
        }
        mData.clear();
        mData.addAll(mDbData);
    }

    private void loadDb(long id) {
        PostPublishDao postPublishDao = MainApplication.getInstance().getDaoSession().getPostPublishDao();
        PostPublish postPublish = postPublishDao.load(id);
        Post post = postPublishToPost(postPublish);
        mData.add(0, post);
        mAdapter.notifyDataSetChanged();
    }

    @NonNull
    private Post postPublishToPost(PostPublish postPublish) {
        Post post = new Post();
        post.setId(postPublish.getId());
        post.setAddressCode(postPublish.getAddressCode());
        post.setAnonymous(postPublish.getAnonymous());
        post.setCreationTime(postPublish.getTime());
        post.setDescription(postPublish.getContent());
        post.setState(postPublish.getState());

        Gson gson = new Gson();
        PostType postType = gson.fromJson(postPublish.getType(), PostType.class);
        if (postType != null)
            post.setPostType(postType);

        List<String> list = gson.fromJson(postPublish.getMediaContent(), List.class);
        if (list != null)
            post.setMediaList(list);

        return post;
    }

    private void loadPage(final int page) {
        cancelCall();

        if (page == 1) {
            mData.clear();
        }
        List<Post> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Post post = new Post();
            post.setDescription("位置 =" + i);
            list.add(post);
        }
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
        mListSwipeRefreshLayout.setRefreshing(false);

//        mPostCall = Services.postService.get(null, page == 1 ? 0L : mMaxId, page, Page.DEFAULT_PAGE_SIZE);
//        mPostCall.enqueue(new ListenerCallBack<Page<Post>>(getActivity()) {
//            @Override
//            public void onSuccess(Page<Post> postPage) {
//                mCurrentPage = postPage.getPageNum();
//
//                List<Post> list = postPage.getList();
//                if (mCurrentPage == 1) {
//                    if (list != null && !list.isEmpty())
//                        mMaxId = list.get(0).getId();
//
//                    mData.clear();
//
//                    mData.addAll(list);
//                    mAdapter.notifyDataSetChanged();
//
//                    if (mListSwipeRefreshLayout != null)
//                        mListSwipeRefreshLayout.setRefreshing(false);
//                } else {
//                    if (list == null || list.isEmpty()) {
//                        ToastUtils.showShortToast("无更多数据");
//                    } else {
//                        mData.addAll(list);
//                        mAdapter.notifyDataSetChanged();
//                    }
//
//                    if (mListSwipeRefreshLayout != null)
//                        mListSwipeRefreshLayout.finishLoadingMore();
//                }
//            }
//
//            @Override
//            public void onFailure(NetWorkException e) {
//                if (page == 1) {
//                    if (mListSwipeRefreshLayout != null)
//                        mListSwipeRefreshLayout.setRefreshing(false);
//                } else {
//                    if (mListSwipeRefreshLayout != null)
//                        mListSwipeRefreshLayout.finishLoadingMore();
//                    ToastUtils.showShortToast(e.getMessage());
//                }
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        cancelCall();
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        EventBus.getDefault().unregister(this);
    }

    private void cancelCall() {
        if (mPostCall != null && mPostCall.isExecuted()) {
            mPostCall.cancel();
            mPostCall = null;
        }
    }

    @Override
    public View getScrollableView() {
        return mListSwipeRefreshLayout.getRecyclerView();
    }
}
