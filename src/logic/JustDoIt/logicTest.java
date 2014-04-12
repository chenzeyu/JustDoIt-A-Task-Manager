package logic.JustDoIt;


import static org.junit.Assert.*;


/**
 * IMPORTANT: Please read me
 * 
 * This is the unit testing class for the logic class
 * Note that most of the methods and variables in logics class are now declared private
 * They were declared public during the developing process for easier testing
 * Now since all the testing has been done, they were changed to private therefore 
 * most of the testing code has been deleted (errors otherwise)
 * The testing of the logic class is then done mostly in exploratory method, combined with the GUI class
 *
 * @author CuiWei A0091621
 *
 */
public class logicTest {
	
	Logic logicInstance=Logic.getInstance();
	
	
	@org.junit.Test
	public void testShouldRefresh(){
	
	assertFalse(logicInstance.shouldRefresh);
	logicInstance.shouldRefresh = true;
	assertTrue(logicInstance.shouldRefresh);
	logicInstance.resetShouldRefresh();
	assertFalse(logicInstance.shouldRefresh);
	
	}
	
	
	
}
