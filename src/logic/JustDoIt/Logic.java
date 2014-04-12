package logic.JustDoIt;

import task.JustDoIt.*;
import API.*;
import java.util.ArrayList;
import java.util.Collections;
import storage.JustDoIt.*;
import java.util.Stack;
/**
 * This class is the logic. The logic acquires parsed user command from the UI
 * and return the result of the execution of the corresponding command
 * 
 * @author Cui Wei A0091621M
 */

public class Logic implements ILogic {

	private static Logic theOne = new Logic();

	private ArrayList<Task> current;    // current arrayList stores the task
	// objects, and is where each function
	// access
	// to manipulate task objects

	private Stack<ArrayList<Task>> previous; 
	private ArrayList<Task> toDisplay;

	private Storage store;
	private KeywordComparator compareKeyword = new KeywordComparator();
	private TimeComparator compareTime = new TimeComparator();

	private final Task dummyTaskFound = new Task("searchFound");
	private final Task dummyTaskSimilar = new Task("searchSimilar");
	private final Task dummyTaskNotFound = new Task("searchNothing");

	public boolean shouldRefresh=false;
	
	private static final String ADDED_SUCCESS = "added successfully to schedule";
	private static final String ADDED_SUCCESS_WARNING = "WARNING: added to schedule.\nEnd time is before the start time.\nuse undo to cancel the addition if you want.";
	private static final String DELETED_SINGLE_TASK_SUCCESS = "deleted successfully from schedule";
	private static final String DELETED_All_TASKS_SUCCESS = "All tasks deleted from schedule";
	private static final String DELETED_COMPLETED_TASKS_SUCCESS = "All completed tasks deleted from schedule";
	private static final String DELETED_KEYWORD_TASKS_SUCCESS = "tasks related deleted from schedule";
	private static final String UPDATE_TASK_SUCCESS = "updated successfully";
	private static final String UPDATE_TASK_SUCCESS_WARNING = "WARNING: updated successfully.\nEnd time is before the start time.\nuse undo to cancel the addition if you want.";
	private static final String UNDO_OPERATION_SUCCESS = "The previous opreration has been cancelled";
	private static final String NO_MATCHES_FOUND = "No matches found";
	private static final String INDEX_OUT_OF_BOUND = "Invalid index";
	private static final String GENERAL_ERROR_MESSAGE = "Unexpected error";
	private static final String MULTIPLE_MATCHES_FOUND = "Multiple match found.\nPlease enter more detailed information";
	private static final String CANNOT_UNDO = "No operations to undo!";


	/**
	 * This is the default constructor of the logic class
	 */
	private Logic() {
		
		shouldRefresh=false;
		toDisplay = new ArrayList<Task>();
		current = new ArrayList<Task>();
		previous = new Stack<ArrayList<Task>>();
		store = Storage.getInstance();
		current = store.loadTasks();
		backUp();
	}


	/**
	 * This function executes commands of the user and return a string
	 * indicating the executing results of each corresponding command
	 * 
	 * @param info
	 *            is a string array containing all the information of a user's
	 *            input string parsed by a parser object
	 */
	@Override
	public Object executeCommand(String[] info) {

		String commandType = info[0];
		switch (commandType) {
		case "add" :
			return add(info);

		case "deleteName" :
			return deleteByKeyword(info[5]);

		case "deleteNum" :
			return deleteByIndex(Integer.parseInt(info[5]));

		case "deleteAll" :
			return deleteAll();

		case "deleteCompleted" :
			return deleteCompleted();

		case "updateTask" :
			return updateTask(info[1], info[5], info[2], info[3]);

		case "updateStatus" :
			return updateStatus(info[1], info[5]);

		case "done" :
			return updateStatusWithIndex(Integer.parseInt(info[5]));

		case "updateTaskIndex" :
			return updateTaskIndex(Integer.parseInt(info[1]), info[2], info[3], info[5]);//1 index 5 new name

		case "searchKey" :
			return searchKeyword(info[5]);

		case "searchTime" :
			return searchTime(info[5]);

		case "searchThisWeek" :
			return searchThisWeek();

		case "searchPeriod" :
			return searchPeriod(info[2], info[3]);

		case "searchTag" :
			return searchTag(info[4]);

		case "displayAll" :
			return displayAll();

		case "displayPending" :
			return displayPending();

		case "displayPendingNum" :
			return displayPendingNum(Integer.parseInt(info[5]));

		case "displayUnfinished" :
			return displayUnfinished();

		case "undo" :
			return undo();

		}

		return GENERAL_ERROR_MESSAGE;

	}




	/**
	 * This function creates a task object and adds it to the current arrayList
	 * 
	 * @param info
	 *            is the command input containing information of the added task
	 *            by the user
	 * @return ADDED_SUCCESS is a String indicating that the task has been
	 *         successfully added to the schedule
	 */
	private String add(String[] info) {

		backUp();
		
		String[] taskInfo = new String[4];
		for (int i = 0; i < 4; i++) {
			taskInfo[i] = info[i + 1];
		}
		Task createdNew = new Task(taskInfo);
		current.add(createdNew);
		
		writeBack();
		
		shouldRefresh=true;

		if(createdNew.isValidTime()){
			return ADDED_SUCCESS;
		} else{
			return ADDED_SUCCESS_WARNING;
		}
	}

	/**
	 * This function deletes all the tasks stored in the string
	 * 
	 * @return DELETED_All_TASKS_SUCCESS is a String indicating that the task
	 *         has been successfully deleted
	 */
	private String deleteAll() {

		backUp();
		
		current.clear();
		store.clearStorage();
		
		shouldRefresh=true;
		
		return DELETED_All_TASKS_SUCCESS;

	}

	/**
	 * This function deletes a task object from the current arrayList according
	 * to the index. Note that this function can only be executed after a search
	 * or display function
	 * 
	 * @param i
	 *            is the index in the toDisplay arrayList
	 * @return DELETED_SINGLE_TASK_SUCCESS is a String indicating that
	 *         corresponding the task has been successfully deleted
	 */
	private String deleteByIndex(int i) {


		backUp();

		if (i <= 0 || i > toDisplay.size()){
			return INDEX_OUT_OF_BOUND;
		}

		Task temp = toDisplay.get(i - 1);
		current.remove(temp);

		if (current.size() != 0){
			writeBack();
		} else{
			store.clearStorage();
		}
		
		shouldRefresh = true;

		return DELETED_SINGLE_TASK_SUCCESS;

	}

	/**
	 * This function deletes all the completed tasks
	 * 
	 * @return DELETED_COMPLETED_TASKS_SUCCESS is a String indicating that all
	 *         the completed tasks have been successfully deleted
	 */
	private String deleteCompleted() {

		backUp();
		
		for (int i = 0; i < current.size(); i++) {
			if (current.get(i).getTaskStatus().equalsIgnoreCase("completed")){
				current.remove(i);
			}
		}
		
		writeBack();
		
		shouldRefresh=true;
		
		return DELETED_COMPLETED_TASKS_SUCCESS;
	}

	/**
	 * This function deletes a task object from the schedule according to the
	 * keyword
	 * 
	 * @param keyword
	 *            is a string containing the keyword inputed by the user
	 * @return DELETED_SINGLE_TASK_SUCCESS is a String indicating that the tasks
	 *         have been successfully deleted
	 */
	private String deleteByKeyword(String keyword) {

		backUp();

		ArrayList<Task> toRemove = searchKeyword(keyword);

		if (!(toRemove.get(0).getTaskName().equalsIgnoreCase("searchFound"))){
			return NO_MATCHES_FOUND;
		}

		toRemove.remove(0);

		int i = 0;

		while (i < toRemove.size()) {
			Task temp = toRemove.get(i);
			current.remove(temp);
			i++;
		}

		writeBack();
		
		shouldRefresh=true;

		return DELETED_KEYWORD_TASKS_SUCCESS;

	}

	/**
	 * This function updates the name or(and) time of a task
	 * 
	 * @param taskName
	 *            is a string containing the task name of the task that the user
	 *            want to modify
	 * @param newName
	 *            is a string of new task name the user want to change to
	 * @param startTime
	 *            is a string of new start time the user want to change to
	 * @param endTime
	 *            is a string of new end time the user want to change to
	 * @return UPDATE_TASKSUCCESS is an string indicating a success of updating
	 * @return GENERAL_ERROR_MESSAGE is an string indicating a operation failure
	 */
	private String updateTask(String taskName, String newName, String startTime,
			String endTime) {

		backUp();

		ArrayList<Task> found = searchKeyword(taskName);

		for(int i = 0; i < found.size(); i++){
			resetTaskKeyword(found.get(i));
		}

		if (!(found.get(0).getTaskName().equalsIgnoreCase("searchFound"))){
			return NO_MATCHES_FOUND;
		}

		found.remove(0);

		if (found.size() > 1){
			return MULTIPLE_MATCHES_FOUND;
		}

		Task temp = found.get(0);

		if (newName != null){
			temp.setTaskName(newName);
		}
		if (startTime != null){
			temp.setStartTime(startTime);
		}
		if (endTime != null){
			temp.setEndTime(endTime);
		}

		writeBack();
		
		shouldRefresh=true;

		if(temp.isValidTime()){
			return UPDATE_TASK_SUCCESS;
		} else{
			return UPDATE_TASK_SUCCESS_WARNING;
		}

	}

	/**
	 * This function updates the status of a task
	 * 
	 * @param taskName
	 *            is a string containing the task name of the task that the user
	 *            want to modify
	 * @param status
	 *            is a string of task status the user want to change to
	 * @return UPDATE_TASK_STATUS_SUCCESS is an string indicating a success of
	 *         updating
	 * @return GENERAL_ERROR_MESSAGE is an string indicating a operation failure
	 */
	private String updateStatus(String taskName, String status) {

		backUp();

		ArrayList<Task> found = searchKeyword(taskName);

		for(int i = 0; i < found.size(); i++){
			resetTaskKeyword(found.get(i));
		}


		if (!(found.get(0).getTaskName().equalsIgnoreCase("searchFound"))){
			return NO_MATCHES_FOUND;
		}

		found.remove(0);
		if (found.size() > 1){
			return MULTIPLE_MATCHES_FOUND;
		}


		found.get(0).setTaskStatus(status);

		writeBack();
		
		shouldRefresh=true;

		return UPDATE_TASK_SUCCESS;

	}

	/**
	 * This function updates the status of a task
	 * 
	 * @param index
	 *            is a the index of the task that the user want to modify
	 * @return UPDATE_TASK_STATUS_SUCCESS is an string indicating a success of
	 *         updating
	 */
	private String updateStatusWithIndex(int index) {

		backUp();

		if (index < 1 || index > toDisplay.size()){
			return INDEX_OUT_OF_BOUND;
		}

		Task temp = toDisplay.get(index - 1);


		temp.setTaskStatus("completed");

		writeBack();
		
		shouldRefresh=true;

		return UPDATE_TASK_SUCCESS;

	}



	/**
	 * This function updates the status of a task with index
	 * 
	 * @param index
	 *            is a the index of the task that the user want to modify
	 * @param startTime
	 *            is a the new start time
	 * @param endTime
	 *            is a the new end time
	  * @param newName
	 *            is a the new task name
	 * @return UPDATE_TASK_SUCCESS is an string indicating a success of
	 *         updating
	 */
	private String updateTaskIndex(int index, String startTime, String endTime, String newName) {

		if(index<1||index>toDisplay.size()){
			return INDEX_OUT_OF_BOUND;
		}

		Task temp = toDisplay.get(index-1);


		if (newName != null){
			temp.setTaskName(newName);
		}
		if (startTime != null){
			temp.setStartTime(startTime);
		}
		if (endTime != null){
			temp.setEndTime(endTime);
		}

		writeBack();
		
		shouldRefresh=true;

		if(temp.isValidTime()){
			return UPDATE_TASK_SUCCESS;
		} else{
			return UPDATE_TASK_SUCCESS_WARNING;
		}

	}

	/**
	 * This function returns an arrayList containing all the task objects
	 * keyword is a string inputed by the user
	 * 
	 * @return found is an arrayList containing the matched task objects
	 */
	private ArrayList<Task> searchKeyword(String keyword) {

		toDisplay.clear();
		String[] keywords = keyword.split(" ");

		for (int i = 0; i < current.size(); i++) {

			Task temp = current.get(i);

			if (containStrings(temp.getTaskName(), keywords)) {
				toDisplay.add(temp);

				temp.setKeyWord(keyword);
			}

		}

		if (toDisplay.size() != 0) {
			toDisplay = sortTask(toDisplay);
			toDisplay.add(0, dummyTaskFound);

			return toDisplay;
		}

		for (int i = 0; i < current.size(); i++) {

			Task temp = current.get(i);

			if (compareKeyword.similar(temp.getTaskName(), keywords)) {
				toDisplay.add(temp);

				String[] tempTaskName = temp.getTaskName().split(" ");
				String markWord = "";

				for (int j = 0; j < tempTaskName.length; j++) {
					if (compareKeyword.similar(tempTaskName[j], keywords)){
						markWord = markWord.concat(tempTaskName[j]).concat(" ");
					}
				}

				temp.setKeyWord(markWord);

			}

		}

		if (toDisplay.size() != 0) {
			toDisplay = sortTask(toDisplay);
			toDisplay.add(0, dummyTaskSimilar);

			return toDisplay;
		}

		toDisplay.add(dummyTaskNotFound);
		return toDisplay;

	}

	/**
	 * This function returns an arrayList containing all the task objects with
	 * end time matching the input
	 * 
	 * @param time
	 *            is a string of time information, inputed by the user
	 * @return toDisplay is an arrayList containing the matched task objects
	 */
	private ArrayList<Task> searchTime(String time) {
		toDisplay.clear();

		for (int i = 0; i < current.size(); i++) {
			Task temp = current.get(i);
			if (compareTime.isSameDate(temp.getEndTime(), time)){
				toDisplay.add(temp);
			}

		}

		if (toDisplay.size() != 0) {

			toDisplay = sortTask(toDisplay);
			toDisplay.add(0, dummyTaskFound);

			return toDisplay;
		}

		for (int i = 0; i < current.size(); i++) {
			Task temp = current.get(i);

			if (compareTime.isSimilarDate(time, temp.getStartTime())){
				toDisplay.add(temp);
			}

		}

		if (toDisplay.size() != 0) {

			toDisplay = sortTask(toDisplay);
			toDisplay.add(0, dummyTaskSimilar);

			return toDisplay;
		}

		toDisplay.add(dummyTaskNotFound);
		return toDisplay;
	}

	
	/**
	 * This function returns an arrayList containing all the task objects with
	 * end time within the current week
	 * 
	 * @return toDisplay is an arrayList containing the task objects with
	 * end time within the current week
	 */
	private ArrayList<Task> searchThisWeek() {

		toDisplay.clear();

		for (int i = 0; i < current.size(); i++) {
			Task temp = current.get(i);
			if (compareTime.inCurrentWeek(temp.getEndTime())){
				toDisplay.add(temp);
			}

		}

		toDisplay = sortTask(toDisplay);

		if (toDisplay.size() == 0){
			toDisplay.add(0, dummyTaskNotFound);
		} else{
			toDisplay.add(0, dummyTaskFound);
		}

		return toDisplay;

	}

	
	/**
	 * This function returns an arrayList containing all the task objects with
	 * end time within the specified period
	 * @param start
	 *            is a string of of the start time
	 * @param end
	 *            is a string of end time
	 * 
	 * @return toDisplay is an arrayList containing the task objects with
	 * end time within the period
	 */
	private ArrayList<Task> searchPeriod(String start, String end) {

		toDisplay.clear();

		for (int i = 0; i < current.size(); i++) {
			Task temp = current.get(i);
			if (compareTime.withinPeriod(temp.getEndTime(), start, end)){
				toDisplay.add(temp);
			}

		}

		toDisplay = sortTask(toDisplay);

		if (toDisplay.size() == 0){
			toDisplay.add(0, dummyTaskNotFound);
		} else{
			toDisplay.add(0, dummyTaskFound);
		}

		return toDisplay;

	}

	/**
	 * This function returns an arrayList containing all the task objects with
	 * tags matching the input
	 * 
	 * @param taggedWord
	 *            is a string of tag word information inputed by the user
	 * @return toDisplay is an arrayList containing the matched task objects
	 */
	private ArrayList<Task> searchTag(String taggedWord) {

		toDisplay.clear();

		for (int i = 0; i < current.size(); i++) {
			Task temp = current.get(i);
			if (temp.getTaggedWord().equalsIgnoreCase(taggedWord)){
				toDisplay.add(temp);
			}
		}

		if (toDisplay.size() != 0) {

			toDisplay = sortTask(toDisplay);
			toDisplay.add(0, dummyTaskFound);

			return toDisplay;
		}

		for (int i = 0; i < current.size(); i++) {
			Task temp = current.get(i);

			if (compareKeyword.similar(taggedWord, temp.getTaggedWord())){
				toDisplay.add(temp);
			}
		}

		if (toDisplay.size() != 0) {

			toDisplay = sortTask(toDisplay);
			toDisplay.add(0, dummyTaskSimilar);

			return toDisplay;
		}

		toDisplay.add(dummyTaskNotFound);
		return toDisplay;

	}

	/**
	 * This function returns an arrayList containing all the task objects with
	 * status "pending"
	 */
	private ArrayList<Task> displayPending() {
		toDisplay.clear();

		for (int i = 0; i < current.size(); i++) {
			Task temp = current.get(i);
			if (temp.getTaskStatus().equalsIgnoreCase("pending")){
				toDisplay.add(temp);
			}
		}


		if(toDisplay.isEmpty()){
			toDisplay.add(dummyTaskNotFound);
			return toDisplay;
		}


		toDisplay = sortTask(toDisplay);
		toDisplay.add(0, dummyTaskFound);


		return toDisplay;
	}

	/**
	 * This function returns an arrayList containing all the task objects
	 */
	private ArrayList<Task> displayAll() {

		toDisplay.clear();

		if (current.size() == 0){
			toDisplay.add(dummyTaskNotFound);
			return toDisplay;
		}

		for (int i = 0; i < current.size(); i++){
			toDisplay.add(current.get(i));
		}

		toDisplay = sortTask(toDisplay);
		toDisplay.add(0, dummyTaskFound);

		return toDisplay;
	}

	/**
	 * This function returns an arrayList containing all the task objects with
	 * status "pending"
	 */
	private ArrayList<Task> displayPendingNum(int num) {

		toDisplay.clear();
		for (int i = 0; i < current.size(); i++) {
			Task temp = current.get(i);
			if (temp.getTaskStatus().equalsIgnoreCase("pending")){
				toDisplay.add(temp);
			}
		}

		if(toDisplay.isEmpty()){
			toDisplay.add(dummyTaskNotFound);
			return toDisplay;
		}

		toDisplay = sortTask(toDisplay);

		while (toDisplay.size() > num) {
			toDisplay.remove(toDisplay.size() - 1);
		}

		toDisplay.add(0, dummyTaskFound);

		return toDisplay;
	}

	/**
	 * This function returns an arrayList containing all the objects with status
	 * "unfinished"
	 */
	private ArrayList<Task> displayUnfinished() {

		toDisplay.clear();

		for (int i = 0; i < current.size(); i++) {
			Task temp = current.get(i);
			if (temp.getTaskStatus().equalsIgnoreCase("unfinished")){
				toDisplay.add(temp);
			}
		}

		if(toDisplay.isEmpty()){
			toDisplay.add(dummyTaskNotFound);
			return toDisplay;
		}

		toDisplay = sortTask(toDisplay);
		toDisplay.add(0, dummyTaskFound);


		return toDisplay;
	}

	/**
	 * This function writes back the task objects in the current arrayList to
	 * the hard drive
	 */
	private void writeBack() {
		store.storeBack(current);
	}

	/**
	 * This function makes a copy of the current arrayList storing all the task
	 * objects to the previous arrayList
	 */
	private void backUp() {

		ArrayList<Task> temp=new ArrayList<Task>();

		for (int i = 0; i < current.size(); i++) {
			temp.add(new Task(current.get(i)));
		}

		previous.add(temp);

	}

	/**
	 * This function undoes the previous operation
	 * 
	 * @return DO_OPERATION_SUCCESS is an string indicating a successful undo
	 *         operation
	 */
	private String undo() {

		if(previous.isEmpty()){
			return CANNOT_UNDO;
		}

		current.clear();

		ArrayList<Task> temp=previous.pop();

		for (int i = 0; i < temp.size(); i++) {
			current.add(new Task(temp.get(i)));
		}

		writeBack();
		
		shouldRefresh=true;

		return UNDO_OPERATION_SUCCESS;
	}

	/**
	 * This function unions sorts task objects according to the endTime in
	 * ascending order
	 * 
	 * @param tasks
	 *            is a arrayList containing tasks to be sorted.
	 * @return tasks is a arrayList where the tasks have been sorted in
	 *         ascending order.
	 */
	private ArrayList<Task> sortTask(ArrayList<Task> tasks) {

		Collections.sort(tasks);
		return tasks;
	}



	private boolean containStrings(String toCheck, String[] keywords) {

		for (int i = 0; i < keywords.length; i++){
			if (!(toCheck.contains(keywords[i]))){
				return false;
			}
		}

		return true;
	}


	public void resetTaskKeyword(Task temp) {

		temp.setKeyWord(null);
	}

	public void resetShouldRefresh(){
		shouldRefresh=false;
	}

	public static Logic getInstance() {

		return theOne;

	}

}
