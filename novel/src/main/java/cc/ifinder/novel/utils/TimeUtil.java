package cc.ifinder.novel.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {
    /**
     * 判断时间戳是否是今天
     *
     * @param time 时间戳
     * @return 是否今天
     */
    public static boolean isToday(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(time);

        return year == calendar.get(Calendar.YEAR)
                && month == calendar.get(Calendar.MONTH)
                && day == calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取时间戳所在当日的最后时间
     *
     * @param time
     * @return
     */
    public static Date getDayEnd(Long time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date zero = calendar.getTime();
        return zero;
    }
    /**
     * 获取时间戳所在当日的开始时间
     *
     * @param time
     * @return
     */
    public static Date getDayStart(Long time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+6"));
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }

    public static void main(String args[]) {
        Date date = getDayStart(1544863124000l);
        System.out.println(date.toString());
    }
}
