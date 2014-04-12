package parser.JustDoIt;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * This class is the testing class for Parser
 * 
 * @author Choo Cheng Mun Paulina
 */
public class ParserTest {

	private String[] test;
	
	/**
	 * This operation test parsing for the add function
	 */
	@Test
	//@author A0088751U
	public void addTest() {
		test = Parser.getCommand("add eat lunch \\from 8/12/2012 1200pm \\by 8/12/2012 0100pm \\# omnomnom");
		assertTrue(test[0].equals("add"));
		assertTrue(test[1].equals("eat lunch"));
		assertTrue(test[2].equals("2012-12-08 12:00:00.000"));
		assertTrue(test[3].equals("2012-12-08 13:00:00.000"));
		assertTrue(test[4].equals("omnomnom"));
		assertTrue(test[5] == null);
		
		test = Parser.getCommand("add eat lunch");
		assertTrue(test[0].equals("add"));
		assertTrue(test[1].equals("eat lunch"));
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5] == null);
		
		test = Parser.getCommand("add eat lunch \\by 8/12/2012 1200pm");
		assertTrue(test[0].equals("add"));
		assertTrue(test[1].equals("eat lunch"));
		assertTrue(test[2] == null);
		assertTrue(test[3].equals("2012-12-08 12:00:00.000"));
		assertTrue(test[4] == null);
		assertTrue(test[5] == null);
		
		test = Parser.getCommand("add   ");
		assertTrue(test[0].equals("errorParam"));
		
		test = Parser.getCommand("add eat lunch \\by bahbahblacksheep");
		assertTrue(test[0].equals("errorParam"));
	}
	
	/**
	 * This operation test parsing for the delete function
	 */
	@Test
	//@author A0088751U
	public void deleteTest() {
		test = Parser.getCommand("delete eat lunch");
		assertTrue(test[0].equals("deleteName"));
		assertTrue(test[1] == null);
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5].equals("eat lunch"));
		
		test = Parser.getCommand("delete 4");
		assertTrue(test[0].equals("deleteNum"));
		assertTrue(test[1] == null);
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5].equals("4"));
		
		test = Parser.getCommand("delete all");
		assertTrue(test[0].equals("deleteAll"));
		assertTrue(test[1] == null);
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5] == null);
		
		test = Parser.getCommand("delete");
		assertTrue(test[0].equals("errorParam"));
	}
	
	/**
	 * This operation test parsing for the display function
	 */
	@Test
	//@author A0088751U
	public void displayTest() {
		test = Parser.getCommand("display all");
		assertTrue(test[0].equals("displayAll"));
		assertTrue(test[1] == null);
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5] == null);
		
		test = Parser.getCommand("display unfinished");
		assertTrue(test[0].equals("displayUnfinished"));
		assertTrue(test[1] == null);
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5] == null);
		
		test = Parser.getCommand("display pending");
		assertTrue(test[0].equals("displayPending"));
		assertTrue(test[1] == null);
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5] == null);
		
		test = Parser.getCommand("display pending 5");
		assertTrue(test[0].equals("displayPendingNum"));
		assertTrue(test[1] == null);
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5].equals("5"));
		
		test = Parser.getCommand("display nonsense");
		assertTrue(test[0].equals("errorParam"));
		
		test = Parser.getCommand("display  ");
		assertTrue(test[0].equals("errorParam"));
	}
	
	/**
	 * This operation test parsing for the modify function
	 */
	@Test
	//@author A0088751U
	public void modifyTest() {
		test = Parser.getCommand("modify eat lunch \\by 8/12/2012 1200pm \\from 8/12/2012 0200pm \\name omnomnom \\# food");
		assertTrue(test[0].equals("updateTask"));
		assertTrue(test[1].equals("eat lunch"));
		assertTrue(test[2].equals("2012-12-08 14:00:00.000"));
		assertTrue(test[3].equals("2012-12-08 12:00:00.000"));
		assertTrue(test[4].equals("food"));
		assertTrue(test[5].equals("omnomnom"));
		
		test = Parser.getCommand("update complete eat lunch");
		assertTrue(test[0].equals("updateStatus"));
		assertTrue(test[1].equals("eat lunch"));
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5].equals("complete"));
		
		test = Parser.getCommand("done 1");
		assertTrue(test[0].equals("done"));
		assertTrue(test[1] == null);
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5].equals("1"));
		
		test = Parser.getCommand("change  ");
		assertTrue(test[0].equals("errorParam"));
	}
	
	/**
	 * This operation test parsing for the search function
	 */
	@Test
	//@author A0088751U
	public void searchTest() {
		test = Parser.getCommand("search lunch meal");
		assertTrue(test[0].equals("searchKey"));
		assertTrue(test[1] == null);
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5].equals("lunch meal"));
		
		test = Parser.getCommand("search this week");
		assertTrue(test[0].equals("searchThisWeek"));
		assertTrue(test[1] == null);
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5] == null);
		
		test = Parser.getCommand("search 8/2/2012");
		assertTrue(test[0].equals("searchTime"));
		assertTrue(test[1] == null);
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5].equals("2012-02-08 23:59:00.000"));
		
		test = Parser.getCommand("search \\from 8/2/2012 \\to 9/2/2012");
		assertTrue(test[0].equals("searchPeriod"));
		assertTrue(test[1].equals("dummy name"));
		assertTrue(test[2].equals("2012-02-08 23:59:00.000"));
		assertTrue(test[3].equals("2012-02-09 23:59:00.000"));
		assertTrue(test[4] == null);
		assertTrue(test[5] == null);
		
		test = Parser.getCommand("search \\# work");
		assertTrue(test[0].equals("searchTag"));
		assertTrue(test[1].equals("dummy name"));
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4].equals("work"));
		assertTrue(test[5] == null);
		
		test = Parser.getCommand("search \\from 8/2/2012");
		assertTrue(test[0].equals("errorParam"));
	}
	
	/**
	 * This operation test parsing for the undo function
	 */
	@Test
	//@author A0088751U
	public void undoTest() {
		test = Parser.getCommand("undo");
		assertTrue(test[0].equals("undo"));
		assertTrue(test[1] == null);
		assertTrue(test[2] == null);
		assertTrue(test[3] == null);
		assertTrue(test[4] == null);
		assertTrue(test[5] == null);
	}

}
