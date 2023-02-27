package com.dev.util;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateUtil {
	public Calendar getCurrentCalendar() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;
	}

	public Date addCalendar(Calendar calendar, int calendarField, int duration) {
		calendar.add(calendarField, duration);
		Date date = calendar.getTime();

		return date;
	}
}
