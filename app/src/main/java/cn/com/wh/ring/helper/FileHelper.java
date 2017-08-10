package cn.com.wh.ring.helper;

import android.text.TextUtils;

import java.io.File;
import java.util.List;

import cn.com.wh.ring.utils.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Hui on 2017/7/31.
 */

public class FileHelper {

    public static MultipartBody filesToMultipartBody(List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (File file : files) {
            String extension = FileUtils.getExtensionName(file.getName());
            if (!TextUtils.isEmpty(extension)) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + extension), file);
                builder.addFormDataPart("file", file.getName(), requestBody);
            }
        }

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }
}
