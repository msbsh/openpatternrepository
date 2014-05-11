/*
 * Copyright (c) 2005 - 2008 Aduna and Deutsches Forschungszentrum fuer Kuenstliche Intelligenz DFKI GmbH.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Provides utility methods for handling dates.
 */
public class DateUtil {

    private static DateFormat fullDateFormat = null;
    private static DateFormat notzDateFormat = null;
    private static DateFormat plainDateFormat = null;
    private static DatatypeFactory datatypeFactory = null;
    
    /**
     * Format the given date in a good dateTime format: ISO-8601, using the T separator and the - and :
     * seperators accordingly. Example: 2003-01-22T17:00:00+01:00.
     * 
     * @param date A date instance.
     * @return A String containing the date in ISO-8601 format.
     * @see #string2DateTime(String)
     */
    public static String dateTime2String(Date date) {
        long ts = date.getTime();
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTimeInMillis(ts);
        XMLGregorianCalendar cal = getDataTypeFactory().newXMLGregorianCalendar(gcal);
        cal.setMillisecond(0);
        return cal.toXMLFormat();
    }
    
    /**
     * Format the given date in a good date format: ISO-8601, using the - seperators accordingly. Example:
     * 2003-01-22
     * 
     * @param date A date instance.
     * @return A String containing the date in ISO-8601 format.
     * @see #string2Date(String)
     */
    public static String date2String(Date date) {
        return getPlainDateFormat().format(date);
    }

    /**
     * Parses the given string as a Date using the same date format as dateTime2String.
     * 
     * @param string A String in ISO-8601 format.
     * @return A Date instance with a timestamp obtained from the specified String.
     * @throws ParseException when the specified string did not conform to the ISO-8601 standard.
     * @see #dateTime2String(Date)
     */
    public static Date string2DateTime(String string) throws ParseException {
        Date res = null;
        try {
            XMLGregorianCalendar xcal = getDataTypeFactory().newXMLGregorianCalendar(string);
            xcal.setMillisecond(0);
            return xcal.toGregorianCalendar().getTime();
        } catch (IllegalArgumentException pe) {
            res=getISO8601DateFormat().parse(string);
        }
        return res; 
    }
    
    /**
     * Parses the given string as a Date using the same date format as date2String.
     * 
     * @param string A String in ISO-8601 date format.
     * @return A Date instance with the date obtained from the specified String.
     * @throws ParseException when the specified string did not conform to the ISO-8601 standard.
     * @see #date2String(Date)
     */
    public static Date string2Date(String string) throws ParseException {
        return getPlainDateFormat().parse(string);
    }

    /**
     * Converts the string in the ISO-8601 dateTime format to a string representing the same timestamp,
     * normalized to the UTC timezone.
     * 
     * @param string
     * @return a normalized version of the input date
     * @throws ParseException
     */
    public static String string2UTCString(String string) throws ParseException {
        Date date = string2DateTime(string);
        return dateTime2UTCString(date);
    }
    
    /**
     * Returns the given date formatted as an ISO 8601 dateTime string normalized to the UTC timezone.
     * 
     * @param date
     * @return a normalized version of the input date
     */
    public static String dateTime2UTCString(Date date) {
        Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.UK);
        utcCalendar.setTimeInMillis(date.getTime());
        return utcCalendar.get(Calendar.YEAR) + "-" + 
               zeroPad((utcCalendar.get(Calendar.MONTH)+1)) + "-" + 
               zeroPad(utcCalendar.get(Calendar.DAY_OF_MONTH)) + "T" + 
               zeroPad(utcCalendar.get(Calendar.HOUR_OF_DAY)) + ":" +
               zeroPad(utcCalendar.get(Calendar.MINUTE)) + ":" + 
               zeroPad(utcCalendar.get(Calendar.SECOND)) + "Z";
    }
    
    /**
     * Returns true if two dates are equal - denote the same time in milliseconds from the Epoch.
     * @param dateTimeString a string in valid full (date + time) ISO8601 format, in any timezone 
     * @param utcString a string in valid full (date + time) ISO8601 format in the UTC timezone
     * @return true if both strings denote equal dates
     * @throws ParseException if the dateString format is wrong
     */
    public static boolean dateTimeStringEqualToUTCString(String dateTimeString, String utcString) throws ParseException {
        return utcString.equals(DateUtil.string2UTCString(dateTimeString));
    }
    
    /**
     * Returns true if two dates are equal - denote the same time in milliseconds from the Epoch.
     * @param dateTime an instance of {@link java.util.Date} 
     * @param utcString a string in valid full (date + time) ISO8601 format in the UTC timezone
     * @return true if both strings denote equal dates
     * @throws ParseException if the dateString format is wrong
     */
    public static boolean dateTimeEqualToUTCString(Date dateTime, String utcString) {
        return utcString.equals(DateUtil.dateTime2UTCString(dateTime));
    }
    
    private static String zeroPad(long i) {
        if (i >= 0 && i <=9) {
            return "0" + i;
        } else {
            return String.valueOf(i);
        }
    }
    
    /**
     * Returns a statically shared DateFormat that uses the ISO-8601 format, which is used by
     * XSD-DATETIME. This method returns the format with the timezone suffix.
     * @return the DateFormat
     */
    public static DateFormat getISO8601DateFormat() {
        if (fullDateFormat == null) {
            fullDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        }
        return fullDateFormat;
    }
    
    /**
     * Returns a statically shared DateFormat that uses the ISO-8601 format, which is used by
     * XSD-DATETIME. This method returns the format without the timezone suffix.
     * @return the DateFormat
     */
    public static DateFormat getISO8601DateFormatNoTimezone() {
        if (notzDateFormat == null) {
            notzDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        }
        return notzDateFormat;
    }
    
    /**
     * Returns a statically shared DateFormat that uses the ISO-8601 format, which is used by
     * XSD-DATE
     * @return the DateFormat
     */
    public static DateFormat getPlainDateFormat() {
        if (plainDateFormat == null) {
            plainDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        return plainDateFormat;
    }
    
    private static DatatypeFactory getDataTypeFactory() {
        if (datatypeFactory == null) {
            try {
                datatypeFactory = DatatypeFactory.newInstance();
            }
            catch (DatatypeConfigurationException e) {
                // this will not happen
                e.printStackTrace();
            }
        }
        return datatypeFactory;
    }
}
