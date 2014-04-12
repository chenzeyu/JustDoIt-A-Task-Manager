package logic.JustDoIt;

import org.joda.time.Days;
import org.joda.time.LocalDate;

/**
   This class is the timeComparator.
   The object of this class determines the specifics of a time information


* @author Cui Wei A0091621
*/

public class TimeComparator {

	private LocalDate currentDate = LocalDate.now();   //current date
	private int dayOfWeek = currentDate.getDayOfWeek();
	private int lowerBound = 1 - dayOfWeek;
	private int upperBound = 7 - dayOfWeek;

	
	
	public boolean isSameDate(String one, String other) {
		
		assert(one.length() >= 10);
		assert(other.length() >= 10);
		
		one = one.substring(0, 10);
		other = other.substring(0, 10);
		return one.equalsIgnoreCase(other);
	}

	
	private int numberOfDifference(String one, String other) {
		
		assert(one != null && other != null);
		assert(one.length() == other.length());

		int count = 0;

		for (int i = 0; i < one.length(); i++){
			if (one.charAt(i) != other.charAt(i)){
				count++;
			}
		}

		return count;

	}
	
	public boolean isSimilarDate(String one, String other){
		
		assert(one.length() >= 10);
		assert(other.length() >= 10);
		
		one = one.substring(0, 10);
		other = other.substring(0, 10);
		return numberOfDifference(one, other) <= 1;
	}
	
	public boolean inCurrentWeek(String time){
		
		assert(time.length() >= 10);
		
		time = time.substring(0, 10);
		LocalDate temp = new LocalDate(time);
		
		int diff = Days.daysBetween(currentDate, temp).getDays();
		
		return (diff >= lowerBound && diff <= upperBound);
		
	}
	
	public boolean withinPeriod(String time, String start, String end){
		
		assert(time.length() >= 10);
		assert(start.length() >= 10);
		assert(end.length() >= 10);
		
		time = time.substring(0, 10);
		start = start.substring(0, 10);
		end = end.substring(0, 10);
		
		LocalDate check = new LocalDate(time);
		LocalDate startDate = new LocalDate(start);
		LocalDate endDate = new LocalDate(end);
		
		int offsetOne = Days.daysBetween(startDate, check).getDays();
		int offsetTwo = Days.daysBetween(check, endDate).getDays();
		
		return offsetOne >= 0 && offsetTwo >= 0;
		
		}
	
	
	
}
