/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2015/9/27 0027.
 */
public class DateUtil {
    /**
     * 日期(时间)转化为字符串.
     *
     * @param formater 日期或时间的格式.
     * @param date     java.util.Date类的实例.
     * @return 日期转化后的字符串.
     */
    public static String dateToString(Date date, String formater) {
        if (formater == null || "".equals(formater))
            return "";
        if (date == null)
            return "";
        return (new SimpleDateFormat(formater)).format(date);
    }


    public static Date stringToDate(String dateString, String format) {
        SimpleDateFormat srcDateFormat = new SimpleDateFormat(format);
        try {
            Date date = srcDateFormat.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 装换日期格式
     *
     * @param dateString
     * @param srcFmt
     * @param targetFmt
     * @return
     */

    public static String convert(String dateString, String srcFmt, String targetFmt) {
        SimpleDateFormat srcDateFormat = new SimpleDateFormat(srcFmt);
        SimpleDateFormat targetDateFormat = new SimpleDateFormat(targetFmt);
        try {
            Date date = srcDateFormat.parse(dateString);
            return targetDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws java.text.ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long between_days = 0;
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) between_days;
    }


    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 将日期时间字符串根据转换为指定时区的日期时间.
     *
     * @param srcFormat     待转化的日期时间的格式.
     * @param srcDateTime   待转化的日期时间.
     * @param dstFormat     目标的日期时间的格式.
     * @param dstTimeZoneId 目标的时区编号.
     * @return 转化后的日期时间.
     */
    public static String convertByTimezone(String srcDateTime, String srcFormat, String dstFormat, String srcTimeZoneId, String dstTimeZoneId) {
        if (srcFormat == null || "".equals(srcFormat))
            return null;
        if (srcDateTime == null || "".equals(srcDateTime))
            return null;
        if (dstFormat == null || "".equals(dstFormat))
            return null;
        if (dstTimeZoneId == null || "".equals(dstTimeZoneId))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(srcFormat);
        try {
            int diffTime =
                    TimeZone.getTimeZone(srcTimeZoneId).getRawOffset()
                            - TimeZone.getTimeZone(dstTimeZoneId).getRawOffset();
            Date d = sdf.parse(srcDateTime);
            long nowTime = d.getTime();
            long newNowTime = nowTime - diffTime;
            d = new Date(newNowTime);
            return dateToString(d, dstFormat);
        } catch (ParseException e) {
            //  Log.output(e.toString(), Log.STD_ERR);
            return null;
        } finally {
            sdf = null;
        }
    }
}
