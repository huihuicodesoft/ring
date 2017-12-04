package cn.com.wh.ring.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wh.photo.photopicker.widget.PTNinePhotoLayout;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.bean.PostPublish;
import cn.com.wh.ring.network.response.Post;
import cn.com.wh.ring.network.response.PostType;
import cn.com.wh.ring.network.task.Executor;
import cn.com.wh.ring.utils.NumberUtils;
import cn.com.wh.ring.utils.TimeUtils;

/**
 * Created by Hui on 2017/8/17.
 */

public class PostPublishAdapter extends RecyclerView.Adapter<PostPublishAdapter.ViewHolder> {
    private List<Post> mData;
    private OnItemClickListener mOnItemClickListener;

    public PostPublishAdapter(List<Post> data) {
        mData = data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public PostPublishAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_publish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostPublishAdapter.ViewHolder holder, final int position) {
        if (mData == null)
            return;

        final Post post = mData.get(position);
        holder.bindData(post);
        if (mOnItemClickListener != null) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(v, post);
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return (mData == null || mData.isEmpty()) ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.state_tv)
        TextView mStateTv;
        @BindView(R.id.time_tv)
        TextView mTimeTv;
        @BindView(R.id.content_tv)
        TextView mContentTv;
        @BindView(R.id.ptSortableNinePhotoLayout)
        PTNinePhotoLayout mPTNinePhotoLayout;
        @BindView(R.id.address_tv)
        TextView mAddressTv;

        @BindView(R.id.item_handler_praise_tv)
        TextView mPraiseTv;
        @BindView(R.id.item_handler_criticize_tv)
        TextView mCriticizeTv;
        @BindView(R.id.item_handler_comment_tv)
        TextView mCommentTv;
        @BindView(R.id.item_handler_forward_tv)
        TextView mForwardTv;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(final Post post) {
            mStateTv.setOnClickListener(null);

            if (post.getState() == PostPublish.STATE_PUBLISHING) {
                mStateTv.setText(R.string.publishing);
            } else if (post.getState() == PostPublish.STATE_FAIL) {
                mStateTv.setText(R.string.publish_fail);
                mStateTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Executor.execute(post.getId());
                    }
                });
            } else if (post.getState() == PostPublish.STATE_SUCCESS || post.getState() == Post.STATE_CHECKING) {
                mStateTv.setText(R.string.publish_checking);
            } else if (post.getState() == Post.STATE_CHECK_FAIL) {
                mStateTv.setText(R.string.publish_check_fail);
            } else {
                mStateTv.setText(R.string.publish_check_success);
            }

            mTimeTv.setText(TimeUtils.getFormatTimeString(post.getCreationTime(), System.currentTimeMillis()));
            PostType postType = post.getPostType();
            String content = post.getDescription();
            if (postType != null) {
                content = content + "#" + (TextUtils.isEmpty(postType.getName()) ? "" : postType.getName()) + "#";
            }
            mContentTv.setText(content);
            List<String> mediaList = post.getMediaList();
            if (mediaList == null || mediaList.isEmpty()) {
                mPTNinePhotoLayout.setData(Collections.<String>emptyList());
            } else {
                mPTNinePhotoLayout.setData(mediaList);
            }
            mAddressTv.setText(post.getRegion());

            mPraiseTv.setText(NumberUtils.getString(post.getPraiseNumber()));
            mCriticizeTv.setText(NumberUtils.getString(post.getCriticizeNumber()));
            mCommentTv.setText(NumberUtils.getString(post.getCommentNumber()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Post post);
    }
}
