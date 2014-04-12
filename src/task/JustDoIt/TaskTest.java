/**
 * 
 */
package task.JustDoIt;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author chen zeyu
 *
 */
public class TaskTest {
	
	private static final String defaultStartTime = "1991-11-11 23:59:00.000";
	private static final String defaultEndTime = "2091-11-11 23:59:00.000";
	private static final String defaultTaggedWord = "k";
	private static final String defaultStatus = "pending";
	String[] args1 = {"tell a true story","2012-12-28 23:59:00.000","2012-11-10 23:59:00.000","cs2103"};
	String[] args2 = {"tell a true story",defaultStartTime,"2012-12-27 23:59:00.000","cs2103"};
	String[] args3 = {"tell a true story",defaultStartTime,defaultEndTime,defaultTaggedWord};	
	Task t1 = new Task(args1);
	Task t2 = new Task(args2);
	Task t3 = new Task(args3);

	/**
	 * Test method for {@link task.JustDoIt.Task#isDefaultStart()}.
	 */
	@Test
	public void testIsDefaultStart() {
		assertTrue(t3.isDefaultStart());
		assertFalse(t1.isDefaultStart());
		assertTrue(t2.isDefaultStart());
	}

	/**
	 * Test method for {@link task.JustDoIt.Task#isDefaultEnd()}.
	 */
	@Test
	public void testIsDefaultEnd() {
		assertTrue(t3.isDefaultEnd());
		assertFalse(t1.isDefaultEnd());
		assertFalse(t2.isDefaultEnd());
	}

	/**
	 * Test method for {@link task.JustDoIt.Task#isDefaultTag()}.
	 */
	@Test
	public void testIsDefaultTag() {
		assertTrue(t3.isDefaultTag());
		assertFalse(t1.isDefaultTag());
		assertFalse(t2.isDefaultTag());
	}

	/**
	 * Test method for {@link task.JustDoIt.Task#isUrgent()}.
	 */
	@Test
	public void testIsUrgent() {
		assertNotNull(t1.isUrgent());
		assertNull(t2.isUrgent());
		assertNull(t3.isUrgent());
	}

	/**
	 * Test method for {@link task.JustDoIt.Task#isValidTime()}.
	 */
	@Test
	public void testIsValidTime() {
		assertFalse(t1.isValidTime());
		assertTrue(t2.isValidTime());
		assertTrue(t3.isValidTime());
	}

	/**
	 * Test method for {@link task.JustDoIt.Task#compareTo(task.JustDoIt.Task)}.
	 */
	@Test
	public void testCompareTo() {
		assertEquals(-1,t1.compareTo(t2));
	}

}
