package com.spiderdata.modules.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Wangjs
 * @version 1.0
 * @date 2021/1/27 16:04
 */
public class DateUtil {
    public static String getNextDay(String curDate) {
        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date temp = dft.parse(curDate);
            Calendar cld = Calendar.getInstance();
            cld.setTime(temp);
            cld.add(Calendar.DATE, 1);
            temp = cld.getTime();
            curDate = dft.format(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return curDate;
    }
    public static int daysBetween(Date start,Date end) throws ParseException {
        Calendar cal = Calendar.getInstance();


        cal.setTime(start);

        long time1 = cal.getTimeInMillis();

        cal.setTime(end);

        long time2 = cal.getTimeInMillis();

        long diff =(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(diff));

    }

    public static int daysBetween(String start,String end) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();

        cal.setTime(sdf.parse(start));

        long time1 = cal.getTimeInMillis();

        cal.setTime(sdf.parse(end));

        long time2 = cal.getTimeInMillis();

        long diff=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(diff));

    }
    public static void main(String[] args) throws ParseException {
        // TODO Auto-generated method stub

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1=sdf.parse("2012-09-08 10:10:10");

        Date d2=sdf.parse("2012-09-15 00:00:00");

        System.out.println(daysBetween(d1,d2));

        System.out.println(daysBetween("2012-09-08","2012-09-15"));

    }

}
