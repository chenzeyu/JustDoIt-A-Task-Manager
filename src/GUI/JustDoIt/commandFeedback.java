package GUI.JustDoIt;

/*This is an implementation of command helper message*/
public class commandFeedback {

	private final int IDLocation = 1;
	private final String ADD = "add";
	private final String NEW = "new";
	private final String CREATE = "create";
	private final String DELETE = "delete";
	private final String REMOVE = "remove";
	private final String UPDATE = "update";
	private final String MODIFY = "modify";
	private final String CHANGE = "change";
	private final String DISPLAY = "display";
	private final String SHOW = "show";
	private final String SEARCH = "search";
	private final String FIND = "find";
	private final String QUIT = "quit";
	private final String CLOSE = "close";
	private final String EXIT = "exit";
	private final String MESSAGE_ADD = "%1$s <task name> \\from <start time> \\to <end time> \\# <tag word>";
	private final String MESSAGE_DELETE_ID = "%1$s <task ID> (you can only do this after a display or search.)";
	private final String MESSAGE_DELETE_KEYWORD = "%1$s <keyword> (this will delete all tasks contain the keyword.)";
	private final String MESSAGE_DELETE_COMPLETED = "%1$s completed (delete all completed tasks.)";
	private final String MESSAGE_DELETE_ALL = "%1$s all (delete all the tasks.)";
	private final String MESSAGE_DONE_INDEX = "done <task ID> (mark the task as finished.)";
	private final String MESSAGE_UPDATE = "%1$s <name> \\name(\\from, \\to or \\#) <new infomation>";
	private final String MESSAGE_UPDATE_INDEX = "%1$s <task ID> \\name(\\from, \\to or \\#) <new infomation>";
	private final String MESSAGE_DISPLAY_ALL = "%1$s all (display all the tasks.)";
	private final String MESSAGE_DISPLAY_PENDING = "%1$s pending <number>(optional) (display n pending tasks sorted by urgency.)";
	private final String MESSAGE_DISPLAY_UNFINISHED = "%1$s unfinished (display tasks which are expired but unfinished.)";
	private final String MESSAGE_SEARCH_KEYWORD = "%1$s <keyword> (search tasks with the given keyword.)";
	private final String MESSAGE_SEARCH_TIME = "%1$s <time> (recommended format: DD-MM-YYYY)";
	private final String MESSAGE_SEARCH_PERIOD = "%1$s \\from <start time> \\to <end time> (recommended format: DD-MM-YYYY)";
	private final String MESSAGE_SEARCH_TAG = "%1$s \\# <tag word>";
	private final String MESSAGE_SEARCH_WEEK = "%1$s this week (search all tasks within this week)";
	private final String MESSAGE_HELP = "help (show HELP documentation)";
	private final String MESSAGE_QUIT = "%1$s (close JustDoIt)";

	private static commandFeedback instance = new commandFeedback();

	// @author A0091683X
	private boolean isNumericCommand(String command) {
		assert command.trim().split("\\s").length > 1;
		String ID = command.trim().split("\\s")[IDLocation];
		try {
			Integer.parseInt(ID);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

	private boolean isSingleCommand(String command) {
		return command.trim().split("\\s").length == 1;
	}

	public String onCommandChange(String text) {
		String newText = text.toLowerCase();

		// for delete commands
		if (newText.startsWith("de") && isSingleCommand(newText)) {
			return String.format(MESSAGE_DELETE_ID, DELETE);
		} else if (newText.startsWith("r") && isSingleCommand(newText)) {
			return String.format(MESSAGE_DELETE_ID, REMOVE);
		} else if (newText.startsWith("de") && isNumericCommand(newText)) {
			return String.format(MESSAGE_DELETE_ID, DELETE);
		} else if (newText.startsWith("r") && isNumericCommand(newText)) {
			return String.format(MESSAGE_DELETE_ID, REMOVE);
		} else if (newText.startsWith(DELETE) && !(isNumericCommand(newText))
				&& !newText.trim().split("\\s")[IDLocation].startsWith("a")
				&& !newText.trim().split("\\s")[IDLocation].startsWith("c")) {
			return String.format(MESSAGE_DELETE_KEYWORD, DELETE);
		} else if (newText.startsWith(REMOVE) && !(isNumericCommand(newText))
				&& !(newText.trim().split("\\s")[IDLocation].startsWith("a"))
				&& !(newText.trim().split("\\s")[IDLocation].startsWith("c"))) {
			return String.format(MESSAGE_DELETE_KEYWORD, REMOVE);
		} else if (newText.startsWith(DELETE) && !(isNumericCommand(newText))
				&& newText.trim().split("\\s")[IDLocation].startsWith("a")) {
			return String.format(MESSAGE_DELETE_ALL, DELETE);
		} else if (newText.startsWith(REMOVE) && !(isNumericCommand(newText))
				&& newText.trim().split("\\s")[IDLocation].startsWith("a")) {
			return String.format(MESSAGE_DELETE_ALL, REMOVE);
		} else if (newText.startsWith(DELETE) && !(isNumericCommand(newText))
				&& newText.trim().split("\\s")[IDLocation].startsWith("c")) {
			return String.format(MESSAGE_DELETE_COMPLETED, DELETE);
		} else if (newText.startsWith(REMOVE) && !(isNumericCommand(newText))
				&& newText.trim().split("\\s")[IDLocation].startsWith("c")) {
			return String.format(MESSAGE_DELETE_COMPLETED, REMOVE);
		}

		// for update
		else if (newText.startsWith("do")) {
			return MESSAGE_DONE_INDEX;
		} else if (newText.startsWith("u") && isSingleCommand(newText)) {
			return String.format(MESSAGE_UPDATE, UPDATE);
		} else if (newText.startsWith("u") && isNumericCommand(newText)) {
			return String.format(MESSAGE_UPDATE_INDEX, UPDATE);
		} else if (newText.startsWith("u") && !isNumericCommand(newText)) {
			return String.format(MESSAGE_UPDATE, UPDATE);
		} else if (newText.startsWith("ch") && isSingleCommand(newText)) {
			return String.format(MESSAGE_UPDATE, CHANGE);
		} else if (newText.startsWith("ch") && isNumericCommand(newText)) {
			return String.format(MESSAGE_UPDATE_INDEX, CHANGE);
		} else if (newText.startsWith("ch") && !isNumericCommand(newText)) {
			return String.format(MESSAGE_UPDATE, CHANGE);
		} else if (newText.startsWith("m") && isSingleCommand(newText)) {
			return String.format(MESSAGE_UPDATE, MODIFY);
		} else if (newText.startsWith("m") && isNumericCommand(newText)) {
			return String.format(MESSAGE_UPDATE_INDEX, MODIFY);
		} else if (newText.startsWith("m") && !isNumericCommand(newText)) {
			return String.format(MESSAGE_UPDATE, MODIFY);
		}

		// for display
		else if (newText.startsWith("d") && isSingleCommand(newText)) {
			return String.format(MESSAGE_DISPLAY_ALL, DISPLAY);
		} else if (newText.startsWith("sh") && isSingleCommand(newText)) {
			return String.format(MESSAGE_DISPLAY_ALL, SHOW);
		} else if (newText.startsWith("d")
				&& newText.trim().split("\\s")[IDLocation].startsWith("a")) {
			return String.format(MESSAGE_DISPLAY_ALL, DISPLAY);
		} else if (newText.startsWith("sh")
				&& newText.trim().split("\\s")[IDLocation].startsWith("a")) {
			return String.format(MESSAGE_DISPLAY_ALL, SHOW);
		} else if (newText.startsWith("d")
				&& newText.trim().split("\\s")[IDLocation].startsWith("p")) {
			return String.format(MESSAGE_DISPLAY_PENDING, DISPLAY);
		} else if (newText.startsWith("sh")
				&& newText.trim().split("\\s")[IDLocation].startsWith("p")) {
			return String.format(MESSAGE_DISPLAY_PENDING, SHOW);
		} else if (newText.startsWith("d")
				&& newText.trim().split("\\s")[IDLocation].startsWith("u")) {
			return String.format(MESSAGE_DISPLAY_UNFINISHED, DISPLAY);
		} else if (newText.startsWith("sh")
				&& newText.trim().split("\\s")[IDLocation].startsWith("u")) {
			return String.format(MESSAGE_DISPLAY_UNFINISHED, SHOW);
		} else if (newText.startsWith("d")) {
			return String.format(MESSAGE_DISPLAY_ALL, DISPLAY);
		} else if (newText.startsWith("sh")) {
			return String.format(MESSAGE_DISPLAY_ALL, SHOW);
		}

		// for search
		else if (newText.startsWith("search t")) {
			return String.format(MESSAGE_SEARCH_WEEK, SEARCH);
		}else if (newText.startsWith("s") && isSingleCommand(newText)) {
			return String.format(MESSAGE_SEARCH_KEYWORD, SEARCH);
		} else if (newText.startsWith("find t")) {
			return String.format(MESSAGE_SEARCH_WEEK, FIND);
		} else if (newText.startsWith("f") && isSingleCommand(newText)) {
			return String.format(MESSAGE_SEARCH_KEYWORD, FIND);
		} else if (newText.startsWith("s") && isNumericCommand(newText)) {
			return String.format(MESSAGE_SEARCH_TIME, SEARCH);
		} else if (newText.startsWith("f") && isNumericCommand(newText)) {
			return String.format(MESSAGE_SEARCH_TIME, FIND);
		} else if (newText.startsWith("s") && newText.contains("\\")) {
			return String.format(MESSAGE_SEARCH_PERIOD, SEARCH);
		} else if (newText.startsWith("f") && newText.contains("\\")) {
			return String.format(MESSAGE_SEARCH_PERIOD, FIND);
		} else if (newText.startsWith("s") && newText.contains("\\#")) {
			return String.format(MESSAGE_SEARCH_TAG, SEARCH);
		} else if (newText.startsWith("f") && newText.contains("\\#")) {
			return String.format(MESSAGE_SEARCH_TAG, FIND);
		} else if (newText.startsWith("s")) {
			return String.format(MESSAGE_SEARCH_KEYWORD, SEARCH);
		} else if (newText.startsWith("f")) {
			return String.format(MESSAGE_SEARCH_KEYWORD, FIND);
		}

		// for help
		else if (newText.startsWith("h")) {
			return MESSAGE_HELP;
		}

		// for quit
		else if (newText.startsWith("qu")) {
			return String.format(MESSAGE_QUIT, QUIT);
		} else if (newText.startsWith("cl")) {
			return String.format(MESSAGE_QUIT, CLOSE);
		} else if (newText.startsWith("ex")) {
			return String.format(MESSAGE_QUIT, EXIT);
		}

		// for add commands
		else if (newText.startsWith("a")) {
			return String.format(MESSAGE_ADD, ADD);
		} else if (newText.startsWith("n")) {
			return String.format(MESSAGE_ADD, NEW);
		} else if (newText.startsWith("c")) {
			return String.format(MESSAGE_ADD, CREATE);
		}

		return "";
	}

	public static commandFeedback getInstance() {
		return instance;
	}
}
