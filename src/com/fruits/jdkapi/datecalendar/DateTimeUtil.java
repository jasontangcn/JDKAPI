package com.fruits.jdkapi.datecalendar;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * @author Jason Tang
 */

public class DateTimeUtil {
	/**
	 * Return current datetime string.
	 * 
	 * @return current datetime, pattern: "yyyy-MM-dd HH:mm:ss".
	 */
	public static String getDateTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}

	/**
	 * Return current datetime string.
	 * 
	 * @param pattern
	 *          format pattern
	 * @return current datetime
	 */
	public static String getDateTime(String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(new Date());
	}

	/**
	 * Return short format datetime string.
	 * 
	 * @param date
	 *          java.util.Date
	 * @return short format datetime
	 */
	public static String shortFormat(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		return df.format(date);
	}

	public static String normalFormat(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		return df.format(date);
	}

	/**
	 * Parse a datetime string.
	 * 
	 * @param param
	 *          datetime string, pattern: "yyyy-MM-dd HH:mm:ss".
	 * @return java.util.Date
	 */
	public static Date parse(String dateTime) {
		Date date = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = df.parse(dateTime);
		} catch (ParseException e) {
		}
		return date;
	}
}