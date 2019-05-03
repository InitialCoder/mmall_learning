package com.mmall.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtil {
    //joda  time

    public static final String STANDAR_FORMAT="yyyy-MM-dd HH:mm:ss";

    //str--> date
    //date-->str

    public static Date strToDate(String str,String formatStr){
        DateTimeFormatter formatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = formatter.parseDateTime(str);
        return dateTime.toDate();
    }

    public static String DateToStr(Date date,String formatStr){
        if(null == date){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

}
