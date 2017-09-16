package cn.com.wh.ring.ui.view.banner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.com.wh.banner.widget.Banner.BaseIndicatorBanner;
import cn.com.wh.ring.R;
import cn.com.wh.ring.network.response.BannerItem;


/**
 * Created by Hui on 2017/9/14.
 */


public class ImageBanner extends BaseIndicatorBanner<BannerItem, ImageBanner> {
    private ColorDrawable colorDrawable;
    private List<BannerItem> list;

    public ImageBanner(Context context) {
        this(context, null, 0);
    }

    public ImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        colorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    @Override
    public ImageBanner setSource(List<BannerItem> list) {
        this.list = list;
        return super.setSource(list);
    }

    @Override
    public void onTitleSlect(TextView tv, int position) {
        final BannerItem item = mDatas.get(position);
        tv.setText(item.getText());
    }

    @Override
    public View onCreateItemView(int position) {
        View view = View.inflate(mContext, R.layout.item_banner_image, null);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);

        final BannerItem item = mDatas.get(position);
        int itemWidth = mDisplayMetrics.widthPixels;
        int itemHeight = (int) (itemWidth * 360 * 1.0f / 640);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));

        String imgUrl = item.getImageUrl();

        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(mContext)
                    .load(imgUrl)
                    .override(itemWidth, itemHeight)
                    .centerCrop()
                    .placeholder(colorDrawable)
                    .into(iv);
        } else {
            iv.setImageDrawable(colorDrawable);
        }

        return view;
    }
}
