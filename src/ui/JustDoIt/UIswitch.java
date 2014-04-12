package ui.JustDoIt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import logic.JustDoIt.*;
import task.JustDoIt.*;
import parser.JustDoIt.*;

/**
 * This class acts as a facade between the GUI and Parser and Logic components.
 * It receives the user input from the GUI and sends them to Parser and Logic to be executed.
 * It also does any necessary processing.
 * 
 * @author Choo Cheng Mun Paulina
 */
public class UIswitch {

	// This is the instantiated Logic unit
	private static Logic logicUnit;

	// These are the IDs for errors
	private static final String ERROR_WRONG_COMMAND = "errorCommand";
	private static final String ERROR_WRONG_PARAM = "errorParam";
	private static final String ERROR_UNKNOWN = "errorUnknown";

	// These are error messages
	private static final String MESSAGE_ERROR_WRONG_COMMAND = "Error: Incorrect command entered.";
	private static final String MESSAGE_ERROR_WRONG_PARAM = "Error: Incorrect parameters entered.";
	private static final String MESSAGE_ERROR_UNKNOWN = "Error: Unknown error has occured.";

	// These are the special codes for search result types
	private static final String ID_SEARCH_EXACT = "searchFound";
	private static final String ID_SEARCH_SIMILAR = "searchSimilar";
	private static final String ID_SEARCH_NONE = "searchNothing";

	// These are result messages
	private static final String MESSAGE_SEARCH_EXACT = "Exact match found.";
	private static final String MESSAGE_SEARCH_SIMILAR = "Similar matches found.";
	private static final String MESSAGE_SEARCH_NONE = "No matches found.";
	private static final String MESSAGE_LIST = "All tasks displayed.";

	//	FileHandler logFile;
	//	private static Logger logger = Logger.getLogger("UIswitch_log");

	/**
	 * This operation instantiates the UIswitch object.
	 * It also initializes the Logic instance.
	 */
	//@author A0088751U
	public UIswitch(){
		logicUnit = Logic.getInstance();

		//try {
		//	logFile  = new FileHandler("UISwitch.log");
		//	logger.addHandler(logFile);
		//	logger.setLevel(Level.ALL);
		//	SimpleFormatter formatter = new SimpleFormatter();
		//	logFile.setFormatter(formatter);
		//	logger.log(Level.INFO, "Switch instantiated");
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
	}

	/**
	 * This operation executes the user input by communicating with the parser and logic.
	 * The list of tasks returned always has a message at the head.
	 * 
	 * @param userInput   The user input from the GUI.
	 * @return            A list of tasks to be displayed.
	 */
	//@author A0088751U
	@SuppressWarnings("unchecked")
	public List<Task> executeCommand(String userInput){
		//logger.log(Level.INFO, "Command received");
		String[] dataPacket = Parser.getCommand(userInput);
		boolean isParserError = checkParserError(dataPacket[0]);

		if(isParserError) {
			//logger.log(Level.WARNING, "Error from parser: " + dataPacket[0]);
			logicUnit.shouldRefresh = true;
			return createErrorMessageList(dataPacket[0]);
		}

		Object returnMessage = logicUnit.executeCommand(dataPacket);
		//logger.log(Level.INFO, "Message from logic received, processing now");

		if(returnMessage instanceof List) {
			//logger.log(Level.INFO, "List of tasks to display");
			return createMessageTaskList((ArrayList<Task>) returnMessage);
		} else {
			//logger.log(Level.INFO, "String message to display");
			return createMessageList((String) returnMessage);
		}
	}

	/**
	 * This operation creates the return list for an error.
	 * 
	 * @param ID   The ID of the error.
	 * @return     A list to be displayed.
	 */
	//@author A0088751U
	private ArrayList<Task> createErrorMessageList(String ID){
		ArrayList<Task> displayList = new ArrayList<Task>();
		Task messageHead = createErrorMessage(ID);
		displayList.add(messageHead);
		return displayList;
	}

	/**
	 * This operation creates the return list for a single message.
	 * 
	 * @param message   The message for the user.
	 * @return          A list to be displayed.
	 */
	//@author A0088751U
	private ArrayList<Task> createMessageList(String message){
		ArrayList<Task> displayList = new ArrayList<Task>();
		Task messageHead = createMessage(message);
		displayList.add(messageHead);
		return displayList;
	}

	/**
	 * This operation creates the return list for a list of tasks.
	 * 
	 * @param returnMessage   The list of tasks to be displayed.
	 * @return                A list to be displayed.
	 */
	//@author A0088751U
	private ArrayList<Task> createMessageTaskList(ArrayList<Task> returnMessage) {
		if(returnMessage == null || returnMessage.isEmpty()) {
			return null;
		}

		boolean isSearchResult = checkSearchResult(returnMessage.get(0));
		Task messageHead;

		if(isSearchResult) {
			//logger.log(Level.INFO, "Is search result");
			messageHead = createSearchMessage(returnMessage.get(0));
			returnMessage.remove(0);
			returnMessage.add(0, messageHead);
			return returnMessage;
		} else {
			//logger.log(Level.INFO, "Is display result");
			messageHead = createMessage(MESSAGE_LIST);
			returnMessage.add(0, messageHead);
			return returnMessage;
		}
	}

	/**
	 * This operation creates the task containing the message.
	 * 
	 * @param message   The message to be displayed.
	 * @return          A task containing the message in the task name.
	 */
	//@author A0088751U
	private Task createMessage(String message){
		return new Task(message);
	}

	/**
	 * This operation checks if the message from Logic is a search result.
	 * 
	 * @param dummyTask   The head of the list of tasks returned by Logic.
	 * @return            True if search result, false otherwise.
	 */
	//@author A0088751U
	private boolean checkSearchResult(Task dummyTask) {
		String headTask = dummyTask.getTaskName();
		return headTask.equals(ID_SEARCH_EXACT) || 
				headTask.equals(ID_SEARCH_SIMILAR) || 
				headTask.equals(ID_SEARCH_NONE);
	}

	/**
	 * This operation creates the return list for a search result.
	 * 
	 * @param dummyTask   The task containing the message from Logic.
	 * @return            A task with the message in the task name.
	 */
	//@author A0088751U
	private Task createSearchMessage(Task dummyTask) {
		String searchID = dummyTask.getTaskName();

		switch(searchID) {			
		case ID_SEARCH_EXACT :
			return createMessage(MESSAGE_SEARCH_EXACT);

		case ID_SEARCH_SIMILAR :
			return createMessage(MESSAGE_SEARCH_SIMILAR);

		default:
			return createMessage(MESSAGE_SEARCH_NONE);
		}
	}

	/**
	 * This operation checks if the parser returned an error.
	 * 
	 * @param ID   The ID from parser.
	 * @return     True if error, false otherwise.
	 */
	//@author A0088751U
	private boolean checkParserError(String ID){
		return ID.equals(ERROR_UNKNOWN) || 
				ID.equals(ERROR_WRONG_COMMAND) || 
				ID.equals(ERROR_WRONG_PARAM);
	}

	/**
	 * This operation creates the task containing the error message from Parser.
	 * @param ID   The ID of the error from Parser.
	 * @return     A task with the message in the task name.
	 */
	//@author A0088751U
	private Task createErrorMessage(String ID) {
		switch(ID) {			
		case ERROR_WRONG_COMMAND :
			return createMessage(MESSAGE_ERROR_WRONG_COMMAND);

		case ERROR_WRONG_PARAM :
			return createMessage(MESSAGE_ERROR_WRONG_PARAM);

		default :
			return createMessage(MESSAGE_ERROR_UNKNOWN);
		}
	}

}
