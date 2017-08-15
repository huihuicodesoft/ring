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

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import cn.com.wh.photo.R;
import cn.com.wh.photo.adapter.RecyclerViewAdapter;
import cn.com.wh.photo.adapter.ViewHolderHelper;
import cn.com.wh.photo.photopicker.imageloader.Image;
import cn.com.wh.photo.photopicker.model.ImageFolderModel;
import cn.com.wh.photo.photopicker.util.FileTypeUtils;
import cn.com.wh.photo.photopicker.util.PhotoPickerUtils;

public class PhotoPickerAdapter extends RecyclerViewAdapter<String> {
    private ArrayList<String> mSelectedImages = new ArrayList<>();
    private int mImageSize;
    private boolean mTakePhotoEnabled;

    public PhotoPickerAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_photo_picker);
        mImageSize = PhotoPickerUtils.getScreenWidth() / 6;
    }

    @Override
    public int getItemViewType(int position) {
        if (mTakePhotoEnabled && position == 0) {
            return R.layout.item_photo_camera;
        } else {
            return R.layout.item_photo_picker;
        }
    }

    @Override
    public void setItemChildListener(ViewHolderHelper helper, int viewType) {
        if (viewType == R.layout.item_photo_camera) {
            helper.setItemChildClickListener(R.id.item_photo_camera_camera_iv);
        } else {
            helper.setItemChildClickListener(R.id.item_photo_picker_flag_iv);
            helper.setItemChildClickListener(R.id.item_photo_picker_photo_iv);
        }
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, String model) {
        if (getItemViewType(position) == R.layout.item_photo_picker) {
            if (FileTypeUtils.isGif(model)) {
                helper.setVisibility(R.id.item_photo_picker_photo_type_iv, View.VISIBLE);
                helper.setImageResource(R.id.item_photo_picker_photo_type_iv, R.mipmap.ic_delete);
            } else {
                helper.setVisibility(R.id.item_photo_picker_photo_type_iv, View.GONE);
            }
            Image.display(helper.getImageView(R.id.item_photo_picker_photo_iv), R.mipmap.ic_holder_dark, model, mImageSize);

            if (mSelectedImages.contains(model)) {
                helper.setImageResource(R.id.item_photo_picker_flag_iv, R.mipmap.ic_cb_checked);
                helper.getImageView(R.id.item_photo_picker_photo_iv).setColorFilter(helper.getConvertView().getResources().getColor(R.color.photo_selected_mask));
            } else {
                helper.setImageResource(R.id.item_photo_picker_flag_iv, R.mipmap.ic_cb_normal);
                helper.getImageView(R.id.item_photo_picker_photo_iv).setColorFilter(null);
            }
        }
    }

    public void setSelectedImages(ArrayList<String> selectedImages) {
        if (selectedImages != null) {
            mSelectedImages = selectedImages;
        }
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedImages() {
        return mSelectedImages;
    }

    public int getSelectedCount() {
        return mSelectedImages.size();
    }

    public void setImageFolderModel(ImageFolderModel imageFolderModel) {
        mTakePhotoEnabled = imageFolderModel.isTakePhotoEnabled();
        setData(imageFolderModel.getImages());
    }
}
