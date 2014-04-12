package parser.JustDoIt;

import java.util.Arrays;
import java.util.List;

/**
 * This class is the type parser.
 * The parser has a public method getCommandType that takes in the String command input by the user
 * and returns the type of command it is.
 * 
 * @author Choo Cheng Mun Paulina
 */
public class TypeParser {
	
	// These are the lists of user inputs that will be accepted for commands
	private static final List<String> INPUT_ADD = Arrays.asList("add", "create", "new");
	private static final List<String> INPUT_MODIFY = Arrays.asList("modify", "change", "update", "done");
	private static final List<String> INPUT_DELETE = Arrays.asList("delete", "remove");
	private static final List<String> INPUT_DISPLAY = Arrays.asList("display", "show");
	private static final List<String> INPUT_SEARCH = Arrays.asList("search", "find");
	private static final List<String> INPUT_UNDO = Arrays.asList("undo");
	private static final List<String> INPUT_EXIT = Arrays.asList("exit", "quit", "close");
	
	// These are the command types
	private static final String TYPE_ADD = "typeAdd";
	private static final String TYPE_DELETE = "typeDelete";
	private static final String TYPE_MODIFY = "typeModify";
	private static final String TYPE_DISPLAY = "typeDisplay";
	private static final String TYPE_SEARCH = "typeSearch";
	private static final String TYPE_UNDO = "typeUndo";
	private static final String TYPE_ERROR = "typeError";
	private static final String TYPE_EXIT = "typeExit";
	
	/**
	 * This operation gets the type of command.
	 * 
	 * @param command   The command entered.
	 * @return          Command type.
	 */
	//@author A0088751U
	public static String getCommandType(String command){
		if(checkInList(INPUT_ADD, command)) {
			return TYPE_ADD;
		} else if(checkInList(INPUT_MODIFY, command)) {
			return TYPE_MODIFY;
		} else if(checkInList(INPUT_DELETE, command)) {
			return TYPE_DELETE;
		} else if(checkInList(INPUT_DISPLAY, command)) {
			return TYPE_DISPLAY;
		} else if(checkInList(INPUT_SEARCH, command)) {
			return TYPE_SEARCH;
		} else if(checkInList(INPUT_UNDO, command)) {
			return TYPE_UNDO;
		} else if(checkInList(INPUT_EXIT, command)) {
			return TYPE_EXIT;
		} else {
			return TYPE_ERROR;
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
	private static boolean checkInList(List<String> list, String command){
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

}
