package jp.espla.gallery.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

    /**
     * 秒から日付文字列に変換する
     * @param seconds 秒
     * @param formatter フォーマット
     * @return 日付文字列
     */
    public static String convertSecIntoDateString(long seconds, SimpleDateFormat formatter) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(seconds * 1000);
        return formatter.format(calendar.getTime());
    }

    /**
     * ミリ秒から日付文字列に変換する
     * @param millis ミリ秒
     * @param formatter フォーマット
     * @return 日付文字列
     */
    public static String convertMillsIntoDateString(long millis, SimpleDateFormat formatter) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return formatter.format(calendar.getTime());
    }

}
