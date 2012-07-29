package com.ecs.android.sample.twitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

	public static int GetCurrentTime_UTCSeconds() {
		Calendar c = Calendar.getInstance();
		int seconds = c.get(Calendar.SECOND);
		return seconds;
	}

	public static long GetCurrentTime_UTCMilliSeconds() {
		// return System.currentTimeMillis();
		Calendar c = Calendar.getInstance();
		// int ms = c.get(Calendar.MILLISECOND);
		return c.getTimeInMillis();
	}

	public static Date GetDate(long milliseconds) {
		return new Date(milliseconds);
	}

	public static Date GetDate(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			return new Date();
			// e.printStackTrace();
		}
	}

	public static String GetCurrentTime_String() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String curentDateandTime = sdf.format(new Date());
		return curentDateandTime;

		// Date currentDate = new Date(System.currentTimeMillis());
		//
		// And as mentioned by others to create a time
		// Time currentTime = new Time();
		// currentTime.setToNow();
		// String currentDateTimeString =
		// DateFormat.getDateInstance().format(new Date());

		// // long dtMili = System.currentTimeMillis();

		// Date d = new Date();
		// CharSequence s = DateFormat.format("EEEE, MMMM d, yyyy ",
		// d.getTime());

		// You can use android.text.format.Time:
		// Time now = new Time();
		// now.setToNow();

	}
}
