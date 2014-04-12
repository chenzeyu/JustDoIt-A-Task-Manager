package parser.JustDoIt;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This class is the parser.
 * The parser has a public method getCommand that takes in the String input by the user
 * and extracts all the necessary information from it.
 * 
 * @author Choo Cheng Mun Paulina
 */
public class Parser {

	// This is the String array that will be returned with the information extracted from the user's command
	private static String[] dataPacket;

	// This is the size of the dataPacket
	private static final int DATAPACKET_SIZE = 6;

	// These are the locations of the different information in dataPacket
	private static final int LOCATION_COMMAND_ID = 0;
	private static final int LOCATION_TASK_NAME = 1;
	private static final int LOCATION_START_TIME = 2;
	private static final int LOCATION_END_TIME = 3;
	private static final int LOCATION_TAG = 4;
	private static final int LOCATION_OTHERS = 5;

	// These are the lists of user inputs that will be accepted for extra information
	private static final List<String> INPUT_START_TIME = Arrays.asList("from", "start", "at");
	private static final List<String> INPUT_END_TIME = Arrays.asList("by", "end", "to");
	private static final List<String> INPUT_NEW_NAME = Arrays.asList("name", "description");
	private static final List<String> INPUT_ALL = Arrays.asList("all", "everything");
	private static final List<String> INPUT_COMPLETE = Arrays.asList("complete", "completed", "finish", "finished", "done");
	private static final List<String> INPUT_PENDING = Arrays.asList("pending");
	private static final List<String> INPUT_UNFINISHED = Arrays.asList("unfinished");
	private static final List<String> INPUT_HASHTAG = Arrays.asList("#");
	private static final List<String> INPUT_THIS_WEEK = Arrays.asList("this week");

	// These are the command types
	private static final String TYPE_ADD = "typeAdd";
	private static final String TYPE_DELETE = "typeDelete";
	private static final String TYPE_MODIFY = "typeModify";
	private static final String TYPE_DISPLAY = "typeDisplay";
	private static final String TYPE_SEARCH = "typeSearch";
	private static final String TYPE_UNDO = "typeUndo";
	private static final String TYPE_ERROR = "typeError";
	private static final String TYPE_EXIT = "typeExit";

	// These are the command IDs for the different commands
	private static final String ID_ADD = "add";
	private static final String ID_DELETE_NAME = "deleteName";
	private static final String ID_DELETE_NUMBER = "deleteNum";
	private static final String ID_DELETE_ALL = "deleteAll";
	private static final String ID_DELETE_COMPLETE = "deleteCompleted";
	private static final String ID_UPDATE_TASK = "updateTask";
	private static final String ID_UPDATE_TASK_INDEX = "updateTaskIndex";
	private static final String ID_UPDATE_STATUS = "updateStatus";
	private static final String ID_UPDATE_DONE = "done";
	private static final String ID_SEARCH_KEYWORD = "searchKey";
	private static final String ID_SEARCH_TIME = "searchTime";
	private static final String ID_SEARCH_THIS_WEEK = "searchThisWeek";
	private static final String ID_SEARCH_TIME_PERIOD = "searchPeriod";
	private static final String ID_SEARCH_TAG = "searchTag";
	private static final String ID_DISPLAY_ALL = "displayAll";
	private static final String ID_DISPLAY_PENDING = "displayPending";
	private static final String ID_DISPLAY_PENDING_NUM = "displayPendingNum";
	private static final String ID_DISPLAY_UNFINISHED = "displayUnfinished";
	private static final String ID_UNDO = "undo";
	private static final String ID_EXIT = "exitProg";

	// These are the IDs for errors from the parser
	private static final String ERROR_WRONG_COMMAND = "errorCommand";
	private static final String ERROR_WRONG_PARAM = "errorParam";
	private static final String ERROR_UNKNOWN = "errorUnknown";

	/**
	 * This operation extracts all necessary information from the user's input.
	 * 
	 * @param userCommand   The command input by the user.
	 * @return dataPacket   A String[] with all information.
	 */
	//@author A0088751U
	public static String[] getCommand(String userCommand){
		assert userCommand != null;
		dataPacket = new String[DATAPACKET_SIZE];

		String firstWord = getFirstWord(userCommand);
		String commandInfo = removeFirstWord(userCommand);
		String commandType = TypeParser.getCommandType(firstWord);

		switch(commandType) {
		case TYPE_ADD :
			extractAdd(commandInfo);
			break;

		case TYPE_DELETE :
			extractDelete(commandInfo);
			break;

		case TYPE_MODIFY :
			extractModify(commandInfo);
			break;

		case TYPE_DISPLAY :
			extractDisplay(commandInfo);
			break;

		case TYPE_SEARCH :
			extractSearch(commandInfo);
			break;

		case TYPE_UNDO :
			extractUndo(commandInfo);
			break;

		case TYPE_EXIT :
			extractExit(commandInfo);
			break;

		case TYPE_ERROR :
			dataPacket[LOCATION_COMMAND_ID] = ERROR_WRONG_COMMAND;
			break;

		default :
			assert false;
			dataPacket[LOCATION_COMMAND_ID] = ERROR_UNKNOWN;
		}

		return dataPacket;
	}

	/**
	 * This operation extracts the information needed for Add.
	 * 
	 * @param commandInfo   The user input without the add command.
	 */
	//@author A0088751U
	private static void extractAdd(String commandInfo) {		
		if(commandInfo == null || commandInfo.equals("")){
			dataPacket[LOCATION_COMMAND_ID] = ERROR_WRONG_PARAM;
		} else {
			dataPacket[LOCATION_COMMAND_ID] = ID_ADD;
			getAllInfo(commandInfo);
		}
	}

	/**
	 * This operation gets all information from the command.
	 * This includes times, new names and tags.
	 * 
	 * @param commandInfo   The user input without the add command.
	 */
	//@author A0088751U
	private static void getAllInfo(String commandInfo) {
		String command;
		String withoutCommand;
		
		Scanner in = new Scanner(commandInfo);
		in.useDelimiter(" \\\\");
		String next = in.next();

		dataPacket[LOCATION_TASK_NAME] = next;

		while(in.hasNext()) {
			next = in.next();
			command = getFirstWord(next);
			withoutCommand = removeFirstWord(next);
			
			if(isInList(INPUT_NEW_NAME, command)) {
				dataPacket[LOCATION_OTHERS] = withoutCommand;
			} else if(isInList(INPUT_START_TIME, command)) {
				try {
					String startTime = TimeParser.getDate(withoutCommand);
					dataPacket[LOCATION_START_TIME] = startTime;
				} catch (IllegalArgumentException e) {
					dataPacket[LOCATION_COMMAND_ID] = ERROR_WRONG_PARAM;
					break;
				}
			} else if(isInList(INPUT_END_TIME, command)) {
				try {
					String endTime = TimeParser.getDate(withoutCommand);
					dataPacket[LOCATION_END_TIME] = endTime;
				} catch (IllegalArgumentException e) {
					dataPacket[LOCATION_COMMAND_ID] = ERROR_WRONG_PARAM;
					break;
				}
			} else if(isInList(INPUT_HASHTAG, command)) {
				dataPacket[LOCATION_TAG] = withoutCommand;
			}
		}

		in.close();
	}

	/**
	 * This operation extracts the information needed for Delete.
	 * 
	 * @param commandInfo   The user input without the delete command.
	 */
	//@author A0088751U
	private static void extractDelete(String commandInfo) {
		boolean isInt = isInt(commandInfo);

		if(commandInfo == null || commandInfo.equals("")) {
			dataPacket[LOCATION_COMMAND_ID] = ERROR_WRONG_PARAM;
		} else if(isInt) {
			dataPacket[LOCATION_COMMAND_ID] = ID_DELETE_NUMBER;
			dataPacket[LOCATION_OTHERS] = commandInfo;
		} else if(isInList(INPUT_ALL, commandInfo)) {
			dataPacket[LOCATION_COMMAND_ID] = ID_DELETE_ALL;
		} else if(isInList(INPUT_COMPLETE, commandInfo)) {
			dataPacket[LOCATION_COMMAND_ID] = ID_DELETE_COMPLETE;
		} else {
			dataPacket[LOCATION_COMMAND_ID] = ID_DELETE_NAME;
			dataPacket[LOCATION_OTHERS] = commandInfo;
		}
	}

	/**
	 * This operation extracts the information needed for Modify.
	 * 
	 * @param commandInfo   The user input without the modify command.
	 */
	//@author A0088751U
	private static void extractModify(String commandInfo) {
		if(commandInfo == null || commandInfo.equals("")) {
			dataPacket[LOCATION_COMMAND_ID] = ERROR_WRONG_PARAM;
		} else if(isInt(commandInfo)) {
			dataPacket[LOCATION_COMMAND_ID] = ID_UPDATE_DONE;
			dataPacket[LOCATION_OTHERS] = commandInfo;
		} else {
			String command = getFirstWord(commandInfo);
			
			if(isInList(INPUT_COMPLETE, command)) {
				dataPacket[LOCATION_COMMAND_ID] = ID_UPDATE_STATUS;
				dataPacket[LOCATION_TASK_NAME] = removeFirstWord(commandInfo);
				dataPacket[LOCATION_OTHERS] = command;
			} else {
				dataPacket[LOCATION_COMMAND_ID] = ID_UPDATE_TASK;
				getAllInfo(commandInfo);
				
				if(isInt(dataPacket[LOCATION_TASK_NAME])) {
					dataPacket[LOCATION_COMMAND_ID] = ID_UPDATE_TASK_INDEX;
				}
			}
		}
	}

	/**
	 * This operation extracts the information needed for Display.
	 * 
	 * @param commandInfo   The user input without the display command.
	 */
	//@author A0088751U
	private static void extractDisplay(String commandInfo) {
		if(commandInfo == null || commandInfo.equals("")) {
			dataPacket[LOCATION_COMMAND_ID] = ERROR_WRONG_PARAM;
		} else if(isInList(INPUT_ALL, commandInfo)) {
			dataPacket[LOCATION_COMMAND_ID] = ID_DISPLAY_ALL;
		} else if(isInList(INPUT_UNFINISHED, commandInfo)) {
			dataPacket[LOCATION_COMMAND_ID] = ID_DISPLAY_UNFINISHED;
		} else if(isInList(INPUT_PENDING, getFirstWord(commandInfo))) {
			String withoutCommand = removeFirstWord(commandInfo);
			boolean isInt = isInt(withoutCommand);

			if(isInt) {
				dataPacket[LOCATION_COMMAND_ID] = ID_DISPLAY_PENDING_NUM;
				dataPacket[LOCATION_OTHERS] = withoutCommand;
			} else {
				dataPacket[LOCATION_COMMAND_ID] = ID_DISPLAY_PENDING;
			}
		} else {
			dataPacket[LOCATION_COMMAND_ID] = ERROR_WRONG_PARAM;
		}

	}

	/**
	 * This operation extracts the information needed for Search.
	 * 
	 * @param commandInfo   The user input without the search command.
	 */
	//@author A0088751U
	private static void extractSearch(String commandInfo) {
		if(commandInfo == null || commandInfo.equals("")) {
			dataPacket[LOCATION_COMMAND_ID] = ERROR_WRONG_PARAM;
		} else if(TimeParser.checkDate(commandInfo)) {
			try {
				String searchDate = TimeParser.getDate(commandInfo);
				dataPacket[LOCATION_COMMAND_ID] = ID_SEARCH_TIME;
				dataPacket[LOCATION_OTHERS] = searchDate;
			} catch (IllegalArgumentException e) {
				dataPacket[LOCATION_COMMAND_ID] = ERROR_WRONG_PARAM;
			}
		} else if (isInList(INPUT_THIS_WEEK, commandInfo)) {
			dataPacket[LOCATION_COMMAND_ID] = ID_SEARCH_THIS_WEEK;
		} else {
			if(commandInfo.contains("\\")) {
				getAllInfo("dummy name " + commandInfo);
				if(commandInfo.contains("\\#")) {
					dataPacket[LOCATION_COMMAND_ID] = ID_SEARCH_TAG;
				} else {
					if(dataPacket[LOCATION_START_TIME] == null || 
							dataPacket[LOCATION_END_TIME] == null) {
						dataPacket[LOCATION_COMMAND_ID] = ERROR_WRONG_PARAM;
					} else {
						dataPacket[LOCATION_COMMAND_ID] = ID_SEARCH_TIME_PERIOD;
					}
				}
			} else {
				dataPacket[LOCATION_COMMAND_ID] = ID_SEARCH_KEYWORD;
				dataPacket[LOCATION_OTHERS] = commandInfo;
			}
		}
	}

	/**
	 * This operation extracts the information needed for Undo.
	 * 
	 * @param commandInfo   The user input without the undo command.
	 */
	//@author A0088751U
	private static void extractUndo(String commandInfo) {
		dataPacket[LOCATION_COMMAND_ID] = ID_UNDO;
	}

	/**
	 * This operation extracts the information needed for exit.
	 * 
	 * @param commandInfo   The user input without the exit command.
	 */
	//@author A0088751U
	private static void extractExit(String commandInfo) {
		dataPacket[LOCATION_COMMAND_ID] = ID_EXIT;
	}

	/**
	 * This operation gets the first word in the input.
	 * 
	 * @param userInput   The user's input.
	 * @return firstWord  The first word of the input.
	 */
	//@author A0088751U
	private static String getFirstWord(String userInput) {
		String firstWord = userInput.trim().split(" ")[0];		
		return firstWord;
	}

	/**
	 * This operation removes the first word of the input.
	 * 
	 * @param userInput              The user's input.
	 * @return inputWithoutCommand   The input without first word.
	 */
	//@author A0088751U
	private static String removeFirstWord(String userInput) {
		String firstWord = getFirstWord(userInput);
		
		if(firstWord.equals(userInput)) {
			return null;
		} else {
			String inputWithoutCommand = userInput.replaceFirst(firstWord, "").trim();
			return inputWithoutCommand;
		}
	}

	/**
	 * This operation checks whether the command is in a list.
	 * 
	 * @param list      The list of valid commands.
	 * @param command   The command entered.
	 * @return          True if valid command, false otherwise.
	 */
	//@author A0088751U
	private static boolean isInList(List<String> list, String command){
		if(command == null) {
			return false;
		}

		for(String i : list) {
			if(i.equalsIgnoreCase(command)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * This operation checks if a string is an integer.
	 * 
	 * @param input   The input string.
	 * @return        True if integer, false otherwise.
	 */
	//@author A0088751U
	private static boolean isInt(String input){
		try {
			Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}	

}
