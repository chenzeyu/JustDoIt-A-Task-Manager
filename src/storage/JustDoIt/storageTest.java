package storage.JustDoIt;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import task.JustDoIt.Task;

public class storageTest {

	@Test
	public void test() {
		Storage s = Storage.getInstance();
		String[] args1 = {"tell a true story","2012-10-28 23:59:00.000","2012-10-27 23:59:00.000","cs2103"};
		String[] args2 = {"tell a true story",null,"2012-12-27 23:59:00.000","cs2103"};
		String[] args3 = {"tell a true story",null,null,"cs2103"};	
		Task t1 = new Task(args1);
		Task t2 = new Task(args2);
		Task t3 = new Task(args3);
		ArrayList<Task> ta = new ArrayList<Task>();
		ta.add(t1);
		ta.add(t2);
		ta.add(t3);
		//test storeback
		assertTrue(s.storeBack(ta));
		//test loadTasks
		assertNotNull(s.loadTasks());
		//test clearStorage
		assertTrue(s.clearStorage());
	}
}


