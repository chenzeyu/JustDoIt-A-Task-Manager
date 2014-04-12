package API;
import java.util.ArrayList;
import task.JustDoIt.Task;

public interface IUI {
	public void display(String message);
	public void display(ArrayList<Task> tasks);
}
