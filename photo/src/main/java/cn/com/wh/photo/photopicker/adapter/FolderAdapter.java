package cn.com.wh.photo.photopicker.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import cn.com.wh.photo.R;
import cn.com.wh.photo.adapter.RecyclerViewAdapter;
import cn.com.wh.photo.adapter.ViewHolderHelper;
import cn.com.wh.photo.photopicker.imageloader.Image;
import cn.com.wh.photo.photopicker.model.ImageFolderModel;
import cn.com.wh.photo.photopicker.util.PhotoPickerUtil;

/**
 * Created by Hui on 2017/7/26.
 */

public class FolderAdapter extends RecyclerViewAdapter<ImageFolderModel> {
    private int mImageSize;

    public FolderAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_photo_folder);

        mData = new ArrayList<>();
        mImageSize = PhotoPickerUtil.getScreenWidth() / 10;
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, ImageFolderModel model) {
        helper.setText(R.id.tv_item_photo_folder_name, model.name);
        helper.setText(R.id.tv_item_photo_folder_count, String.valueOf(model.getCount()));
        Image.display(helper.getImageView(R.id.iv_item_photo_folder_photo), R.mipmap.ic_holder_light, model.coverPath, mImageSize);
    }
}
