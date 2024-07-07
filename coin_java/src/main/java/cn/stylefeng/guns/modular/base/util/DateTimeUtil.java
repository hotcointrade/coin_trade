package cn.stylefeng.guns.modular.base.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil
{

    //joda-time

    //str->Date
    //Date->str
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //0点时间
    public static final String ZERO_FORMAT = "yyyy-MM-dd 00:00:00";


    public static Date strToDate(String dateTimeStr, String formatStr)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date, String formatStr)
    {
        if (date == null)
        {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    public static Date strToDate(String dateTimeStr)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date)
    {
        if (date == null)
        {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }

    /**
     * 增加时长
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date addTime(Date date, int hour)
    {
        //秒
        long time = date.getTime() / 1000 + hour * 3600;
        String str = dateToStr(new Date(time * 1000));
        return strToDate(str);
    }

    /**
     * 增加时长
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date addMinute(Date date, int minute)
    {
        //秒
        long time = date.getTime() / 1000 + minute * 60;
        String str = dateToStr(new Date(time * 1000));
        return strToDate(str);
    }

    /**
     * 计算时间
     *
     * @param startTime ： 开始时间
     * @param endTime   ： 结束时间
     * @return
     */
    public static int getDays(Date startTime, Date endTime)
    {
        Long l = 0L;
        long ts = startTime.getTime();
        long ts1 = endTime.getTime();

        l = (ts - ts1) / (1000 * 60 * 60 * 24);

        return l.intValue();
    }


    /**
     *
     * 获取当前时间离一天结束剩余秒数
     * @param currentDate 当前时间
     * @return 离一天结束剩余秒数
     */
    public static Integer getRemainSecondsOneDay(Date currentDate) {
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault());
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int) seconds;
    }
    /**
     * 获取指定时间的整分钟时间戳（秒、毫秒置为0）
     *
     * @param date 时间
     * @return 时间戳
     */
    public static long minuteTimeNotSec(Date date)
    {
        SimpleDateFormat min = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");

        //获取当前时间截止到整分钟时间戳（去除秒、毫秒精度）
        return DateTimeUtil.strToDate(min.format(date)).getTime();
    }


    /**
     * 日期相隔天数
     *
     * @param startDateInclusive
     * @param endDateExclusive
     * @return
     */
    public static int periodDays(LocalDate startDateInclusive, LocalDate endDateExclusive) {
        return Period.between(startDateInclusive, endDateExclusive).getDays();
    }

    /**
     * 日期转换 字符转 LocalDate
     * @param dateStr 日期字符
     * @param pattern 格式
     * @return LocalDate
     */
    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * LocalDate 转 Date
     * @param localDate
     * @return
     */
    public static Date localDateToDate(LocalDate localDate){

        //获取时间地区ID
        ZoneId zoneId = ZoneId.systemDefault();
        //转换为当地时间
        ChronoZonedDateTime<LocalDate> zonedDateTime = localDate.atStartOfDay(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     *  Date  转  LocalDate
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date){
        Instant instant = date.toInstant();

        ZoneId zoneId  = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * 获取当前日期0点时间
     * @param date 日期
     * @return 0点时间
     */
    public static Date getZeroDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }



}
