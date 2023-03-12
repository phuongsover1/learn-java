package com.semanticsquare.datetime;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

class DateTimeDemo {
	private static void go(String ds) {
	}

	 private static void testLegacyDateAPI() {
		System.out.println("\nDate class ...");
		System.out.print("sasa");
		Date currentDate = new Date();
		System.out.println("currentDate: " + currentDate);
		System.out.println("currentDate in ms: " + currentDate.getTime());
		System.out.println("\nCalendar class ...");
		Calendar expiryDate = new GregorianCalendar(2017, 05, 30);
		System.out.println("expiryDate: " + expiryDate);
		System.out.println("expiryDate: " + expiryDate.getTime());
		expiryDate.add(Calendar.MONTH, 8);
		System.out.println("new expiryDate: " + expiryDate);
		System.out.println("new expiryDate: " + expiryDate.getTime());

		// Time Zone Demo
		String[] timeZones = TimeZone.getAvailableIDs();
        timeZones.hashCode();
		// for (String timeZone: timeZones) {
		// System.out.println(timeZone);
		// }

		// No-arg constructor below ==> default timezone
		Calendar gameStartTime = new GregorianCalendar(TimeZone.getTimeZone("Japan"));
		gameStartTime.set(2017, Calendar.JANUARY, 03, 9, 00);
		System.out.println("gameStartTime: " + gameStartTime.getTime());
		System.out.println("Japan time -- MONTH/DAY at hr:min:sec (AM/PM) -- " + (gameStartTime.get(Calendar.MONTH) + 1)
				+ "/" + gameStartTime.get(Calendar.DAY_OF_MONTH) + " at " + gameStartTime.get(Calendar.HOUR) + ":"
				+ gameStartTime.get(Calendar.MINUTE) + ":" + gameStartTime.get(Calendar.SECOND) + " "
				+ (gameStartTime.get(Calendar.AM_PM) == 0 ? "AM" : "PM"));

		System.out.println("sasa");

		System.out.println("Asia/Kolkata time -- MONTH/DAY at hr:min:sec (AM/PM) -- "
				+ (gameStartTime.get(Calendar.MONTH) + 1) + "/" + gameStartTime.get(Calendar.DAY_OF_MONTH) + " at "
				+ gameStartTime.get(Calendar.HOUR) + ":" + gameStartTime.get(Calendar.MINUTE) + ":"
				+ gameStartTime.get(Calendar.SECOND) + " " + (gameStartTime.get(Calendar.AM_PM) == 0 ? "AM" : "PM"));

		gameStartTime.setTimeZone(TimeZone.getTimeZone("GMT+7:00"));
		System.out.println("Custom time -- MONTH/DAY at hr:min:sec (AM/PM) -- "
				+ (gameStartTime.get(Calendar.MONTH) + 1) + "/" + gameStartTime.get(Calendar.DAY_OF_MONTH) + " at "
				+ gameStartTime.get(Calendar.HOUR) + ":" + gameStartTime.get(Calendar.MINUTE) + ":"
				+ gameStartTime.get(Calendar.SECOND) + " " + (gameStartTime.get(Calendar.AM_PM) == 0 ? "AM" : "PM"));

		// After/Before demonstration
		Calendar gameFinal = new GregorianCalendar(TimeZone.getTimeZone("Japan"));
		gameFinal.set(2017, Calendar.JANUARY, 16, 9, 00);
		System.out.println("After? " + gameStartTime.after(gameFinal));
		System.out.println("Before? " + gameStartTime.before(gameFinal));

	}

	private static void testDateTimeAPI() {
		// Use case 1: Software renewal
		LocalDate expiryDate = LocalDate.of(2017, Month.JUNE, 30);
		System.out.println("expiryDate: " + expiryDate);

		LocalDate newExpiryDate = expiryDate.plusMonths(8);
		System.out.println("newExpiryDate: " + newExpiryDate);

		// Other methods: plus & minus methods, isBefore, isAfter
		System.out.println("\nYear: " + newExpiryDate.getYear());
		System.out.println("\nMonth: " + newExpiryDate.getMonth());
		System.out.println("\nDay of month: " + newExpiryDate.getDayOfMonth());
		System.out.println("\nDay of week: " + newExpiryDate.getDayOfWeek());
		System.out.println("\nis Leap year: " + newExpiryDate.isLeapYear());


		// get returns an int
		System.out.println("year again: " + newExpiryDate.get(ChronoField.YEAR));
		System.out.println("month again: " + newExpiryDate.get(ChronoField.MONTH_OF_YEAR));
		System.out.println("day of month again: " + newExpiryDate.get(ChronoField.DAY_OF_MONTH));

		// parse String
		LocalDate epoch = LocalDate.parse("1970-01-01"); // yyyy-mm-dd
		System.out.println("epoch year: " + epoch.getYear());

		// LocalTime
		LocalTime time = LocalTime.of(9, 00, 00);
		System.out.println("\nTime -- hr: " + time.getHour());

		// LocalDate
		LocalDate gameStartDate = LocalDate.of(2017, Month.JULY, 03);

		// LocalDatetime
		LocalDateTime gameStartTime = LocalDateTime.of(gameStartDate, time);
		System.out.println("gameStartTime: " + gameStartTime);

		// TimeZone ==> ZoneId
		ZonedDateTime zonedGamesStartTime = ZonedDateTime.of(gameStartTime, ZoneId.of("Japan"));
		System.out.println("zonedGamesStartTime: " + zonedGamesStartTime);


		ZonedDateTime londonTime = zonedGamesStartTime.withZoneSameInstant(ZoneId.of("Europe/London"));
		System.out.println("londonTime: " + londonTime);

		// Use-Case 3: Age calculation (Period)

		LocalDate birthDay = LocalDate.of(2001, Month.MAY, 15);
		LocalDate today = LocalDate.now();
		Period period = birthDay.until(today);
		System.out.println("\nComplete Age: " + period.toString());
		System.out.println("Age: " + period);

		// Use-Case 4: Interval timing (Instant & Duration)

		Instant startTime = Instant.now();
		testLegacyDateAPI();
		Instant endTime = Instant.now();
		Duration timeElapsed = Duration.between(startTime, endTime);
		System.out.println("timeElapsed in ms:" + timeElapsed.toMillis());

		// Partial Classes
		System.out.println("Christmas: " + MonthDay.of(Month.DECEMBER, 25));
		System.out.println("Credit card expiry date: " + YearMonth.of(2017, Month.JULY));

	}

	public static void main(String[] args) {
		System.out.println(System.getProperty("java.security.properties"));

//		testDateTimeAPI();
		System.out.println();

	}
}
