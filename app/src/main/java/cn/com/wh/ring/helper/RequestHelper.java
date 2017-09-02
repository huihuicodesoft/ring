package cn.com.wh.ring.helper;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Hui on 2017/8/16.
 */

public class RequestHelper {
    public static MultipartBody filesToMultipartBody(List<String> list) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (String path : list) {
            File file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            builder.addFormDataPart("file", file.getName(), requestBody);
        }

        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

}
