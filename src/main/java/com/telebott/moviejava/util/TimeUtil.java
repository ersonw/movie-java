package com.telebott.moviejava.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    public static String getNowString(int v) {
        if (v > 13) {
            return null;
        }
        long time = System.currentTimeMillis();
        return timeToString(time, v);
    }

    public static String getNowString() {
        return getNowString(10);
    }

    public static String timeToString(long time, int v) {
        String[] sTime = new String[]{Long.toString(time)};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < v; i++) {
            builder.append(sTime[i]);
        }
        return builder.toString();
    }

    public static String timeToString(long time) {
        return timeToString(time, 10);
    }

    public static long strToTime(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(str);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static long strToDateTime(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(str);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static long sevenDaysLater() {
        return manyDaysLater(7);
    }

    public static long sevenDaysBefore() {
        return manyDaysBefore(7);
    }

    public static long manyDaysLater(int days) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + days);
        return calendar.getTimeInMillis();
    }

    public static long manyDaysBefore(int days) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - days);
        return calendar.getTimeInMillis();
    }
}
