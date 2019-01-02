package com.air.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormats {

	public static String getDateFormat() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //当前时间时间格式化
		return format.format(date);
	}
	
	public static String getDateFormat(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //时间格式化
		return format.format(date);
	}

	public static boolean isValidDate(String str) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (Exception e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return convertSuccess;
	}

	/**
	 * 时间戳转日期
	 * 
	 * @param ms
	 * @return
	 */
	public static Date transForDate(Long ms) {
		if (ms == null) {
			ms = 0L;
		}
		long msl = (long) ms * 1000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date temp = null;
		if (ms != null) {
			try {
				String str = sdf.format(ms);
				temp = sdf.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}
	
	/**
	 * 计算时间（加天数）
	 * @param date
	 * @param num
	 * @return
	 */
	public static String addDate(Date date,Integer num) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		
		calendar.add(Calendar.DAY_OF_YEAR, num);
		
		return getDateFormat(calendar.getTime());
	}
	
	/**
	 * 计算时间（减天数）
	 * @param date
	 * @param num
	 * @return
	 */
	public static String subDate(Date date,Integer num) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		
		calendar.add(Calendar.DAY_OF_YEAR, 0-num);
		
		return getDateFormat(calendar.getTime());
	}
}
