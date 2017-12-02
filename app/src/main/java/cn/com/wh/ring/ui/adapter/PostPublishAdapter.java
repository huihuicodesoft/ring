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
import cn.com.wh.ring.network.response.Post;
import cn.com.wh.ring.network.response.PostPublish;
import cn.com.wh.ring.network.response.PostType;
import cn.com.wh.ring.utils.NumberUtils;
import cn.com.wh.ring.utils.TimeUtils;

/**
 * Created by Hui on 2017/8/17.
 */

public class PostPublishAdapter extends RecyclerView.Adapter<PostPublishAdapter.ViewHolder> {
    private List<PostPublish> mData;
    private OnItemClickListener mOnItemClickListener;

    public PostPublishAdapter(List<PostPublish> data) {
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

        final PostPublish postPublish = mData.get(position);
        holder.bindData(postPublish);
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

        public void bindData(final PostPublish postPublish) {
            String stateText;
            mStateTv.setOnClickListener(null);
            if (postPublish.getState() == cn.com.wh.ring.database.bean.PostPublish.STATE_PUBLISHING) {
                stateText = "发布中";
            } else if (postPublish.getState() == cn.com.wh.ring.database.bean.PostPublish.STATE_SUCCESS) {
                stateText = "发布成功";
            } else {
                stateText = "发布失败";
                mStateTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //new PostPublishTask(post).start();
                    }
                });
            }

            mStateTv.setText(stateText);
            mTimeTv.setText(TimeUtils.getFormatTimeString(postPublish.getCreationTime(), System.currentTimeMillis()));
            PostType postType = postPublish.getPostType();
            String content = postPublish.getDescription();
            if (postType != null) {
                content = content + "#" + (TextUtils.isEmpty(postType.getName()) ? "" : postType.getName()) + "#";
            }
            mContentTv.setText(content);
            List<String> mediaList = postPublish.getMediaList();
            if (mediaList == null || mediaList.isEmpty()) {
                mPTNinePhotoLayout.setData(Collections.<String>emptyList());
            } else {
                mPTNinePhotoLayout.setData(mediaList);
            }
            mAddressTv.setText(postPublish.getRegion());

            mPraiseTv.setText(NumberUtils.getString(postPublish.getPraiseNumber()));
            mCriticizeTv.setText(NumberUtils.getString(postPublish.getCriticizeNumber()));
            mCommentTv.setText(NumberUtils.getString(postPublish.getCommentNumber()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Post post);
    }
}
