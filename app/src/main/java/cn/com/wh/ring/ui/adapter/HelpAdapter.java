package cn.com.wh.ring.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wh.ring.R;
import cn.com.wh.ring.network.response.BannerItem;
import cn.com.wh.ring.network.response.Help;
import cn.com.wh.ring.ui.view.banner.ImageBanner;

/**
 * Created by Hui on 2017/9/14.
 */

public class HelpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_BANNER = 0x46;
    private static final int TYPE_HELP = 0x45;
    List<BannerItem> mBannerItems;
    List<Help> mHelps;

    /**
     * @param bannerItems 推荐banner, 如果为null或者为空则没有banner
     * @param helps
     */
    public HelpAdapter(List<BannerItem> bannerItems, List<Help> helps) {
        mBannerItems = bannerItems;
        mHelps = helps;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && isBannerExist()) {
            return TYPE_BANNER;
        } else {
            return TYPE_HELP;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) {
            return new BannerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_help, parent, false));
        } else {
            return new HelperViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0 && holder instanceof BannerViewHolder) {
            ((BannerViewHolder)(holder)).bindData(mBannerItems);
        } else {
           //help
        }
    }

    private boolean isBannerExist() {
        return mBannerItems != null && !mBannerItems.isEmpty();
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (isBannerExist()) {
            count++;
        }
        if (mHelps != null && !mHelps.isEmpty()) {
            count = count + mHelps.size();
        }
        return count;
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.help_imageBanner)
        ImageBanner imageBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(List<BannerItem> bannerItems) {
            imageBanner.setSource(bannerItems).startScroll();
        }
    }

    static class HelperViewHolder extends RecyclerView.ViewHolder {
        public HelperViewHolder(View itemView) {
            super(itemView);
        }
    }
}
