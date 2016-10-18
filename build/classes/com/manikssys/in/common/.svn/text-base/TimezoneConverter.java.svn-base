/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.common;

/**
 *
 * @author sandeep
 */

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimezoneConverter {

    private static Calendar calendar = Calendar.getInstance();

    /**
     * Converts a date from one TimeZone to another TimeZone.
     * @param date      date to convert, if null then throws {@code IllegalArgumentException}
     * @param zone      the destination TimeZone. If null then converts to UTC by default.
     * @return          converted date
     * @throws java.lang.IllegalArgumentException
     */
    public static Date convert(Date date, TimeZone zone) throws IllegalArgumentException {

        if (date == null) {
            throw new IllegalArgumentException("Date to convert must not be null.");
        }
        if (zone == null) {
            zone = TimeZone.getTimeZone("UTC"); // by default convert to UTC.
        }
        int offset = zone.getOffset(date.getTime());

        if (zone.equals(TimeZone.getDefault())) {
            calendar.setTime(date);
            calendar.add(Calendar.MILLISECOND, -offset);
            Date utc = calendar.getTime();
            return utc;
        }

        Date utc = convert(date, TimeZone.getDefault());

        calendar.setTime(new Date(utc.getTime()));
        calendar.add(Calendar.MILLISECOND, +offset);
        System.out.println("class -- " + calendar.getTime());
        return calendar.getTime();
    }
}
