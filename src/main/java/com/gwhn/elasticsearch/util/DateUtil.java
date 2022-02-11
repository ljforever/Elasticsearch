package com.gwhn.elasticsearch.util;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String DATE_HOUR_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
    public static String DATE_CHINESE_HOUR_MINUTE_FORMAT = "yyyy-MM-dd HH：mm";

    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String DATE_FORMAT_CHINESE = "yyyy年M月d日";

    //region java8 Time
    /**
     * 将LocalDateTime 格式化输出string
     *
     * @return
     */
    public static String getTimeStr(LocalDateTime time, String format) {
        if (null == format|| format.length() == 0) {
            format = DateUtil.DATE_TIME_FORMAT;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return time.format(df);
    }
    //endregion

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        String dateStr = "";
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
        dateStr = df.format(new Date());
        return dateStr;
    }

    /**
     * 获取当前日期时间
     *
     * @return
     */
    public static String getCurrentDateTime() {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_TIME_FORMAT);
        datestr = df.format(new Date());
        return datestr;
    }

    /**
     * 获取当前日期时间
     *
     * @return
     */
    public static String getCurrentDateTime(String Dateformat) {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(Dateformat);
        datestr = df.format(new Date());
        return datestr;
    }

    public static String dateToDateTime(Date date) {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_TIME_FORMAT);
        datestr = df.format(date);
        return datestr;
    }

    /**
     *
     * @param datepart 日期差值类型
     *         (Calendar.DATE: 天数, Calendar.HOUR: 小时数, Calendar.MINUTE: 分钟数; Calendar.SECOND: 秒数　
     *         Calendar.MONTH: 月数, Calendar.YEAR: 年数
     * @param startdate　较之前的时间
     * @param enddate    较之后的时间
     * @return 差值
     * @throws Exception
     */
    public static long dateDiff(int datepart, Date startdate, Date enddate) throws Exception {
        long starttime = startdate.getTime();
        long endtime = enddate.getTime();

        if(Calendar.DATE == datepart) {
            return (endtime - starttime) / (1000 * 60 * 60 * 24); // 一天  1000 * 60 * 60 * 24 ms
        } else if(Calendar.MINUTE == datepart) {
            return (endtime - starttime) / (1000 * 60 * 60); // 一小时  1000 * 60 * 60  ms
        } else if(Calendar.HOUR == datepart) {
            return (endtime - starttime) / (1000 * 60); // 一分钟  1000 * 60  ms
        } else if(Calendar.SECOND == datepart) {
            return (endtime - starttime) / (1000); // 一秒钟  1000  ms
        }else{
            return endtime - starttime;
        }

        /*Calendar cs = Calendar.getInstance(), ce = Calendar.getInstance();
        cs.setTime(startdate); ce.setTime(enddate);

        if(Calendar.MONTH == datepart) {
            int ds = cs.get(Calendar.DAY_OF_MONTH), de = ce.get(Calendar.DAY_OF_MONTH);
            int dss = getMonthLastDay(startdate);
            //int des = getMonthLastDay(enddate);

            int result = ce.get(Calendar.MONTH) - cs.get(Calendar.MONTH);
            int myear = (ce.get(Calendar.YEAR) - cs.get(Calendar.YEAR))*12;
            double dday = 0.00;
            if(ds != de) { // 日期不相等时采用此比例差值（以较早日期所在月的天数为分母作差值比例)
                dday = (double)(de - ds)/dss;
            }
            return myear + result + dday;
        } else if(Calendar.YEAR == datepart) {
            int ds = cs.get(Calendar.DAY_OF_YEAR), de = ce.get(Calendar.DAY_OF_YEAR);
            int dss = isLeapYear(startdate) ? 366 : 365;
            //int des = isLeapYear(enddate) ? 366 : 365;
            int dyear = ce.get(Calendar.YEAR) - cs.get(Calendar.YEAR);

            double dday = 0.00;
            if(ds != de) {  // 日期不相等时采用此比例差值（以较早日期所在月的天数为分母作差值比例)
                dday = (double)(de -ds)/dss;
            }

            return dyear + dday;
        } else {
            throw new Exception("Unsupported Datepart");
        }*/
    }

    /**
     * 判断是否闰年
     * @param date 日期
     */
    public static boolean isLeapYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // 把日期设为12月31
        c.set(Calendar.MONTH, Calendar.DECEMBER);
        c.set(Calendar.DATE, 31);
        return (c.get(Calendar.DAY_OF_YEAR)==366);
    }

    public static int getMonthLastDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE , 1);  // 把日期设为当月第一天
        c.roll(Calendar.DATE, -1); // 日期回滚一天，也就是当月最后一天
        return c.get(Calendar.DATE);
    }

    /**
     * 将字符串日期转换为日期格式
     *
     * @param datestr
     * @return
     */
    public static Date stringToDate(String datestr) {

        if (datestr == null || datestr.equals("")) {
            return null;
        }
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_TIME_FORMAT);
        try {
            date = df.parse(datestr);
        } catch (ParseException e) {

        }
        return date;
    }

    /**
     * 将字符串日期转换为日期格式
     * 自定義格式
     *
     * @param datestr
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String datestr, String dateformat) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        try {
            date = df.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 将日期格式日期转换为字符串格式
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
        datestr = df.format(date);
        return datestr;
    }

    /**
     * 将日期格式日期转换为字符串格式 自定義格式
     *
     * @param date
     * @param dateformat
     * @return
     */
    public static String dateToString(Date date, String dateformat) {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        datestr = df.format(date);
        return datestr;
    }

    /**
     * 获取日期的DAY值
     *
     * @param date 输入日期
     * @return
     */
    public static int getDayOfDate(Date date) {
        int d = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        d = cd.get(Calendar.DAY_OF_MONTH);
        return d;
    }

    /**
     * 获取日期的MONTH值
     *
     * @param date 输入日期
     * @return
     */
    public static int getMonthOfDate(Date date) {
        int m = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        m = cd.get(Calendar.MONTH) + 1;
        return m;
    }

    /**
     * 获取日期的YEAR值
     *
     * @param date 输入日期
     * @return
     */
    public static int getYearOfDate(Date date) {
        int y = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        y = cd.get(Calendar.YEAR);
        return y;
    }

    /**
     * 获取星期几
     *
     * @param date 输入日期
     * @return
     */
    public static int getWeekOfDate(Date date) {
        int wd = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        wd = cd.get(Calendar.DAY_OF_WEEK) - 1;
        return wd;
    }

    /**
     * 获取输入日期的当月第一天
     *
     * @param date 输入日期
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.set(Calendar.DAY_OF_MONTH, 1);
        return cd.getTime();
    }

    /**
     * 获得输入日期的当月最后一天
     *
     * @param date
     */
    public static Date getLastDayOfMonth(Date date) {
        return DateUtil.addDay(DateUtil.getFirstDayOfMonth(DateUtil.addMonth(date, 1)), -1);
    }

    /**
     * 判断是否是闰年
     *
     * @param date 输入日期
     * @return 是true 否false
     */
    public static boolean isLeapYEAR(Date date) {

        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int year = cd.get(Calendar.YEAR);

        if (year % 4 == 0 && year % 100 != 0 | year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据整型数表示的年月日，生成日期类型格式
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return
     */
    public static Date getDateByYMD(int year, int month, int day) {
        Calendar cd = Calendar.getInstance();
        cd.set(year, month - 1, day);
        return cd.getTime();
    }

    /**
     * 获取年周期对应日
     *
     * @param date  输入日期
     * @param iyear 年数  負數表示之前
     * @return
     */
    public static Date getYearCycleOfDate(Date date, int iyear) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);

        cd.add(Calendar.YEAR, iyear);

        return cd.getTime();
    }

    /**
     * 获取月周期对应日
     *
     * @param date 输入日期
     * @param i
     * @return
     */
    public static Date getMonthCycleOfDate(Date date, int i) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);

        cd.add(Calendar.MONTH, i);

        return cd.getTime();
    }

    /**
     * 计算 fromDate 到 toDate 相差多少年
     *
     * @param fromDate
     * @param toDate
     * @return 年数
     */
    public static int getYearByMinusDate(Date fromDate, Date toDate) {
        Calendar df = Calendar.getInstance();
        df.setTime(fromDate);

        Calendar dt = Calendar.getInstance();
        dt.setTime(toDate);

        return dt.get(Calendar.YEAR) - df.get(Calendar.YEAR);
    }

    /**
     * 计算 fromDate 到 toDate 相差多少个月
     *
     * @param fromDate
     * @param toDate
     * @return 月数
     */
    public static int getMonthByMinusDate(Date fromDate, Date toDate) {
        Calendar df = Calendar.getInstance();
        df.setTime(fromDate);

        Calendar dt = Calendar.getInstance();
        dt.setTime(toDate);

        return dt.get(Calendar.YEAR) * 12 + dt.get(Calendar.MONTH) -
                (df.get(Calendar.YEAR) * 12 + df.get(Calendar.MONTH));
    }

    /**
     * 计算 fromDate 到 toDate 相差多少天
     *
     * @param fromDate
     * @param toDate
     * @return 天数
     */
    public static long getDayByMinusDate(Object fromDate, Object toDate) {

        Date f = DateUtil.chgObject(fromDate);

        Date t = DateUtil.chgObject(toDate);

        long fd = f.getTime();
        long td = t.getTime();

        return (td - fd) / (24L * 60L * 60L * 1000L);
    }

    /**
     * 计算 fromDate 到 toDate 相差多少毫秒
     *
     * @param fromDate
     * @param toDate
     * @return 天数
     */
    public static long getTimeByMinusDate(Object fromDate, Object toDate) {

        Date f = DateUtil.chgObject(fromDate);

        Date t = DateUtil.chgObject(toDate);

        long fd = f.getTime();
        long td = t.getTime();

        return (td - fd);
    }

    /**
     * 计算 fromDate 到 toDate 相差多少小时
     *
     * @param startDate
     * @param endDate
     * @return 小时
     */
    public static long getHourByMinusDate(Date startDate, Date endDate) {
        long nh = 1000 * 60 * 60;
        long diff = endDate.getTime() - startDate.getTime();
        long hour = diff / nh;
        return hour;
    }

    /**
     * 计算年龄
     *
     * @param birthday 生日日期
     * @param calcDate 要计算的日期点
     * @return
     */
    public static int calcAge(Date birthday, Date calcDate) {

        int cYear = DateUtil.getYearOfDate(calcDate);
        int cMonth = DateUtil.getMonthOfDate(calcDate);
        int cDay = DateUtil.getDayOfDate(calcDate);
        int bYear = DateUtil.getYearOfDate(birthday);
        int bMonth = DateUtil.getMonthOfDate(birthday);
        int bDay = DateUtil.getDayOfDate(birthday);

        if (cMonth > bMonth || (cMonth == bMonth && cDay > bDay)) {
            return cYear - bYear;
        } else {
            return cYear - 1 - bYear;
        }
    }

    /**
     * 从身份证中获取出生日期
     *
     * @param idno 身份证号码
     * @return
     */
    public static String getBirthDayFromIDCard(String idno) {
        Calendar cd = Calendar.getInstance();
        if (idno.length() == 15) {
            cd.set(Calendar.YEAR, Integer.valueOf("19" + idno.substring(6, 8))
                    .intValue());
            cd.set(Calendar.MONTH, Integer.valueOf(idno.substring(8, 10))
                    .intValue() - 1);
            cd.set(Calendar.DAY_OF_MONTH,
                    Integer.valueOf(idno.substring(10, 12)).intValue());
        } else if (idno.length() == 18) {
            cd.set(Calendar.YEAR, Integer.valueOf(idno.substring(6, 10))
                    .intValue());
            cd.set(Calendar.MONTH, Integer.valueOf(idno.substring(10, 12))
                    .intValue() - 1);
            cd.set(Calendar.DAY_OF_MONTH,
                    Integer.valueOf(idno.substring(12, 14)).intValue());
        }
        return DateUtil.dateToString(cd.getTime());
    }

    /**
     * 在输入日期上增加（+）或减去（-）天数
     *
     * @param date 输入日期
     * @param iday 要增加或减少的天数
     */
    public static Date addDay(Date date, int iday) {
        Calendar cd = Calendar.getInstance();

        cd.setTime(date);

        cd.add(Calendar.DAY_OF_MONTH, iday);

        return cd.getTime();
    }


    public static Date addSecond(Date date, int second){
        Calendar cd = Calendar.getInstance();

        cd.setTime(date);

        cd.add(Calendar.SECOND, second);

        return cd.getTime();
    }

    /**
     * 在输入日期上增加（+）或减去（-）月份
     *
     * @param date   输入日期
     * @param imonth 要增加或减少的月分数
     */
    public static Date addMonth(Date date, int imonth) {
        Calendar cd = Calendar.getInstance();

        cd.setTime(date);

        cd.add(Calendar.MONTH, imonth);

        return cd.getTime();
    }

    /**
     * 在输入日期上增加（+）或减去（-）年份
     *
     * @param date  输入日期
     * @param iyear 要增加或减少的年数
     */
    public static Date addYear(Date date, int iyear) {
        Calendar cd = Calendar.getInstance();

        cd.setTime(date);

        cd.add(Calendar.YEAR, iyear);

        return cd.getTime();
    }

    /**
     * 將OBJECT類型轉換為Date
     *
     * @param date
     * @return
     */
    public static Date chgObject(Object date) {

        if (date != null && date instanceof Date) {
            return (Date) date;
        }

        if (date != null && date instanceof String) {
            return DateUtil.stringToDate((String) date);
        }

        return null;

    }

    /**
     * 获得年龄
     * @param date
     * @return
     * @throws ParseException
     */
    public static long getAgeByBirthday(String date) throws ParseException {

        Date birthday = stringToDate(date, "yyyy-MM-dd");
        long sec = new Date().getTime() - birthday.getTime();

        long age = sec / (1000 * 60 * 60 * 24) / 365;

        return age;
    }

    /**
     * 比较日期
     * @param d1
     * @param d2
     * @return
     */
    public static int compareDate(Date d1, Date d2) {
        if (d1.getTime() > d2.getTime()) {
            return 1;
        } else if (d1.getTime() < d2.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }


}

