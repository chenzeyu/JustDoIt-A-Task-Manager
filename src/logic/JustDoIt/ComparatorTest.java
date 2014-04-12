package logic.JustDoIt;


import static org.junit.Assert.*;

import org.joda.time.LocalDate;


/**
 * unit testing for both KeywordComparator class and TimeComparator class
 * 
 * @author Cui Wei A0091621
 */
public class ComparatorTest {
	
	public KeywordComparator testWord=new KeywordComparator();
	
	public TimeComparator testTime=new TimeComparator();
	
	
	
	@org.junit.Test
	public void testStrings(){
	
	assertTrue(testWord.similar("cs2103t", "cs2103"));
	assertTrue(testWord.similar("cs2103", "cs2103t"));
	assertTrue(testWord.similar("cs2106", "cs2103"));
	assertTrue(testWord.similar("homework", "homewook"));
	assertTrue(testWord.similar("mid-term", "midterm"));
	assertTrue(testWord.similar("project meeting", "meeting"));
	assertTrue(testWord.similar("essay", "essy"));
	assertTrue(testWord.similar("writing blogs", "blogs"));
	assertTrue(testWord.similar("go shopping", "go shoping"));
	assertTrue(testWord.similar("pick up Tom at the airport", "airport"));
	assertTrue(testWord.similar("attend a conference", "go to a conference"));
	assertTrue(testWord.similar("review for final exams", "study for final exams"));
	assertTrue(testWord.similar("fix the laptop", "fix laptop"));
	assertTrue(testWord.similar("finish programming work", "finishing coding work"));
	assertTrue(testWord.similar("testing typos", "teting typs"));
	assertTrue(testWord.similar("ma3110", "ma3111"));
	
	
	assertFalse(testWord.similar("cs2103", "ma3110"));
	assertFalse(testWord.similar("pick up mom", "go to airport"));
	assertFalse(testWord.similar("project", "discussion"));
	assertFalse(testWord.similar("homework", "programming"));
	assertFalse(testWord.similar("buy some food", "buy stationeries"));
	assertFalse(testWord.similar("essay", "blogs"));
	assertFalse(testWord.similar("team work", "group discussion"));
	
	}
	
	
	@org.junit.Test
	public void testStringArrays(){
		
	String[] a={"cs2103t","homework","project"};
	String[] b={"pick","mom","at","airport"};
	String[] c={"wrirting","blogs","and","comment"};
	String[] d={"test","cases","for","software","engineering"};
	String[] e={"reviewing","for","final","exams"};
	String[] f={"buy","some","food"};
	String[] g={"that","is","it"};
	
	
	assertTrue(testWord.similar("cs2103t", a));
	assertTrue(testWord.similar("pick up", b));
	assertTrue(testWord.similar("blogs", c));
	assertTrue(testWord.similar("software", d));
	assertTrue(testWord.similar("revieeing", e));
	assertTrue(testWord.similar("buy food", f));
	assertTrue(testWord.similar("is it",g));
	
	
	
	assertFalse(testWord.similar("ma3110", a));
	assertFalse(testWord.similar("go home", b));
	assertFalse(testWord.similar("project", c));
	assertFalse(testWord.similar("homework", d));
	assertFalse(testWord.similar("buy some food", e));
	assertFalse(testWord.similar("essay", f));
	assertFalse(testWord.similar("team work", g));
	
	}
	
	
	@org.junit.Test
	public void testTimeSame(){
	
	assertTrue(testTime.isSameDate("12-11-2012", "12-11-2012"));
	assertTrue(testTime.isSameDate("12/11/2012", "12/11/2012"));
	assertTrue(testTime.isSameDate("09-03-2015", "09-03-2015"));
	
	
	assertTrue(testTime.isSameDate("12-11-2012 2359", "12-11-2012 2230"));
	assertTrue(testTime.isSameDate("12/11/2012 7pm", "12/11/2012 9am"));
	assertTrue(testTime.isSameDate("09-03-2015 20:20", "09-03-2015 05:23"));
	
	
	
	
	assertFalse(testTime.isSameDate("12-11-2012", "11-11-2012"));
	assertFalse(testTime.isSameDate("12-11-2012", "13-11-2012"));
	assertFalse(testTime.isSameDate("07-06-2015", "04-01-1999"));
	
	
	}
	
	
	
	@org.junit.Test
	public void testTimeSimilar(){
	
	assertTrue(testTime.isSimilarDate("12-11-2012", "12-12-2012"));
	assertTrue(testTime.isSimilarDate("12-11-2012", "10-11-2012"));
	assertTrue(testTime.isSimilarDate("12-11-2012", "12-11-2013"));
	assertTrue(testTime.isSimilarDate("12-11-2012", "12-11-2912"));
	assertTrue(testTime.isSimilarDate("12-11-2012", "12-11-2012"));
	
	assertTrue(testTime.isSimilarDate("12-11-2012 2359", "12-12-2012 2230"));
	assertTrue(testTime.isSimilarDate("12-11-2012 7pm", "10-11-2012 5am"));
	assertTrue(testTime.isSimilarDate("12-11-2012 20:20", "12-11-2013 05:23"));
	
	
	assertFalse(testTime.isSameDate("07-06-2015", "04-01-1999"));
	assertFalse(testTime.isSimilarDate("12-11-2012", "12-12-2912"));
	assertFalse(testTime.isSimilarDate("11-11-2012", "12-12-2012"));
	
	
	}
	
	@org.junit.Test
	public void testTimeCurrentWeek(){
		
		LocalDate currentDate = LocalDate.now();   //current date
		int dayOfWeek = currentDate.getDayOfWeek();
		
		for(int i=0;i<dayOfWeek;i++){
			LocalDate temp=currentDate.minusDays(i);
			String date=temp.toString();
			assertTrue(testTime.inCurrentWeek(date));
		}
		
		
		for(int i=0;i<=7-dayOfWeek;i++){
			LocalDate temp=currentDate.plusDays(i);
			String date=temp.toString();
			assertTrue(testTime.inCurrentWeek(date));
		}
		
		
		for(int i=dayOfWeek;i<100;i++){
			LocalDate temp=currentDate.minusDays(i);
			String date=temp.toString();
			assertFalse(testTime.inCurrentWeek(date));
		}
		
		
		for(int i=7-dayOfWeek+1;i<100;i++){
			LocalDate temp=currentDate.plusDays(i);
			String date=temp.toString();
			assertFalse(testTime.inCurrentWeek(date));
		}
		
	
	
	
	
	
	}
	
	@org.junit.Test
	public void testTimeWithinPeriod(){
	
	assertTrue(testTime.withinPeriod("2012-11-11", "2012-11-11","2012-11-11"));
	assertTrue(testTime.withinPeriod("2012-11-11", "2012-11-10","2012-11-12"));
	assertTrue(testTime.withinPeriod("2012-11-11", "2012-09-30","2013-05-21"));
	assertTrue(testTime.withinPeriod("2012-11-11", "2012-11-11","2013-11-11"));
	assertTrue(testTime.withinPeriod("2013-11-11", "2012-11-11","2013-11-11"));

	
	assertFalse(testTime.withinPeriod("2012-11-11", "2012-11-12","2012-11-19"));
	assertFalse(testTime.withinPeriod("2012-11-11", "2012-11-08","2012-11-10"));
	assertFalse(testTime.withinPeriod("2012-11-11", "2012-11-17","2012-11-22"));
	assertFalse(testTime.withinPeriod("2012-11-11", "2012-03-12","2012-04-19"));
	
	
	
	}
	
	

}
