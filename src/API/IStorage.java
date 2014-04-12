package API;
import task.JustDoIt.Task;
import java.util.ArrayList;


public interface IStorage {
	
	public ArrayList<Task> loadTasks();
	public boolean storeBack(ArrayList<Task> allTasks);
}
