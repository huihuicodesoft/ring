package cn.com.wh.ring.utils;

import android.text.TextUtils;

/**
 * Created by Hui on 2017/7/31.
 */

public class FileUtils {
    /**
     * a.png或path/a.png 获得 png
     * @param str
     * @return
     */
    public static String getExtensionName(String str) {
        String extension = null;
        if ((str != null) && (str.length() > 0)) {
            int dot = str.lastIndexOf('.');
            if ((dot > -1) && (dot < (str.length() - 1))) {
                extension = str.substring(dot + 1);
            }
        }
        return extension;
    }

    /**
     * a.png或path/a.png 获得 .png
     * @param str
     * @return
     */
    public static String getExtensionNamePoint(String str) {
        String extension = getExtensionName(str);
        if (!TextUtils.isEmpty(extension)) {
            extension = "." + extension;
        }
        return extension;
    }

    /**
     * path/a.png 获得 a
     * @param filePath
     * @return
     */
    public static String getFileNameNoEx(String filePath) {
        String fileName = null;
        if (!TextUtils.isEmpty(filePath)) {
            int var1 = filePath.lastIndexOf('/');
            int var2 = filePath.lastIndexOf('.');
            if (var1 < var2) {
                fileName = filePath.substring(var1 + 1, var2);
            } else {
                fileName = filePath.substring(var1 + 1);
            }
        }
        return fileName;
    }
}
