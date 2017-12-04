package water.android.com.concrete;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by EdgeDi
 * 2017/9/6 14:19
 */

public class DataUtil {

    public static String getMonday(int day) {
        long day_time = (day - 1) * 86400000;
        long l = System.currentTimeMillis() + day_time;
        Date date = new Date(l);
        SimpleDateFormat format = new SimpleDateFormat("E");
        return format.format(date);
    }

    public static Long BTtime(String time) {
        int nian = Integer.parseInt(time.substring(0, 4));
        int yue = Integer.parseInt(time.substring(4, 6));
        int ri = Integer.parseInt(time.substring(6, 8));
        int shi = Integer.parseInt(time.substring(8, 10));
        int fen = Integer.parseInt(time.substring(10, time.length()));
        time = nian + "-" + yue + "-" + ri + " " + shi + ":" + fen;
        return get(time);
    }

    public static String getLineTime(String format) {
        String result = null;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = s.parse(format);
            s = new SimpleDateFormat("HH:mm");
            result = s.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getLineTime2(String format) {
        String result = null;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = s.parse(format);
            s = new SimpleDateFormat("MM-dd");
            result = s.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getModeDay(String format) {
        String result = null;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = s.parse(format);
            s = new SimpleDateFormat("MM月dd日");
            result = s.format(date);
            s = new SimpleDateFormat("EEEE");
            result += "  " + s.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long get(String format) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = s.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String getBeForeTime(String time) {
        long l = System.currentTimeMillis() - 2592000000l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        Date date = new Date(l);
        return format.format(date);
    }

    public static String getBeForeTime() {
        long l = System.currentTimeMillis() - 2592000000l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        Date date = new Date(l);
        return format.format(date);
    }

    public static String getBeForeDetail() {
        long l = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date(l);
        return format.format(date);
    }

    public static String getStrTime(String timeStamp) {
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    public static String getTime(String format) {
        String result = null;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = s.parse(format);
            result = getPublishTime(System.currentTimeMillis() - date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getPublishTime(long time) {
        time = time / 1000;
        if (time > 60) {
            time = time / 60;
            if (time > 60) {
                time = time / 24;
                if (time > 24) {
                    return time + "天前发布";
                } else {
                    return time + "小时前发布";
                }
            } else {
                return time + "分钟前发布";
            }
        } else {
            return time + "秒前发布";
        }
    }

    public static int getLastAfternoon() {
        long time = System.currentTimeMillis();
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        int apm = mCalendar.get(Calendar.AM_PM);
        return apm;
    }

}