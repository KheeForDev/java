package com.kheefordev.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateUtil {
	public String getCurrentTimestampInString() {
		return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	}

	public Date addCalendar(int calendarField, Integer duration) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		calendar.add(calendarField, duration);

		return calendar.getTime();
	}
}