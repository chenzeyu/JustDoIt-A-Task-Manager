package parser.JustDoIt;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This class is the testing class for TimeParser
 * 
 * @author Choo Cheng Mun Paulina
 */
public class TimeParserTest {

	/**
	 * This operation test parsing for the getDate method
	 */
	@Test
	//@author A0088751U
	public void getDateTest() {
		assertTrue(TimeParser.getDate("8-2-2012").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8-2-2012 23:59").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8-2-2012 23.59").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8-2-2012 2359").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8-2-2012 11:59pm").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8-2-2012 11.59pm").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8-2-2012 1159pm").equals("2012-02-08 23:59:00.000"));
		
		assertTrue(TimeParser.getDate("8/2/2012").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8/2/2012 23:59").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8/2/2012 23.59").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8/2/2012 2359").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8/2/2012 11:59pm").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8/2/2012 11.59pm").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8/2/2012 1159pm").equals("2012-02-08 23:59:00.000"));
		
		assertTrue(TimeParser.getDate("8 feb 2012").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8 feb 2012 23:59").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8 feb 2012 23.59").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8 feb 2012 2359").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8 feb 2012 11:59pm").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8 feb 2012 11.59pm").equals("2012-02-08 23:59:00.000"));
		assertTrue(TimeParser.getDate("8 feb 2012 1159pm").equals("2012-02-08 23:59:00.000"));
	}
	
	/**
	 * This operation test parsing for the checkDate method
	 */
	@Test
	//@author A0088751U
	public void checkDateTest() {
		assertFalse(TimeParser.checkDate("hello world"));
		assertTrue(TimeParser.checkDate("8 feb 2012"));
		assertTrue(TimeParser.checkDate("8 feb 2012 2359"));
	}

}
