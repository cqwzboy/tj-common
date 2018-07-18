package com.tj.common.lang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 日期工具类
 * @author silongz
 *
 */
public class DateUtils {

    /**定义常量**/
    public static final String DEFAULT_PATERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认转换为 yyyy-MM-dd HH:mm:ss 
     * @param date 日期字符串
     * @return
     */
    public static Date parseDefault(String date) {
        return parse(date,DEFAULT_PATERN);
    }

    /**
     * 获取指定格式的Date对象，默认转换为 yyyy-MM-dd HH:mm:ss 
     * @param date 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date parse(String date, String pattern) {
        if(StringUtils.isBlank(pattern)){
            pattern = DEFAULT_PATERN ;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("日期【%s】解析成指定格式【%s】异常", date , pattern), e) ;
        }
    }

    /**
     * 获取指定格式的系统当前时间，默认返回yyyy-MM-dd HH:mm:ss格式
     * @param patern
     * @return
     */
    public static String getNowTime(String patern) {
        if(StringUtils.isBlank(patern)){
            patern = DEFAULT_PATERN ;
        }
        SimpleDateFormat df = new SimpleDateFormat(patern);
        return df.format(new Date());
    }

    /**
     *  获取dage对象的yyyy-MM-dd HH:mm:ss格式字符串 
     * @param date 日期字符串
     * @return
     */
    public static String formatDefault(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_PATERN);
        return df.format(date);
    }

    /**
     * 按指定格式获取date的字符串格式，默认转换为 yyyy-MM-dd HH:mm:ss 
     * @param date 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static String format(Date date, String pattern) {
        if(StringUtils.isBlank(pattern)){
            pattern = DEFAULT_PATERN ;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * 当前时间的前几个月
     * @param amount
     * @return
     */
    public static Date beforeNowByMounth(int amount){
        return afterNowByMounth(-amount) ;
    }

    /**
     * 当前时间的加减天数
     * @param days
     * @return
     */
    public static Date addDays(int days){
        return addDays(new Date(),days) ;
    }

    /**
     * 当前时间的后几个月
     * @param amount
     * @return
     */
    public static Date afterNowByMounth(int amount){
        Date now = new Date() ;
        Calendar cal = Calendar.getInstance() ;
        cal.setTime(now);
        cal.add(Calendar.MONTH, amount);
        return cal.getTime() ;
    }

    /**
     * 获取距当天时间days天数的0点，days为负，表示当天之前，正数表示当天之后
     * @return
     */
    public static Date getDateZero(int days){
        return getDateZero(new Date(), days) ;
    }
    /**
     * 获取距指定的date时间days天数的0点，days为负，表示当天之前，正数表示当天之后
     * @return
     */
    public static Date getDateZero(Date date, int days){
        if(date == null){
            date = new Date() ;
        }
        Calendar cal = Calendar.getInstance() ;
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime() ;
    }

    /**
     * 获取距当天时间days天数的23:59:59点，days为负，表示当天之前，正数表示当天之后
     * @return
     */
    public static Date getDateLastSecond(int days){
        return getDateLastSecond(new Date(), days) ;
    }

    /**
     * 获取距指定的时间days天数的23:59:59点，days为负，表示当天之前，正数表示当天之后
     * @return
     */
    public static Date getDateLastSecond(Date date, int days){
        if(date == null){
            date = new Date() ;
        }
        Calendar cal = Calendar.getInstance() ;
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime() ;
    }

    /**
     * 获取当前时间
     * @return 当前时间
     */
    public static Date currentTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.getTime();
    }
    /**
     * 获取两个日期的间隔秒数
     * @param date1 要比较的时间1
     * @param date2 要比较的时间2
     * @return 分钟间隔数，当前时间大于(晚于)传入时间为正，否则为0
     * added by wangzhe
     */
    public static long intervalSeconds(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        return Math.abs((date1.getTime() - date2.getTime()) / 1000);
    }
    /**
     * 指定日期与当前日期的间隔秒数
     * @param date 要比较的时间
     * @return 分钟间隔数，当前时间大于(晚于)传入时间为正，否则为0
     * added by wangzhe
     */
    public static long intervalSecondsDateSubNow(Date date) {
        if (date == null) {
            return 0;
        }
        Date now = currentTime();
        if (date.getTime() < now.getTime()) {
            return 0;
        }
        return (date.getTime() - now.getTime()) / 1000;
    }


    /**
     * 获取当前时间与传入时间的间隔秒数
     * @param date 要比较的时间
     * @return 分钟间隔数，当前时间大于(晚于)传入时间为正，否则为0
     */
    public static long intervalSeconds(Date date) {
        if (date == null) {
            return 0;
        }
        Date now = currentTime();
        if (now.getTime() < date.getTime()) {
            return 0;
        }
        return (now.getTime() - date.getTime()) / 1000;
    }

    /**
     * 获取两个date的间隔天数（忽略时分秒），始终返回>=0的数值
     * @param date1
     * @param date2
     * @return
     */
    public static long intervalDays(Date date1,Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("日期不能为空");
        }
        long time1 = getDateZero(date1,0).getTime() ;
        long time2 = getDateZero(date2,0).getTime() ;
        return Math.abs((time2 - time1)/(24 * 60 * 60 * 1000));
    }
    /**
     * 获取当前时间的整点
     * @return
     */
    public static Date getNowOclock(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) );
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 是否同一天
     * @param dt1
     * @param dt2
     * @return
     */
    public static boolean isSameDate(Date dt1, Date dt2){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dt1).equals(sdf.format(dt2));
    }

    /**
     * 计算同一年两个时间相差的天数
     * @param d1
     * @param d2
     * @return
     */
    public static Integer diffDays(Date d1,Date d2){
        if(d1 == null || d2 == null)return null;
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(d1);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(d2);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return Math.abs(day1 - day2);
    }

    /**
     * 获取当前小时0分0秒对应的date(比如当前时间是2018-04-08 14:58:46,那么调用该方法返回的结果就是2018-04-08 14:00:00)
     * @return
     */
    public static Date getCurrentHour(){
        Calendar cl = Calendar.getInstance() ;
        cl.set(Calendar.MINUTE, 0);
        cl.set(Calendar.SECOND, 0);
        cl.set(Calendar.MILLISECOND, 0);
        return cl.getTime() ;
    }

     /**
      * 给传入date加/减小时
      * @param date 传入时间
      * @param hours 加/减小时（正数加，负数减）
      * @return
      */
    public static Date addHours(Date date, int hours) {
        Calendar cal = Calendar.getInstance() ;
        cal.setTime(date);
        int currentHour = cal.get(Calendar.HOUR_OF_DAY) ;
        cal.set(Calendar.HOUR_OF_DAY, currentHour + hours);
        return cal.getTime() ;
    }

     /**
      * 给传入date加/减天
      * @param date 传入时间
      * @param days 加/减天（正数加，负数减）
      * @return
      */
     public static Date addDays(Date date, int days) {
         Calendar cal = Calendar.getInstance() ;
         cal.setTime(date);
         int currentDay = cal.get(Calendar.DAY_OF_MONTH) ;
         cal.set(Calendar.DAY_OF_MONTH, currentDay + days);
         return cal.getTime() ;
     }

     /**
      * 给传入date加/减分钟
      * @param date 传入时间
      * @param minutes 加/减分钟（正数加，负数减）
      * @return
      */
     public static Date addMinuts(Date date, int minutes) {
         Calendar cal = Calendar.getInstance() ;
         cal.setTime(date);
         int currentMinutes = cal.get(Calendar.MINUTE) ;
         cal.set(Calendar.MINUTE, currentMinutes + minutes);
         return cal.getTime() ;
     }

     /**
      * 给传入date加/减秒
      * @param date 传入时间
      * @param seconds 加/减分钟（正数加，负数减）
      * @return
      */
     public static Date addSeconds(Date date, int seconds) {
         Calendar cal = Calendar.getInstance() ;
         cal.setTime(date);
         int currentSeconds = cal.get(Calendar.SECOND) ;
         cal.set(Calendar.SECOND, currentSeconds + seconds);
         return cal.getTime() ;
     }

    /**
     * 获取传入date的小时数（24小时制）
     * @param date
     * @return
     */
     public static int getHour(Date date) {
         Calendar cl = Calendar.getInstance() ;
         cl.setTime(date);
         return cl.get(Calendar.HOUR_OF_DAY) ;
     }

}