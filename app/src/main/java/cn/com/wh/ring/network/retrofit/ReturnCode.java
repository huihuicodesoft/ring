package cn.com.wh.ring.network.retrofit;

/**
 * Created by Hui on 2017/7/18.
 */
public class ReturnCode {
    public static final int OK = 0;
    public static final int ERROR_TOKEN = -1;
    public static final int ERROR_PROGRAM = 1;
    public static final int ERROR_DATA_ACCESS = 2;
    public static final int ERROR_PERMISSION = 3;

    //控制层异常
    public static final int ERROR_FILE_STORAGE = 1001;
    public static final int ERROR_FILE_UPLOAD_EMPTY = 1002;
    public static final int ERROR_FILE_UPLOAD_MAX_SIZE = 1003;

    //服务层异常
    public static final int ERROR_POST_ILLEGAL_MEDIA_PHOTO_NUMBER_ = 2001;
    public static final int ERROR_POST_ILLEGAL_MEDIA_VIDEO_NUMBER_ = 2002;
    public static final int ERROR_POST_PRAISED_ = 2003;
    public static final int ERROR_POST_CRITICIZED = 2004;
    public static final int ERROR_PHONE = 2005;
    public static final int ERROR_SMS_CODE = 2006;
    public static final int ERROR_LOGIN = 2007;

}
