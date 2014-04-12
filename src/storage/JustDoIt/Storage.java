package storage.JustDoIt;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.joda.time.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.esotericsoftware.yamlbeans.*;
import task.JustDoIt.Task;
import API.IStorage;

/**This class implements IStorage interface, two public methods implemented
 * to load tasks at beginning and write back to storage
 * @author Chen Zeyu
 */
public class Storage implements IStorage {

	private static Storage theInstance = new Storage();
	ArrayList<Task> storage = new ArrayList<Task>();
	static final String storagePath = "storage.yaml";
	private static final String STATUS_UNFINISHED= "unfinished";
	private static final String STATUS_PENDING= "pending";
	private static Logger logger = Logger.getLogger("storage_log");

	//check if local storage file exists, if not, create it.
	//@author A0091683X
	private boolean isStorageExists() {
		File f = new File(storagePath);

		if (f.exists()){
			return true;
		} else {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.log(Level.WARNING,"ERROR OCCURED WHEN CHECKING " + storagePath);
				e.printStackTrace();
			}
		}
		logger.log(Level.INFO, "Check Passed: " + storagePath);
		return true;
	}

	//prevent creating multiple instances of storage
	private Storage(){

	}

	/**
	 * This method load all tasks in local yaml file to an ArrayList<Task> 
	 * @return the ArrayList<Task> contains all tasks in local yaml file.
	 *
	 */
	//override loadTask in IStorage, load all tasks in text file to memory
	//@author A0091683X
	@Override
	public ArrayList<Task> loadTasks() {
		LocalDateTime now = DateTime.now().toLocalDateTime();

		if (isStorageExists()) {
			try {
				YamlReader rd = new YamlReader(new FileReader(storagePath));
				Task t;
				while ((t = (Task) rd.read())!=null) {
					storage.add(t);
				}
			} catch (IOException e) {
				logger.log(Level.WARNING,"EXCEPTION OCCURED WHEN LOADING TASK");
				e.printStackTrace();
			}
		}

		for(int i = 0; i < storage.size(); i++){//on startup, check expired tasks
			Task temp = storage.get(i);
			LocalDateTime tempEnd = temp.parseStringToDate(temp.endTime);
			if(tempEnd.isBefore(now)){
				temp.setTaskStatus(STATUS_UNFINISHED);
			} else if((temp.getTaskStatus().equalsIgnoreCase(STATUS_UNFINISHED) && tempEnd.isAfter(now))){
				temp.setTaskStatus(STATUS_PENDING);
			}
		}
		logger.log(Level.INFO,"Tasks loaded.");
		return storage;
	}

	/**
	 * This method clears local yaml file.
	 * @return TRUE if succeed.
	 *
	 */
	//when writing back, current text file should be cleared, this method clears the file
	//@author A0091683X
	public boolean clearStorage() {
		try {
			FileOutputStream file = new FileOutputStream(storagePath);
			file.write(new String("").getBytes());
			file.close();
		} catch (IOException e) {
			logger.log(Level.WARNING,"EXCEPTION OCCURED WHEN CLEAR STORAGE");
			e.printStackTrace();
		}
		logger.log(Level.INFO,"Clear storage successfully");
		return true;
	}

	/**
	 * This method returns the instance of storage.
	 * @return theInstance
	 */
	//@author A0091683X
	public static Storage getInstance() {
		return theInstance;
	}

	/**
	 * This method stores the given ArrayList<Task> back to the local yaml file.
	 * @return True if succeed.
	 *
	 */
	//override storeBack in IStorage, write the given list of tasks back to text file
	//@author A0091683X
	@Override
	public boolean storeBack(ArrayList<Task> allTasks) {
		if (clearStorage()) {
			if(allTasks.size() == 0){
				return true;
			}
			try {
				YamlWriter writer = new YamlWriter(new FileWriter(storagePath));
				for (int i = 0; i < allTasks.size(); i++) {
					writer.write(allTasks.get(i));
				}
				writer.close();
			} catch (IOException e) {
				logger.log(Level.WARNING,"EXCEPTION OCCURED WHEN WRITING TASK");
				e.printStackTrace();
			}
		}
		logger.log(Level.INFO,"Write back successfully.");
		return true;
	}
}
