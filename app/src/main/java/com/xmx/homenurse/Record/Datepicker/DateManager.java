package com.xmx.homenurse.Record.Datepicker;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by The_onE on 2016/3/22.
 */
public class DateManager {
    public static String makeString(int year, int month, int day) {
        return "" + year + "-" + month + "-" + day;
    }

    public static String makeString(Date date) {
        return "" + (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
    }

    public static int[] getDate(String date) {
        String regex = "(.+?)-(\\d+)-(\\d+)"; //格式为yyyy-M-d
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);
        int year = 1900;
        int month = 1;
        int day = 1;
        if (matcher.find()) {
            year = Integer.valueOf(matcher.group(1));
            month = Integer.valueOf(matcher.group(2));
            day = Integer.valueOf(matcher.group(3));
        }
        return new int[]{year, month, day};
    }
}
