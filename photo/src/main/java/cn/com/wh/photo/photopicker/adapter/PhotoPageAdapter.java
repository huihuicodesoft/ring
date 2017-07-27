/**
 * Copyright 2016 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.wh.photo.photopicker.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.com.wh.photo.R;
import cn.com.wh.photo.photopicker.imageloader.Image;
import cn.com.wh.photo.photopicker.util.BGABrowserPhotoViewAttacher;
import cn.com.wh.photo.photopicker.util.PhotoPickerUtil;
import cn.com.wh.photo.photopicker.widget.PTImageView;
import cn.com.wh.photo.photoview.PhotoViewAttacher;

/**
 * 描述:大图预览适配器
 */
public class PhotoPageAdapter extends PagerAdapter {
    private ArrayList<String> mPreviewImages;
    private PhotoViewAttacher.OnViewTapListener mOnViewTapListener;
    private Activity mActivity;

    public PhotoPageAdapter(Activity activity, PhotoViewAttacher.OnViewTapListener onViewTapListener, ArrayList<String> previewImages) {
        mOnViewTapListener = onViewTapListener;
        mPreviewImages = previewImages;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return mPreviewImages == null ? 0 : mPreviewImages.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        final PTImageView imageView = new PTImageView(container.getContext());
        container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final BGABrowserPhotoViewAttacher photoViewAttacher = new BGABrowserPhotoViewAttacher(imageView);
        photoViewAttacher.setOnViewTapListener(mOnViewTapListener);
        imageView.setDelegate(new PTImageView.Delegate() {
            @Override
            public void onDrawableChanged(Drawable drawable) {
                if (drawable != null && drawable.getIntrinsicHeight() > drawable.getIntrinsicWidth() && drawable.getIntrinsicHeight() > PhotoPickerUtil.getScreenHeight()) {
                    photoViewAttacher.setIsSetTopCrop(true);
                    photoViewAttacher.setUpdateBaseMatrix();
                } else {
                    photoViewAttacher.update();
                }
            }
        });

        Image.display(imageView, R.mipmap.ic_holder_dark, mPreviewImages.get(position), PhotoPickerUtil.getScreenWidth(), PhotoPickerUtil.getScreenHeight());

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public String getItem(int position) {
        return mPreviewImages == null ? "" : mPreviewImages.get(position);
    }
}