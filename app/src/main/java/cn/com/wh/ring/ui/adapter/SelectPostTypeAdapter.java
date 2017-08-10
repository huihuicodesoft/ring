package cn.com.wh.ring.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wh.ring.R;
import cn.com.wh.ring.helper.PostTypeHelper;
import cn.com.wh.ring.network.response.PostType;

/**
 * Created by Hui on 2017/8/1.
 */

public class SelectPostTypeAdapter extends RecyclerView.Adapter<SelectPostTypeAdapter.ViewHolder> {
    private List<PostType> mData;

    public SelectPostTypeAdapter(List<PostType> data) {
        mData = data;
    }

    @Override
    public SelectPostTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_post_type, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectPostTypeAdapter.ViewHolder holder, int position) {
        if (mData == null)
            return;

        PostType postType = mData.get(position);
        holder.bindData(postType);
    }

    @Override
    public int getItemCount() {
        return (mData == null || mData.isEmpty()) ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.symbol_iv)
        ImageView mSymbolIv;
        @BindView(R.id.name_tv)
        TextView mNameTv;
        @BindView(R.id.people_number_tv)
        TextView mPeopleNumberTv;
        @BindView(R.id.support_tv)
        TextView mSupportTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(PostType postType) {
            mNameTv.setText(postType.getName());
            int resId = PostTypeHelper.getTipResId(postType.getSupport());
            if (resId > 0)
                mSupportTv.setText(resId);
            else
                mSupportTv.setText("");
        }
    }
}
