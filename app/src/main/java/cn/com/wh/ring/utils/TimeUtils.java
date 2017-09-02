package cn.com.wh.ring.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hui on 2017/8/18.
 */

public class TimeUtils {
    public static final String DEFAULT_TIME = "刚刚";
    private static Calendar calendarAgo = Calendar.getInstance();
    private static Calendar calendarCurrent = Calendar.getInstance();
    private static SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();

    /**
     * 根据时间戳和当前时间戳,得到格式化的时间字符串, 如: 刚刚, 2分钟前, 昨天, 4-1, 2012-12-23等.
     *
     * @param timestamp         时间戳
     * @param currentTimeMillis 当前时间戳
     * @return 代表时间的字符串
     */
    public static synchronized String getFormatTimeString(long timestamp, long currentTimeMillis) {

        if (currentTimeMillis < timestamp) {
            return DEFAULT_TIME;
        }

        calendarAgo.setTime(new Date(timestamp));
        calendarCurrent.setTime(new Date(currentTimeMillis));

        int yearAgo = calendarAgo.get(Calendar.YEAR);
        int yearCurrent = calendarCurrent.get(Calendar.YEAR);

        int monthAgo = calendarAgo.get(Calendar.MONTH) + 1;
        int monthCurrent = calendarCurrent.get(Calendar.MONTH) + 1;

        int dayAgo = calendarAgo.get(Calendar.DAY_OF_MONTH);
        int dayCurrent = calendarCurrent.get(Calendar.DAY_OF_MONTH);

        String result;
        if (yearAgo < yearCurrent) {
            dateFormat.applyPattern("yyyy-MM-dd");
            result = dateFormat.format(calendarAgo.getTime());
        } else if (monthAgo == monthCurrent && dayAgo == dayCurrent) {
            if (timestamp < currentTimeMillis) {
                long deltaMinutes = (currentTimeMillis - timestamp) / 60000;
                if (deltaMinutes < 1) {
                    result = DEFAULT_TIME;
                } else if (deltaMinutes < 60) {
                    result = String.format("%d分钟前", deltaMinutes);
                } else {
                    result = String.format("%d小时前", deltaMinutes / 60);
                }
            } else {
                result = DEFAULT_TIME;
            }
        } else {
            dateFormat.applyPattern("MM-dd");
            result = dateFormat.format(calendarAgo.getTime());
        }

        return result;
    }
}
