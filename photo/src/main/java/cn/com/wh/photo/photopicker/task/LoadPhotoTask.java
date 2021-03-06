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
package cn.com.wh.photo.photopicker.task;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.wh.photo.R;
import cn.com.wh.photo.photopicker.cache.TakePhotoCache;
import cn.com.wh.photo.photopicker.model.ImageFolderModel;


/**
 * 描述: 加载图片
 */
public class LoadPhotoTask extends PTAsyncTask<Void, ArrayList<ImageFolderModel>> {
    private Context mContext;
    private boolean mTakePhotoEnabled;


    public LoadPhotoTask(Callback<ArrayList<ImageFolderModel>> callback, Context context, boolean takePhotoEnabled) {
        super(callback);
        mContext = context.getApplicationContext();
        mTakePhotoEnabled = takePhotoEnabled;
    }

    private static boolean isNotImageFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        return !file.exists() || file.length() == 0;
    }

    @Override
    protected ArrayList<ImageFolderModel> doInBackground(Void... voids) {
        ArrayList<ImageFolderModel> imageFolderModels = new ArrayList<>();

        ImageFolderModel allImageFolderModel = new ImageFolderModel(mTakePhotoEnabled);
        allImageFolderModel.name = mContext.getString(R.string.all_image);
        imageFolderModels.add(allImageFolderModel);

        HashMap<String, ImageFolderModel> imageFolderModelMap = new HashMap<>();

        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media.DATA},
                    "(" + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?) and "
                            + MediaStore.Images.Media.SIZE + ">?",
                    new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif", "0"},
                    MediaStore.Images.Media.DATE_ADDED + " DESC"
            );

            ImageFolderModel otherImageFolderModel;

            List<String> allPhotos = new ArrayList<>();
            List<String> dataPhotos = new ArrayList<>();

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    if (isNotImageFile(imagePath)) {
                        continue;
                    }

                    dataPhotos.add(imagePath);

                    if (TakePhotoCache.mCache.contains(imagePath)) {
                        TakePhotoCache.mCache.remove(imagePath);
                    }
                }
            }

            for (int i = TakePhotoCache.mCache.size() - 1; i >= 0; i--) {
                allPhotos.add(TakePhotoCache.mCache.get(i));
            }
            for (int j = 0; j < dataPhotos.size(); j++) {
                allPhotos.add(dataPhotos.get(j));
            }

            for (int m = 0; m < allPhotos.size(); m++) {
                String imagePath = allPhotos.get(m);
                if (m == 0) {
                    allImageFolderModel.coverPath = imagePath;
                }
                // 所有图片目录每次都添加
                allImageFolderModel.addLastImage(imagePath);

                String folderPath = null;
                // 其他图片目录
                File folder = new File(imagePath).getParentFile();
                if (folder != null) {
                    folderPath = folder.getAbsolutePath();
                }

                if (TextUtils.isEmpty(folderPath)) {
                    int end = imagePath.lastIndexOf(File.separator);
                    if (end != -1) {
                        folderPath = imagePath.substring(0, end);
                    }
                }

                if (!TextUtils.isEmpty(folderPath)) {
                    if (imageFolderModelMap.containsKey(folderPath)) {
                        otherImageFolderModel = imageFolderModelMap.get(folderPath);
                    } else {
                        String folderName = folderPath.substring(folderPath.lastIndexOf(File.separator) + 1);
                        if (TextUtils.isEmpty(folderName)) {
                            folderName = "/";
                        }
                        otherImageFolderModel = new ImageFolderModel(folderName, imagePath);
                        imageFolderModelMap.put(folderPath, otherImageFolderModel);
                    }
                    otherImageFolderModel.addLastImage(imagePath);
                }

                // 添加其他图片目录
                imageFolderModels.addAll(imageFolderModelMap.values());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return imageFolderModels;
    }

    public LoadPhotoTask perform() {
        if (Build.VERSION.SDK_INT >= 11) {
            executeOnExecutor(THREAD_POOL_EXECUTOR);
        } else {
            execute();
        }
        return this;
    }
}
