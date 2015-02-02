/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.spring.samples.mvp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Server-side utility methods for date/time handling.
 */
public class SSTimeUtil {

    static {
        marketTimeZoneAsString = System.getProperty("marketTimeZone") == null
                ? "America/New_York" : System.getProperty("marketTimeZone");
    }
    /**
     * Hour labels for a 24 hour day, regardless of Daylight Savings or Standard
     * Time time zone offset. Hour 1 is 1:00AM, Hour 24 is 12:00AM of the
     * following day.
     */
    private static String[] normalDayLabels = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09",
        "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};

    /**
     * Hour labels for 23 hour day, where day is a "transition day" from
     * Standard Time to Daylight Savings. Hour 1 is 1:00AM, Hour 23 is 12:00AM
     * of the following day. 2:00AM is skipped.
     */
    private static String[] shortDayLabels = new String[]{"01", "03", "04", "05", "06", "07", "08", "09", "10", "11",
        "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};

    /**
     * Hour labels for 25 hour day, where day is a "transition day" from
     * Daylight Savings to Standard Time. Hour 1 is 1:00AM, Hour 25 is 12:00AM
     * of the following day. 2:00AM is repeated.
     */
    private static String[] longDayLabels = new String[]{"01", "02", "02*", "03", "04", "05", "06", "07", "08", "09",
        "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};

    /**
     * Minute interval labels in increments of 5. First interval starts at 5th
     * minute of hour. Last interval starts at 0th minute of hour of following
     * day.
     */
    private static String[] intervalLabels = new String[] {"05","10","15","20","25","30","35","40","45","50","55","60"};

    /**
     * Valid minute intervals in increments of 5.
     */
    private static HashSet<Integer> valid05MinuteIntervals = new HashSet<Integer>(Arrays.asList(new Integer[]{0,05,10,15,20,25,30,35,40,45,50,55,60}));

    /**
     * Valid minute interval in increments of 15.
     */
    private static HashSet<Integer> valid15MinuteIntervals = new HashSet<Integer>(Arrays.asList(new Integer[]{0,15,30,45,60}));

    /**
     * Valid minute interval in increments of 60.
     */
    private static HashSet<Integer> valid60MinuteIntervals = new HashSet<Integer>(Arrays.asList(new Integer[]{0,60}));


    /**
     * Specify Market time zone as locale-dependent String (e.g. America/Chicago or America/New_York)
     */
    private static String marketTimeZoneAsString;

    /**
     * A DateTimeZone, using Market time zone, for use with DateTimeFormatter
     * instances
     */
    private static DateTimeZone marketTimeZone = DateTimeZone.forID(marketTimeZoneAsString);

    /**
     * The offset in hours between daylight savings and standard time used for the timezone.
     */
    private static int OFFSET_STANDARD_TO_DST = 1;

    /**
     * The standard offset used for the timezone.
     */
    private static String STANDARD_OFFSET = "-0"+String.valueOf(-marketTimeZone.getStandardOffset(0)/(1000*60*60))+":00";

    /**
     * The offset used during daylight savings time.
     */
    private static String DST_OFFSET = "-0"+String.valueOf(-marketTimeZone.getStandardOffset(0)/(1000*60*60)-OFFSET_STANDARD_TO_DST)+":00";

    /**
     * Array of valid offsets
     */
    private static String[] validMarketTimeZoneOffsets = { DST_OFFSET, STANDARD_OFFSET };

    /**
     * Formats a DateTime instance as a hour (0-23), returns a String value.
     */
    private static DateTimeFormatter hourFormat = DateTimeFormat.forPattern("HH");

    /**
     * Formats a DateTime instance as a minute (0-59), returns a String value.
     */
    private static DateTimeFormatter minuteFormat = DateTimeFormat.forPattern("mm");

    /**
     * Formats a DateTime instance as a time zone (e.g., -05:00), returns a
     * String value.
     */
    private static DateTimeFormatter timeZoneOffsetFormat = DateTimeFormat.forPattern("ZZ");

    /**
     * Formats a DateTime instance or parses an ISO8601 no-millis String.
     */
    private static DateTimeFormatter isoFormat = ISODateTimeFormat.dateTimeNoMillis();

    /**
     * Parses a DateTime instance or parses an ISO8601 no-millis String.
     */

    private static DateTimeFormatter isoDateParser = ISODateTimeFormat.dateParser().withZone(getMarketTimeZone());


    /**
     * Parses a String date in yyyy-MM-dd HH:mm:ss format. To be used
     * exclusively for parsing Strings retrieved from a database column!
     */
    private static DateTimeFormatter dbDateParser = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(
            marketTimeZone);

    /**
     * Strictly for use with isoToDateTime(String) method implementation below!
     */
    private static DateTimeFormatter mktFormat = ISODateTimeFormat.dateTimeParser().withZone(marketTimeZone);

    /**
     * Formats a DateTime instance as dash separated year, month and day.
     * Typically used for daily display. For display only! Never use for parsing!
     */
    private static DateTimeFormatter ui_ymdFormat = DateTimeFormat.forPattern("YYYY-MM-dd");

    /**
     * Formats a DateTime instance as a dash separated year, month, day plus a
     * colon separated hour and minute.  For
     * display only! Never use for parsing!
     */
    private static DateTimeFormatter ui_ymdhmFormat = DateTimeFormat
            .forPattern("YYYY-MM-dd HH:mm");

    /**
     * Formats a DateTime instance as a dash separated year, month, day plus a
     * colon separated hour, minute and second.  For display only! Never use for parsing!
     */
    private static DateTimeFormatter ui_ymdhmsFormat = DateTimeFormat
            .forPattern("YYYY-MM-dd HH:mm:ss");

    /**
     * Formats a DateTime instance as a colon separated hour, minute, and
     * second. Typically used for interval display. For display only! Never use
     * for parsing!
     */
    private static DateTimeFormatter ui_hmsFormat = DateTimeFormat.forPattern("HH:mm:ss");


    /**
     * GMT Calendar instance. Used in database operations.
     */
    private static Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

    /**
     * Market Calendar instance. Again used in database operations.
     */
    private static Calendar mktCal = Calendar.getInstance(TimeZone.getTimeZone(marketTimeZoneAsString));

    /**
     * Timezone used for the market.
     */
    public static DateTimeZone getMarketTimeZone() {
        return marketTimeZone;
    }

    public static String[] getValidMarketTimeZoneOffsets() {
        return validMarketTimeZoneOffsets;
    }

    /**
     * Useful to set the Calendar to GMT when using
     * PreparedStatement#setDate(int, java.sql.Date, Calendar)
     *
     * @return a Calendar instance in GMT time
     */
    public static Calendar getGmtCalendar() {
        return gmtCal;
    }

    /**
     * Useful to set the Calendar to Market's timezone when using
     * java.sql.Timestamp#getTimestamp(int, Calendar)
     *
     * @return a Calendar instance in Market time
     */
    public static Calendar getMarketCalendar() {
        return mktCal;
    }

    /**
     * Get Market start time for current day (i.e., today)
     *
     * @return a java.util.Date instance representing the current day at
     *         midnight
     */
    public static Date getMarketStartDate() {
        return getMarketStartDateTime().toDate();
    }

    /**
     * Get Market start time for current day (i.e., today)
     *
     * @return an org.joda.time.DateTime instance representing the current day
     *         at midnight
     */
    public static DateTime getMarketStartDateTime() {
        // Current market date based on now
        DateTime dt = new DateTime();
        dt = dt.withZone(marketTimeZone);
        dt = dt.withMillisOfDay(0); // Set to start of day
        return dt;
    }

    /**
     * Calculates the # of hours in a given day (where day is an
     * org.joda.time.DateTime)
     *
     * @param dt
     *            a DateTime instance
     * @return the number of hours in a day respecting time zones that honor
     *         Daylight Savings (e.g., calculation takes into account transition
     *         days)
     */
    public static int hoursInDay(final DateTime dt) {
        final DateTime dt0 = dt.withMillisOfDay(0); // Set to start of day
        final DateTime dt1 = dt0.plusDays(1); // Set to end of day
        final Hours hours = Hours.hoursBetween(dt0, dt1);
        return hours.getHours();
    }

    /**
     * Generates hour labels for a given day (where day is an
     * org.joda.time.DateTime)
     *
     * @param dt
     *            a DateTime instance
     * @return hour labels where first hour label represents 1:00AM of day and
     *         last hour label represents 12:00AM of the following day
     */
    //TODO - Change use of this function to hoursInDay and isoHoursForDay, then convert DateTime
    // to labels for presentation
    public static String[] labelsForDay(final DateTime dt) {
        final int hoursInDay = hoursInDay(dt);

        if (hoursInDay == 24) {
            return normalDayLabels;
        } else if (hoursInDay == 23) {
            return shortDayLabels;
        } else if (hoursInDay == 25) {
            return longDayLabels;
        } else {
            return null;
        }
    }

    public static List<String> isoHoursForDay(final DateTime date) {
        final int hoursInDay = hoursInDay(date);
        List<String> hours = new ArrayList<String>();
        for (int i = 0; i < hoursInDay; i++) {
            String hour = dateTimeToIsoNoMillis(date.plusHours(i+1));
            hours.add(hour);
        }
        return hours;
    }

    public static List<String> isoIntervalsForHour(final DateTime date) {
        final int intervalsInHour = 12;
        List<String> intervals = new ArrayList<String>();
        for (int i = 0; i < intervalsInHour; i++) {
            String interval = dateTimeToIsoNoMillis(date.plusMinutes((i+1)*5));
            intervals.add(interval);
        }
        return intervals;
    }


    /**
     * Convenience method. Delegates to dateTimeToHourLabel(DateTime) after
     * converting ISO8601 formatted String (no millis).
     *
     * @param isoDateTime
     *            an ISO8601 formatted String (no millis)
     * @return an hour label
     */
    public static String isoToHourLabel(final String isoDateTime) {
        final DateTime dateTime = SSTimeUtil.isoToDateTime(isoDateTime);
        return SSTimeUtil.dateTimeToHourLabel(dateTime);
    }


    /**
     * Convenience method. Delegates to getHourOfDay(DateTime) after
     * converting ISO8601 formatted String (no millis). Assumes isoHour
     * is hour ending format.
     *
     * @param isoDateTime
     *            an ISO8601 formatted String (no millis)
     * @return an hour integer
     */
    public static int isoToHour(final String isoHour) {
        int hour = SSTimeUtil.isoToDateTime(isoHour).getHourOfDay();
        return (hour == 0) ? 24 : hour;
    }

    /**
     * Convenience method. Delegates to dateTimeToDisplayableDay(DateTime) after
     * converting ISO8601 formatted String (no millis).
     *
     * @param isoDateTime
     *            an ISO8601 formatted String (no millis)
     * @return an hour label
     */
    public static String isoToDisplayableDay(final String isoDateTime) {
        final DateTime dateTime = SSTimeUtil.isoToDateTime(isoDateTime);
        return SSTimeUtil.dateTimeToDisplayableDay(dateTime);
    }

    /**
     * Determines the hour label corresponding to a org.joda.time.DateTime
     *
     * @param dateTime
     *            org.joda.time.DateTime (typically a whole hour)
     * @return an hour label where 01 represents 1:00AM and 24 represents
     *         12:00AM of the following day
     */
    public static String dateTimeToHourLabel(final DateTime dateTime) {
        String hour = null;
        if (dateTime != null) {
            hour = hourFormat.print(dateTime.withZone(marketTimeZone));
            final String tz = timeZoneOffsetFormat.print(dateTime);
            if (hour != null) {
                // midnight is the 24th hour!
                if (hour.equals("00")) {
                    hour = "24";
                }
                if (hour.equals("01")) {
                    final DateTime priorHourAsDate = dateTime.minusHours(1);
                    final String priorTz = timeZoneOffsetFormat.print(priorHourAsDate);
                    if (!tz.equals(priorTz)) {
                        hour = "02";
                    }
                }
                if (isExtraHourForHourEnding(dateTime)) {
                    hour = "02*";
                }
            }
        }
        return hour;
    }

    /**
     * Determines whether current hour is an "extra hour" when transitioning
     * from Daylight Savings to Standard Time for a DateTime that represents
     * hour ending data.
     *
     * @param dateTime
     *            an org.joda.time.DateTime instance
     * @return true if the date is a "transition day"; false otherwise
     */
    public static boolean isExtraHourForHourEnding(final DateTime dateTime) {
        boolean result = false;
        final DateTime twoHoursBefore = dateTime.minusHours(2);
        final DateTime oneHourBefore = dateTime.minusHours(1);
        final DateTime currentDate = dateTime;

        if (!marketTimeZone.isStandardOffset(twoHoursBefore.getMillis())
                && marketTimeZone.isStandardOffset(oneHourBefore.getMillis())
                && marketTimeZone.isStandardOffset(currentDate.getMillis())) {
            result = true;
        }

        return result;
    }

    /**
     * Determines whether current hour is an "extra hour" when transitioning
     * from Daylight Savings to Standard Time for a DateTime that represents
     * hour ending data.
     *
     * @param dateTime
     *            an org.joda.time.DateTime instance
     * @return true if the date is a "transition day"; false otherwise
     */
    public static boolean isExtraHourForHourEnding(final String isoDateTime) {
        DateTime dateTime = SSTimeUtil.isoToDateTime(isoDateTime);
        boolean result = false;
        final DateTime twoHoursBefore = dateTime.minusHours(2);
        final DateTime oneHourBefore = dateTime.minusHours(1);
        final DateTime currentDate = dateTime;

        if (!marketTimeZone.isStandardOffset(twoHoursBefore.getMillis())
                && marketTimeZone.isStandardOffset(oneHourBefore.getMillis())
                && marketTimeZone.isStandardOffset(currentDate.getMillis())) {
            result = true;
        }

        return result;
    }

    public static boolean isExtraHour(final DateTime dateTime) {
        boolean result = false;
        final DateTime oneHourBefore = dateTime.minusHours(1);
        final DateTime currentDate = dateTime;

        if (!marketTimeZone.isStandardOffset(oneHourBefore.getMillis())
                && marketTimeZone.isStandardOffset(currentDate.getMillis())) {
            result = true;
        }
        return result;
    }


    /**
     * Converts a java.util.Date in any time zone to a joda.time.DateTime
     * instance in Market time zone.
     *
     * @param date
     *            a Date instance
     * @return a DateTime instance in "Market time"
     */
    public static DateTime dateToDateTime(final Date date) {
        return new DateTime(date).withZone(marketTimeZone);
    }


    /**
     * Converts a a java.util.Date in any time zone to an ISO8601 no-millis
     * String instance in Market time zone
     *
     * @param date
     *            a Date instance
     * @return an ISO8601 no-millis String instance in "Market time"
     */
    public static String dateToIsoNoMillis(final Date date) {
        DateTime dt = new DateTime(date);
        dt = dt.withZone(marketTimeZone);
        return dateTimeToIsoNoMillis(dt);
    }

    /**
     * Converts an org.joda.time.DateTime in any time zone to an ISO8601
     * no-millis String instance respecting time zone of original DateTime
     * instance.
     *
     * @param dateTime
     *            a DateTime instance
     * @return an ISO8601 no-millis String instance
     */
    public static String dateTimeToIsoNoMillis(final DateTime dateTime) {
        // Assumes the caller has set the preferred time zone
        return dateTime == null ? null : isoFormat.print(dateTime);
    }

    /**
     * Strips an org.joda.time.DateTime of its minutes, seconds, and millis
     * respecting time zone of original DateTime instance.
     *
     * @param dateTime
     *            a DateTime instance
     * @return DateTime with only date and hour.
     */
    public static DateTime dateTimeToDateHour(final DateTime dateTime) {
        // Assumes the caller has set the preferred time zone
        return dateTime.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
    }

    /**
     * Converts ISO 8601 date/time String (e.g., 2012-01-01T03:00:00-08:00) into
     * an org.joda.time.DateTime taking into account Market time zone. Capable
     * of parsing a date, a time or both.
     *
     * @param iso
     *            an ISO8601 String (with or without millis)
     * @return a DateTime instance in "Market time"
     */
    public static DateTime isoToDateTime(final String iso) {
        return StringUtils.isEmpty(iso) ? null : mktFormat.parseDateTime(iso);
    }

    /**
     * Return the starting day accounting for Hour 00:00:00 being the day after.
     *
     * @param isoHour
     *              an iso String
     * @return an iso String
     */
    public static String isoHourEndingToIsoDay(String isoHour) {
        DateTime dt = isoToDateTime(isoHour);
        dt = dt.withZone(SSTimeUtil.getMarketTimeZone());
        if (dt.getHourOfDay() == 0) {
            dt = dt.minusDays(1);
        }
        return dateTimeToIsoDay(dt);
    }

    /**
     * Accepts any java.util.Date, converts to Market time zone resets hour
     * minutes and seconds to midnight,
     *
     * @param date
     *            a Date instance
     * @return an ISO8601 formatted String without millis
     */
    public static String dateToIsoDay(final Date date) {
        final DateTime dt = dateToMarketDateTime(date).withMillisOfDay(0);
        return isoFormat.print(dt);
    }

    /**
     * Accepts any org.joda.time.DateTime, converts to Market time zone resets
     * hour minutes and seconds to midnight,
     *
     * @param dateTime
     *            a Date instance
     * @return an ISO8601 formatted String without millis
     */
    public static String dateTimeToIsoDay(final DateTime dateTime) {
        final DateTime dt = dateTime.withZone(marketTimeZone).withMillisOfDay(0);
        return isoFormat.print(dt);
    }

    /**
     * @param dateTime
     *            a DateTime instance
     * @return a colon separated hour, minute, second String (e.g., 11:00:00).
     */
    public static String dateTimeToDisplayableTime(final DateTime dateTime) {
        String formattedTime = ui_hmsFormat.print(dateTime);
        if (isExtraHour(dateTime)) {
            formattedTime +="*";
        }
        return formattedTime;
    }

    /**
     * @param dateTime
     *            a DateTime instance
     * @return the concatenation of a dash separated year, month, day plus a
     *         space plus a colon separated hour, minute String (e.g.,
     *         2012-10-09 07:00).
     */
    public static String dateTimeToDisplayableDateTime(final DateTime dateTime) {
        String formattedDateTime = ui_ymdhmFormat.print(dateTime);
        if (isExtraHour(dateTime)) {
            formattedDateTime +="*";
        }
        return formattedDateTime;
    }

    /**
     * @param dateTime
     *            a DateTime instance
     * @return the concatenation of a dash separated year, month, day plus a
     *         space plus a colon separated hour, minute String (e.g.,
     *         2012-10-09 07:00:00).
     */
    public static String dateTimeToDisplayableDateTimeWithSeconds(final DateTime dateTime) {
        if(dateTime == null) {
            return null;
        }

        String formattedDateTime = ui_ymdhmsFormat.print(dateTime);
        if (isExtraHour(dateTime)) {
            formattedDateTime +="*";
        }
        return formattedDateTime;
    }

    /**
     * Strips out the year, month, and day from an org.joda.time.DateTime
     * instance. For explicit use in UI!
     *
     * @param dateTime
     *            a DateTime instance
     * @return a dash separated year, month, and day String (e.g., 2012-06-03).
     */
    public static String dateTimeToDisplayableDay(final DateTime dateTime) {
        return ui_ymdFormat.print(dateTime);
    }

    /**
     * Strips out the year, month, and day from an org.joda.time.DateTime
     * instance. For explicit use in UI!
     *
     * @param dateTime
     *            a DateTime instance
     * @return a dash separated year, month, and day String (e.g., 2012-06-03).
     */
    public static String dateToDisplayableDay(final Date date) {
        return dateTimeToDisplayableDay(dateToDateTime(date));
    }

    /**
     * Converts an ISO 8601 date/time String (e.g., 2012-01-01T03:00:00-05:00)
     * into a java.util.Date
     *
     * @param iso
     *            an ISO8601 formatted String (no millis)
     * @return a Date instance in "Market time"
     */
    public static Date isoToDate(final String iso) {
        // Parses any ISO 8601 formatted String into a java.util.Date
        return isoToDateTime(iso).toDate();
    }

    /**
     * Converts calendar day at midnight in yyyy-mm-dd format to an
     * org.joda.time.DateTime Format employs Market time zone.
     *
     * @param day
     *            a yyyy-mm-dd formatted String,
     * @param hour
     *               hour in day (1-24)
     * @param extra
     *               is extra or duplicate hour
     *
     * @return a DateTime instance representing day, hour and extra
     */
    public static DateTime dayHourExtraToDateTime(final String day, int hour, boolean extra) {
        DateTime result = isoDateParser.parseDateTime(day);
        result = result.plusHours(hour);
        if (extra) {
            result = result.plusHours(1);
        }
        return result;
    }

    /**
     * Converts calendar day at midnight in yyyy-mm-dd format to an
     * ISO time with millis.
     *
     * @param day
     *            a yyyy-mm-dd formatted String,
     * @param hour
     *               hour in day (1-24)
     * @param extra
     *               is extra or duplicate hour
     *
     * @return an ISO8601 formatted String without millis
     */
    public static String dayHourExtraToIso(final String day, int hour, boolean extra) {
        return dateTimeToIsoNoMillis(dayHourExtraToDateTime(day, hour, extra));
    }

    /**
     * Converts calendar day at midnight in ISO no millis format to an
     * org.joda.time.DateTime Format employs Market time zone.
     *
     * @param day
     *            an ISO8601 formatted String (non millis)
     * @return a DateTime instance at midnight in "Market time"
     */
    public static DateTime isoDayToDateTime(final String day) {
        final DateTimeFormatter fmt = isoFormat.withZone(marketTimeZone);
        final DateTime parsed = fmt.parseDateTime(day);
        // set to start of day
        final DateTime result = parsed.withMillisOfDay(0);
        return result;
    }

    /**
     * Parse a day in yyyy-MM-dd HH:mm:ss format (typically from a database
     * column)
     *
     * @param dayInYMDZZFormat
     *            a String formatted date from a database column
     * @return an oprg.joda.time.DateTime instance in "Market time"
     */
    public static DateTime dayToDateTime(final String dayInYMDZZFormat) {
        return StringUtils.isEmpty(dayInYMDZZFormat) ? null : dbDateParser.parseDateTime(dayInYMDZZFormat);
    }

    /**
     * Parse a day in yyyy-MM-dd format (typically from a database
     * column) into an iso day with hour and minutes reset to midnight.
     *
     * @param dayInYMDZZFormat
     *            a String formatted date from a database column
     * @return an ISO8601 formatted String without millis
     */
    public static String dayToIso(final String dayInYMDFormat) {
        if(StringUtils.isEmpty(dayInYMDFormat)) {
            return null;
        }
        DateTime dt =  dbDateParser .parseDateTime(dayInYMDFormat + " 00:00:00");
        return dateTimeToIsoDay(dt);
    }

    /**
     * Parse a day in  yyyy-MM-dd HH:mm:ssformat (typically from a database
     * column) into an iso.
     *
     * @param dayInYMDHMSFormat
     *            a String formatted date from a database column
     * @return an ISO8601 formatted String without millis
     */
    public static String dayTimeToIso(final String dayInYMDHMSFormat) {
        if(StringUtils.isEmpty(dayInYMDHMSFormat)) {
            return null;
        }
        DateTime dt = dbDateParser.parseDateTime(dayInYMDHMSFormat);
        return dateTimeToIsoNoMillis(dt);
    }

    /**
     * Accepts a java.util.Date, converts to "Market time", then spits out the
     * minute portion.
     *
     * @param date
     *            a Date instance
     * @return a String containing minutes. Range restriction from 0-59 minutes.
     */
    public static String dateToMinute(final Date date) {
        final DateTime dt = dateToMarketDateTime(date);
        return minuteFormat.print(dt);
    }

    /**
     * Converts a java.util.Date to an org.joda.time.DateTime date with the
     * market timezone
     *
     * @param date
     * @return
     */
    protected static DateTime dateToMarketDateTime(final Date date) {
        final DateTime dateTime = new DateTime(date);
        return dateTime.withZone(marketTimeZone);
    }

    /**
     * @return a set of minute interval label keys
     */
    public static String[] getIntervalLabels() {
        return intervalLabels;
    }

    /**
     * <p/>
     * Gets a <code>day</code> and <code>hour</code> and transforms in to ISO
     * formatted date and time. Considers long and short day offset differences.
     * If it is a short day hour 1 {@link shortDayLabels} is in non DST hours
     * rest are in DST hours if it is a long day hour 1 {@link longDayLabels} is
     * in DST hours rest are in non DST hours, day hour 2 is handled like a
     * repeat of hour 1 except with a standard time offset. If <code>hour</code>
     * is "24" need to advance the day to the next day, i.e 24th hour of today
     * is 00th hour next day.
     *
     * @param day
     *            day in ISO8601 String format (no millis)
     * @param hour
     *            an hour returning from <code>labelsForDay</code> method
     * @return <p/>
     */
    public static String getHourInIso(final String day, final String hour) {
        String result = null;
        if (day != null && hour != null) {
            DateTime dateTime = SSTimeUtil.isoDayToDateTime(day);
            String timeZoneOffset = timeZoneOffsetFormat.print(dateTime);
            String adjustedHour = hour;

            if (SSTimeUtil.hoursInDay(dateTime) < 24) { // short day
                // hour 1 should be in -06:00, rest should be in -05:00
                if (hour.equals(shortDayLabels[0])) {
                    timeZoneOffset = STANDARD_OFFSET;
                } else {
                    timeZoneOffset = DST_OFFSET;
                }
            } else if (SSTimeUtil.hoursInDay(dateTime) > 24) { // long day
                // hour 1 should be in -05:00, hour 2 should be converted to
                // "01"
                // with -06:00 time zone offset, and rest should be in -06:00
                if (hour.equals(longDayLabels[0])) {
                    timeZoneOffset = DST_OFFSET;
                } else if (hour.equals(longDayLabels[1])) {
                    timeZoneOffset = STANDARD_OFFSET;
                    adjustedHour = "01";
                } else {
                    timeZoneOffset = STANDARD_OFFSET;
                }
                if (hour.contains("*")) {
                    adjustedHour = hour.substring(0, 2);// remove "*02"
                    timeZoneOffset = STANDARD_OFFSET;
                }
            }
            if (hour == "24") {
                adjustedHour = "00";
                dateTime = dateTime.plusDays(1);
            }

            dateTime = dateTime.withHourOfDay(Integer.valueOf(adjustedHour)).withZoneRetainFields(
                    DateTimeZone.forID(timeZoneOffset));
            result = isoFormat.print(dateTime);
        }
        return result;
    }

    /**
     * Calculate a minute interval based on an ISO8601 formatted String (no
     * millis), an hour label, and an interval label
     *
     * @param day
     *            an ISO8601 formatted String (no millis)
     * @param hour
     *            an hour label (usually 01-24)
     * @param intervalLabel
     *            an interval label (the key, 01-12)
     * @return a minute interval
     */
    public static String getIntervalInIso(final String day, final String hour, final String intervalLabel) {
        final String isoDate = getHourInIso(day,hour);
        DateTime dateTime = isoToDateTime(isoDate);
        dateTime = dateTime.plusMinutes(Integer.parseInt(intervalLabel));
        if (intervalLabel == "00") {
            dateTime = dateTime.plusHours(1).withMinuteOfHour(0);
        }
        return dateTimeToIsoNoMillis(dateTime);
    }

    /**
     * Calculate a minute interval based on an ISO8601 formatted String (no
     * millis), an hour label, and an interval label
     *
     * @param day
     *            an ISO8601 formatted String (no millis)
     * @param hour
     *            an ending hour label (usually 01-24)
     * @param intervalLabel
     *            an interval label (05, 10, 15, ...)
     * @return a minute interval from the start of the hour
     */
    public static String getIntervalEndingInIso(final String day, final String hour, final String intervalLabel) {
        DateTime dateTime = isoToDateTime(day);
        dateTime = dateTime.withHourOfDay(Integer.parseInt(hour) - 1).plusMinutes(Integer.parseInt(intervalLabel));
        return dateTimeToIsoNoMillis(dateTime);
    }

    /**
     * Determine if an interval based on an ISO8601 formatted String is a valid 5 minute interval
     *
     * @param isoInterval
     *            an interval
     * @return true if input is a valid 5 minute interval
     */
    public static boolean isValid5MinuteInterval(final String isoInterval) {
        return valid05MinuteIntervals.contains(new Integer(isoToDateTime(isoInterval).minuteOfHour().get()));
    }

    /**
     * Determine if an interval based on an ISO8601 formatted String is a valid 15 minute interval
     *
     * @param isoInterval
     *            an interval
     * @return true if input is a valid 15 minute interval
     */
    public static boolean isValid15MinuteInterval(final String isoInterval) {
        return valid15MinuteIntervals.contains(new Integer(isoToDateTime(isoInterval).minuteOfHour().get()));
    }

    /**
     * Determine if an interval based on an ISO8601 formatted String is a valid 60 minute interval
     *
     * @param isoInterval
     *            an interval
     * @return true if input is a valid 60 minute interval
     */
    public static boolean isValid60MinuteInterval(final String isoInterval) {
        return valid60MinuteIntervals.contains(new Integer(isoToDateTime(isoInterval).minuteOfHour().get()));
    }

    /**
     * Return the starting day accounting for Hour 00:00:00 being the day after.
     * The date is coerced into the market time zone before computing the hour
     * When hour 00 is detected the day is decremented. The returned date has a
     * time of 00:00:00 in the market time zone.
     *
     * @param dt
     *            an org.joda.time.DateTime
     * @return an org.joda.time.DateTime expressing an operating day
     */
    public static DateTime getOperatingDay(DateTime dt) {
        dt = dt.withZone(SSTimeUtil.getMarketTimeZone());
        if (dt.getHourOfDay() == 0) {
            dt = dt.minusDays(1);
        }
        return dt.withMillisOfDay(0);
    }


}
