package parser.JustDoIt;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

/**
 * This class is the time parser.
 * The parser has a public method getDate that takes in the String date and time input by the user,
 * parses it and returns it in a standardized format.
 * 
 * @author Choo Cheng Mun Paulina
 */
public class TimeParser {
	
	// These are the parsers for the date
	private static DateTimeParser[] dateParsers = { 
		DateTimeFormat.forPattern("dd-MM-yyyy").getParser(),
		DateTimeFormat.forPattern("dd/MM/yyyy").getParser(),
		DateTimeFormat.forPattern("dd MMM yyyy").getParser()
	};
	
	// These are the parsers for the date and time
	private static DateTimeParser[] dateTimeParsers = { 
		DateTimeFormat.forPattern("dd-MM-yyyy HH:mm").getParser(),
		DateTimeFormat.forPattern("dd-MM-yyyy HH.mm").getParser(),
		DateTimeFormat.forPattern("dd-MM-yyyy HHmm").getParser(),
		DateTimeFormat.forPattern("dd-MM-yyyy hh:mmaa").getParser(),
		DateTimeFormat.forPattern("dd-MM-yyyy hh.mmaa").getParser(),
		DateTimeFormat.forPattern("dd-MM-yyyy hhmmaa").getParser(),
		DateTimeFormat.forPattern("dd/MM/yyyy HH:mm").getParser(),
		DateTimeFormat.forPattern("dd/MM/yyyy HH.mm").getParser(),
		DateTimeFormat.forPattern("dd/MM/yyyy HHmm").getParser(),
		DateTimeFormat.forPattern("dd/MM/yyyy hh:mmaa").getParser(),
		DateTimeFormat.forPattern("dd/MM/yyyy hh.mmaa").getParser(),
		DateTimeFormat.forPattern("dd/MM/yyyy hhmmaa").getParser(),
		DateTimeFormat.forPattern("dd MMM yyyy HH:mm").getParser(),
		DateTimeFormat.forPattern("dd MMM yyyy HH.mm").getParser(),
		DateTimeFormat.forPattern("dd MMM yyyy HHmm").getParser(),
		DateTimeFormat.forPattern("dd MMM yyyy hh:mmaa").getParser(),
		DateTimeFormat.forPattern("dd MMM yyyy hh.mmaa").getParser(),
		DateTimeFormat.forPattern("dd MMM yyyy hhmmaa").getParser()
	};
	
	// This is the formatters made from the above parsers
	private static DateTimeFormatter formatterDate = new DateTimeFormatterBuilder().append(null, dateParsers).toFormatter();
	private static DateTimeFormatter formatterDateTime = new DateTimeFormatterBuilder().append(null, dateTimeParsers).toFormatter();

	/**
	 * This operation converts the input date into a standardized format.
	 * When input is date only, set time to 2359.
	 * 
	 * @param inputDate   The date and time input by the user.
	 * @return dateTime   The date and time in a standardized format.
	 * @throws IllegalArgumentException   If invalid date input format.
	 */
	//@author A0088751U
	public static String getDate(String inputDate) throws IllegalArgumentException {
		LocalDateTime parsedDate;
		String dateTime;
		
		try {
			parsedDate = formatterDate.parseDateTime(inputDate).toLocalDateTime();
			dateTime = parsedDate.toString().replace('T', ' ').replace("00:00:00.000", "23:59:00.000");
		} catch (IllegalArgumentException e) {
			parsedDate = formatterDateTime.parseDateTime(inputDate).toLocalDateTime();
			dateTime = parsedDate.toString().replace('T', ' ');
		}
		return dateTime;
	}
	
	/**
	 * This operation checks if the input is a date.
	 * 
	 * @param date   The date input.
	 * @return       True if date, false otherwise.
	 */
	//@author A0088751U
	public static boolean checkDate(String date){
		try {
			formatterDate.parseDateTime(date);
		} catch (IllegalArgumentException e) {
			try {
				formatterDateTime.parseDateTime(date);
			} catch (IllegalArgumentException f) {
				return false;
			}
			
			return true;
		}

		return true;
	}

}
