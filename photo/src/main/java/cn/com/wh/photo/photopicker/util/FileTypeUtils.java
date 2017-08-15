package cn.com.wh.photo.photopicker.util;

import android.text.TextUtils;

/**
 * Created by Hui on 2017/8/15.
 */

public class FileTypeUtils {
    /**
     * a.png或path/a.png 获得 png
     * @param path
     * @return
     */
    public static String getExtensionName(String path) {
        String extension = null;
        if ((path != null) && (path.length() > 0)) {
            int dot = path.lastIndexOf('.');
            if ((dot > -1) && (dot < (path.length() - 1))) {
                extension = path.substring(dot + 1);
            }
        }
        return extension;
    }

    public static boolean isPhoto(String path){
        boolean result = false;
        String extension = getExtensionName(path);
        if (!TextUtils.isEmpty(extension)){
            result = "png".equals(extension) || "jpg".equals(extension) || "jpeg".equals(extension);
        }
        return result;
    }

    public static boolean isGif(String path){
        boolean result = false;
        String extension = getExtensionName(path);
        if (!TextUtils.isEmpty(extension)){
            result = "gif".equals(extension);
        }
        return result;
    }
}
