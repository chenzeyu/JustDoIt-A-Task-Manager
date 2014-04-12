package GUI.JustDoIt;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.SWT;
import logic.JustDoIt.*;
import task.JustDoIt.*;
import ui.JustDoIt.UIswitch;
import GUI.JustDoIt.Helper;

/*This is the implementation of JustDoIt GUI.*/

// @author A0091646Y 
public class GUI {

	/* Control variables */
	private static boolean isExit;
	private static boolean isClear;
	private static Logic logicObject = Logic.getInstance();
	private static UIswitch uiSwitch;
	private static String userCommand = null;
	private static int currentWordCount = 0;
	private List<Task> returnMessage;
	private static Stack<String> prevCommand = new Stack<String>();
	private static Stack<String> nextCommand = new Stack<String>();

	/* Display parameters */
	private static int inputCommandFontSize;
	private static int displayAreaFontSize;
	private static int displayHistoryFontSize;
	private static int circularButtonOffsetX = 17;
	private static int circularButtonOffsetY = 17;
	private static int circularButtonRadius = 11;
	private static int circularButtonFormData = 34;

	/* File path */
	private static String settingFilePath = "settings.ini";
	private static String defaultBackground = "/GUI/JustDoIt/image/Rainbow world.png";

	/* Formated command and feedback */
	private static final List<String> INPUT_EXIT = Arrays.asList("exit",
			"quit", "close");
	private static final List<String> INPUT_CLEAR = Arrays.asList("clear",
			"refresh", "reset");
	private static final String DISPLAY_PENDING = "display pending 3";
	private static final String MESSAGE_WELCOME = "Welcome to JustDoIt.";
	private static final String MESSAGE_TRAY = "In the system tray, right click on the icon to control the program.";
	private static final String MESSAGE_MOTO = "Your ideal scheduler";
	private static final String MESSAGE_MINIMIZED = "JustDoIt has been minimized to the system tray.";
	private static final String MESSAGE_HELP = "Click to see the help document.";

	/* GUI components set up */
	public final Shell shell;
	public final Display display = new Display();
	public static StyledText displayer;
	public final StyledText commandHistory;
	public final StyledText commandHistoryTitle;
	public final Text commandLine;
	private Image backgroundImage;
	private Image helperBackgroundImage;
	private String backgroundImageName;
	private String helperBackgroundImagePath = "/GUI/JustDoIt/image/helper_skin.png";
	private Helper helper;
	private commandFeedback feedback = commandFeedback.getInstance();

	/* Font settings */
	private final Font displayFont = new Font(display, "Arial",
			displayAreaFontSize, SWT.BOLD);

	private final Font feedBackFont = new Font(display, "Times New Roman",
			displayHistoryFontSize, SWT.ITALIC);

	private final Font displayTimeFont = new Font(display, "Times New Roman",
			displayAreaFontSize, SWT.NORMAL);

	private final Font commandGridFont = new Font(display, "Arial",
			inputCommandFontSize, SWT.NORMAL);

	private final Font commandHistoryFont = new Font(display, "Arial",
			displayHistoryFontSize, SWT.BOLD);

	private final Font commandHistoryTitleFont = new Font(display, "Arial",
			displayHistoryFontSize, SWT.NORMAL);

	/* Color Settings */
	Color skyBlue = new Color(null, 43, 72, 153);
	Color navyBlue = new Color(null, 36, 64, 128);
	Color white = new Color(null, 255, 255, 255);
	Color black = new Color(null, 0, 0, 0);
	Color ivory = new Color(null, 255, 255, 240);
	Color gold = new Color(null, 255, 215, 0);
	Color lightBlue = new Color(null, 0, 191, 255);
	Color orange = new Color(null, 255, 127, 0);
	Color grey = new Color(null, 200, 200, 200);
	Color red = new Color(null, 255, 0, 0);
	Color azure = new Color(null, 224, 238, 238);

	// @author A0091646Y
	public GUI() throws IOException {

		/* Initialize the shell */
		shell = new Shell(display, SWT.NO_TRIM);
		backgroundImage = new Image(display,
				GUI.class.getResourceAsStream(getBackgroundImageName()));
		backgroundImageName = new String(getBackgroundImageName());
		setShell();

		/* Make the shell able to be dragged */
		enableDrag();

		/* Set up display area and input area */
		displayer = new StyledText(shell, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		commandLine = new Text(shell, SWT.NONE | SWT.BORDER | SWT.SINGLE);
		commandHistory = new StyledText(shell, SWT.WRAP | SWT.V_SCROLL
				| SWT.BORDER);
		commandHistoryTitle = new StyledText(shell, SWT.WRAP);

		/* Add control buttons */
		initializeControlButton(display);
		initializeBackgroundButton(display);
		initializeHelpButton();
		// initializeClearCommandHistoryButton();

		/* Add Input and Output windows */
		initializeCommandGrid();
		initializeDisplay();
		initializeHistoryTitle();
		initializeHistory();
		initializeCalendar();

		/* Launch the minimize to system tray function */
		initializeMinTray();

		/* Set keyboard shortcuts */
		setHotKeys(display);

		/* Launch welcome window; after that launch the shell */
		initializeWelcome(display);

		/* Add command helper */
		initializeHelper();

		/* Adapt font size to current monitor resolution */
		setFontSize();

		/* Set on-open display settings */
		displayWelcome();
		displayPending();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private void initializeHelper() {

		helperBackgroundImage = new Image(display,
				ResourceLoader.load(helperBackgroundImagePath));
		display.addFilter(SWT.KeyDown, new Listener() {
			public void handleEvent(Event e) {
				try {
					int offsetX = 26;
					int offsetY = 527;
					helper = Helper.getInstance(shell, display,
							helperBackgroundImage, gold,
							inputCommandFontSize + 1);
					helper.setLocation(shell.getLocation().x + offsetX,
							shell.getLocation().y + offsetY);
					onTextChangedListener(helper);
					commandLine.setFocus();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void onTextChangedListener(final Helper helper) {

		commandLine.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text text = (Text) e.widget;
				if (!(text.getText().trim().equalsIgnoreCase("")))

				{
					helper.setText(feedback.onCommandChange(text.getText()));
					helper.show();
				} else {
					helper.hide();
				}
			}
		});
	}

	public void setShell() {

		Region region = getBackRegionFromImage(display, backgroundImageName);

		/* Capture the transparent regions and remove them */
		Region corner = handleTransparenceRegion(backgroundImage, 0, 0);
		region.subtract(corner);
		shell.setRegion(region);

		/* Set shell size and background according to the region */
		Rectangle size = region.getBounds();
		shell.setSize(size.width, size.height);

		/* Load background */
		ImageLoader loader = new ImageLoader();
		ImageData[] imageData = loader.load(ResourceLoader
				.load(backgroundImageName));
		Image image = new Image(null, imageData[0]);
		shell.setBackgroundImage(image);
		shell.setBackgroundMode(SWT.INHERIT_FORCE);

		/* Set icon */
		Image shellIcon = new Image(Display.getCurrent(),
				GUI.class.getResourceAsStream("/GUI/JustDoIt/image/icon.png"));
		shell.setImage(shellIcon);

		/* Put the shell in center of screen */
		center(shell);
	}

	/**
	 * This method finds out transparent region in an image
	 * 
	 * @param image
	 *            is the target image
	 * @param offsetX
	 *            is the distance offset on x axis
	 * @param offsetY
	 *            is the distance offset on y axis
	 * @return Region is the found transparent region
	 */
	public Region handleTransparenceRegion(Image image, int offsetX, int

	offsetY) {

		Region region = new Region();
		final ImageData imageData = image.getImageData();
		if (imageData.alphaData != null) {
			Rectangle pixel = new Rectangle(0, 0, 1, 1);
			for (int y = 0; y < imageData.height; y++) {
				for (int x = 0; x < imageData.width; x++) {
					if (imageData.getAlpha(x, y) != 255) {
						pixel.x = imageData.x + x +

						offsetX;
						pixel.y = imageData.y + y +

						offsetY;
						region.add(pixel);
					}
				}
			}
		}
		return region;
	}

	/* This method makes the shell be able to be dragged by mouse */
	private void enableDrag() {

		Listener listener = new Listener() {
			int startX, startY;

			public void handleEvent(Event e) {
				if (e.type == SWT.MouseDown && e.button == 1) {
					startX = e.x;
					startY = e.y;
				}

				if (e.type == SWT.MouseMove && (e.stateMask & SWT.BUTTON1) != 0) {
					Point p = shell.toDisplay(e.x, e.y);
					p.x -= startX;
					p.y -= startY;
					shell.setLocation(p);
				}
			}
		};
		shell.addListener(SWT.MouseDown, listener);
		shell.addListener(SWT.MouseMove, listener);
	}

	/**
	 * This method records user's choice of background image into a local
	 * setting file
	 * 
	 * @param backgroundName
	 *            is the file name of the background image
	 */
	public void saveBackgroungImage(String backgroundName) {

		try {
			FileOutputStream settingUpdater = new FileOutputStream(
					settingFilePath);
			settingUpdater.write(new String("").getBytes());
			BufferedWriter settingSaver = new BufferedWriter(
					new OutputStreamWriter(settingUpdater));
			settingSaver.write(backgroundName);
			settingSaver.close();
			settingUpdater.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Read the choice of background image from local recorder file */
	public String getBackgroundImageName() {

		File f = new File(settingFilePath);
		if (!f.exists()) {
			try {
				f.createNewFile();
				saveBackgroungImage(defaultBackground);
				return defaultBackground;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				BufferedReader br = new BufferedReader(new FileReader(
						settingFilePath));
				String backgroundImageName = new String();
				backgroundImageName = br.readLine();
				br.close();
				return backgroundImageName;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return defaultBackground;
	}

	/* Set up the command input grid */
	public void initializeCommandGrid() {

		commandLine.setForeground(black);
		commandLine.setBackground(ivory);
		commandLine.setFont(commandGridFont);
		commandLine.setFocus();
		FormData commandData = new FormData(810, 21);
		commandData.left = new FormAttachment(3);
		commandData.bottom = new FormAttachment(96);
		commandLine.setLayoutData(commandData);

		/* Add listeners for command helper, help document and command trace */
		commandLine.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {

				if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN) {
					commandTrace(e);
				} else if (e.keyCode == SWT.CR
						&& !(userCommand = commandLine.getText())
								.equalsIgnoreCase("")) {
					prevCommand.push(userCommand);
					isExit = checkExit(userCommand);
					isClear = checkClear(userCommand);
					commandLine.setText("");
					addBoldTextInCommandHistory(commandHistory, userCommand);
					if (isExit) {
						System.exit(0);
					}
					if (isClear) {
						clearScreen();
					}
					if (userCommand.trim().equalsIgnoreCase("HELP")) {
						if (Desktop.isDesktopSupported()) {
							Desktop desktop = Desktop.getDesktop();
							InputStream resource = getClass()
									.getResourceAsStream(
											"/GUI/JustDoIt/user_manual.pdf");
							try {
								File file = File.createTempFile("user_manual",
										".pdf");
								file.deleteOnExit();
								OutputStream out = new FileOutputStream(file);
								try {
									int stream = resource.read();
									while (stream != -1) {
										out.write(stream);
										stream = resource.read();
									}
								} finally {
									out.close();
								}
								desktop.open(file);
								resource.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					} else {
						returnMessage = uiSwitch.executeCommand(userCommand);
						displayFeedback(returnMessage);
					}
				}
			}
		});
	}

	/* This method enables command tracing using two stacks */
	private void commandTrace(KeyEvent e) {
		if (e.keyCode == SWT.ARROW_UP) {
			commandLine.clearSelection();
			if (prevCommand.size() >= 1) {
				String temp = prevCommand.pop();
				nextCommand.push(temp);
				commandLine.setText(temp);
				commandLine.setSelection(0);
			} else
				commandLine.setText("");
		} else if (e.keyCode == SWT.ARROW_DOWN) {
			commandLine.clearSelection();
			if (nextCommand.size() >= 1) {
				String temp = nextCommand.pop();
				prevCommand.push(temp);
				commandLine.setText(temp);
				commandLine.setSelection(0);
			} else
				commandLine.setText("");
		}
	}

	/**
	 * This operation display feedback messages with designed format
	 * 
	 * @param returnMessage
	 *            is the feedback messages
	 */
	private void displayFeedback(List<Task> returnMessage) {

		if (returnMessage == null || returnMessage.isEmpty()) {
			// do nothing
		} else {
			display(returnMessage.get(0).getTaskName());
			returnMessage.remove(0);

			/* Clear display area before a list of task is to be displayed */
			if (!logicObject.shouldRefresh) {
				clearScreen();
				display(returnMessage);
			}

			/* Refresh display area and display tasks after any modification */
			else {
				logicObject.resetShouldRefresh();
				display(returnMessage);
				clearScreen();
				returnMessage = uiSwitch.executeCommand("display all");
				/* If at least 1 task is found, display it */
				if (returnMessage.size() > 1)
					displayFeedback(returnMessage);
			}
		}
	}

	/**
	 * This operation checks if the user has requested to exit
	 * 
	 * @param userCommand
	 *            is the ID returned by the parser
	 * @return true if exit request, false otherwise
	 */
	private static boolean checkExit(String userCommand) {

		return checkInList(INPUT_EXIT, userCommand.trim());
	}

	/**
	 * This operation checks if the user has requested to clear screen
	 * 
	 * @param userCommand
	 *            is the ID returned by the parser
	 * @return true if clear screen request, false otherwise
	 */
	private static boolean checkClear(String userCommand) {

		return checkInList(INPUT_CLEAR, userCommand.trim());
	}

	/**
	 * This operation checks whether the command is in a list
	 * 
	 * @param list
	 *            is the list of valid commands
	 * @param command
	 *            is the command entered
	 * @return true if valid command, false otherwise
	 */
	private static boolean checkInList(List<String> list, String command) {

		if (command == null) {
			return false;
		}
		for (String i : list) {
			if (i.equalsIgnoreCase(command)) {
				return true;
			}
		}
		return false;
	}

	/* Clear the display area, hide the scroll bar */
	private static void clearScreen() {

		displayer.setText("\n");
		currentWordCount = 0;
		displayer.getVerticalBar().setVisible(false);
	}

	/* Get current count of words in specific text window */
	private static int getCurrentWordCount(StyledText t) {

		return t.getText().length();
	}

	/* This operation displays a fixed number of pending tasks on start-up */
	private void displayPending() {

		List<Task> returnMessage = uiSwitch.executeCommand(DISPLAY_PENDING);
		if (returnMessage == null) {
			// do nothing
		} else {
			returnMessage.remove(0);
			display(returnMessage);
		}
	}

	/* This method displays welcome message every time when program starts */
	private void displayWelcome() {

		int welcomeStartLocation = getCurrentWordCount(displayer);
		displayer.append(MESSAGE_WELCOME);
		int welcomeLength = MESSAGE_WELCOME.length();

		StyleRange welcomeMessageStyleRange = new StyleRange(
				welcomeStartLocation, welcomeLength, white, null);

		welcomeMessageStyleRange.fontStyle = SWT.BOLD;
		displayer.setStyleRange(welcomeMessageStyleRange);
		displayer.append("\n\n");
	}

	/**
	 * This operation displays the message to the user
	 * 
	 * @param message
	 *            is the message returned by the Logic component
	 */
	public void display(String message) {

		/* Display all messages except "exact match found" since it is trivial */
		if (!message.equalsIgnoreCase("exact match found.")) {
			int wordCountBeforeMessage = getCurrentWordCount(commandHistory);
			int lineCountBeforeMessafe = commandHistory.getLineCount();
			commandHistory.append(" " + message);

			StyleRange feedbackStyleRange = new StyleRange(
					wordCountBeforeMessage, message.length() + 1, white, null);

			feedbackStyleRange.font = feedBackFont;
			commandHistory.setStyleRange(feedbackStyleRange);
			commandHistory.append("\n");

			/* Highlight error messages and warnings */
			if (message.contains("Error:") || message.contains("error:")) {
				commandHistory.setLineBackground(
						commandHistory.getLineCount() - 2, 1, red);
			} else if (message.contains("WARNING")) {
				commandHistory.setLineBackground(lineCountBeforeMessafe - 1,
						commandHistory.getLineCount() - lineCountBeforeMessafe,
						orange);
			}
		}
	}

	/**
	 * This operation appends bold text to a StyledText
	 * 
	 * @param target
	 *            is the target string to be displayed
	 */
	public void addBoldTextInCommandHistory(StyledText text, String target) {

		int startLocation = getCurrentWordCount(text);
		int messageLength = target.length();
		text.append(" " + target);
		text.append("\n");

		StyleRange messageStyleRange = new StyleRange(startLocation,
				messageLength + 1, white, null);

		messageStyleRange.font = commandHistoryFont;
		text.setStyleRange(messageStyleRange);
	}

	/**
	 * This operation displays tasks to the user
	 * 
	 * @param taskList
	 *            is the list of tasks to be displayed
	 */
	public void display(List<Task> taskList) {

		Task current;

		/* Parameters for styled texts display */
		int urgentTaskTimeLength;
		int urgentTaskNameLocation;
		int currentLineCountBeforeTime;
		int[] taggedWordLocation = new int[taskList.size()];

		/* Initialize the colorful texts parameters */
		for (int j = 0; j < taskList.size(); j++) {
			taggedWordLocation[j] = -1;
		}

		/* Explore all tasks in the list, start from 1 as index */
		for (int i = 1; i <= taskList.size(); i++) {
			current = taskList.get(i - 1);

			/* Urgent tasks: highlight index, display remaining time */
			if (current.isUrgent() != null) {

				/* Display index with a style */
				displayIndexWithStyle(i, current);

				/* get start location of the highlight text */
				int wordCountBeforeTaskName = getCurrentWordCount(displayer);
				displayer.append(current.taskName + " ");

				StyleRange taskNameStyleRange = new StyleRange(
						wordCountBeforeTaskName, current.taskName.length(),
						white, null);

				taskNameStyleRange.font = displayFont;
				displayer.setStyleRange(taskNameStyleRange);

				/* Record urgent task name start location and length offset */
				urgentTaskTimeLength = current.isUrgent().length();
				urgentTaskNameLocation = getCurrentWordCount(displayer);
				displayer.append(current.isUrgent());

				StyleRange urgentTaskStyleRange = new StyleRange(
						urgentTaskNameLocation, urgentTaskTimeLength, white,
						null);
				urgentTaskStyleRange.font = displayFont;
				displayer.setStyleRange(urgentTaskStyleRange);

				/* highlight task names contains search keyword,highlight */
				if (current.getKeyWord() != null) {

					highlightKeyword(current, wordCountBeforeTaskName);
				}
			}

			/* For normal tasks, check if they contain search keywords */
			else {

				displayIndexWithStyle(i, current);

				/* get start location of the highlight text */
				int wordCountBeforeTaskName = getCurrentWordCount(displayer);

				displayer.append(current.taskName + " ");
				StyleRange taskNameStyleRange = new StyleRange(
						wordCountBeforeTaskName, current.taskName.length(),
						white, null);

				taskNameStyleRange.font = displayFont;
				displayer.setStyleRange(taskNameStyleRange);

				/* highlight task name contains search keyword */
				if (current.getKeyWord() != null) {
					highlightKeyword(current, wordCountBeforeTaskName);
				}
			}

			/* For non-default tag, record its information and display it */
			if (current.taggedWord != null && !current.isDefaultTag())

			{
				taggedWordLocation[i - 1] = getCurrentWordCount(displayer);
				displayer.append(" #" + current.taggedWord);
				currentLineCountBeforeTime = displayer.getLineCount();
				displayer.append("\n");

			} else {
				currentLineCountBeforeTime = displayer.getLineCount();
				displayer.append("\n");
			}

			/* Parameters for a styled display of time */
			int wordCountBeforeTime = 0;
			int wordCountAfterTime = 0;
			int timeDisplayLength = 0;
			StyleRange timeDisplayStyleRange;

			/* If task has both start and end time */
			if (current.startTime != null && !current.isDefaultStart()
					&& current.endTime != null && !current.isDefaultEnd()) {

				current.startTime.replaceAll(":00.000", "");
				current.endTime.replaceAll(":00.000", "");
				wordCountBeforeTime = getCurrentWordCount(displayer);
				addBothTime(current);

				wordCountAfterTime = getCurrentWordCount(displayer);
				setTimeDisplayStyle(currentLineCountBeforeTime,
						wordCountBeforeTime, wordCountAfterTime);
				displayer.append("\n");
			}

			/* If task has only start time */
			else if (current.startTime != null && !current.isDefaultStart()) {

				current.startTime.replaceAll(":00.000", "");
				wordCountBeforeTime = getCurrentWordCount(displayer);
				addStartTime(current);

				wordCountAfterTime = getCurrentWordCount(displayer);
				setTimeDisplayStyle(currentLineCountBeforeTime,
						wordCountBeforeTime, wordCountAfterTime);
				displayer.append("\n");

			}

			/* If task has only start time */
			else if (current.endTime != null && !current.isDefaultEnd()) {

				current.startTime.replaceAll(":00.000", "");
				current.endTime.replaceAll(":00.000", "");
				wordCountBeforeTime = getCurrentWordCount(displayer);
				addEndTime(current);
				wordCountAfterTime = getCurrentWordCount(displayer);
				setTimeDisplayStyle(currentLineCountBeforeTime,
						wordCountBeforeTime, wordCountAfterTime);
				displayer.append("\n");
			}

			/* If task has no specific start or end time */
			else {

				wordCountBeforeTime = getCurrentWordCount(displayer);
				displayer.append("No specific deadline ");
				wordCountAfterTime = getCurrentWordCount(displayer);
				setTimeDisplayStyle(currentLineCountBeforeTime,
						wordCountBeforeTime, wordCountAfterTime);
				displayer.append("\n");
			}

			/* Use the recorded location information to highlight tags */
			for (int k = 0; k < taggedWordLocation.length; k++) {

				if (taggedWordLocation[k] != -1) {

					StyleRange taggedWordStyleRange = new StyleRange(
							taggedWordLocation[k],
							taskList.get(k).taggedWord.length() + 2, gold, null);
					displayer.setStyleRange(taggedWordStyleRange);
				}
			}
		}
	}

	/* This method sets the time display style according to related parameters */
	private void setTimeDisplayStyle(int currentLineCountBeforeTime,
			int wordCountBeforeTime, int wordCountAfterTime) {

		int timeDisplayLength;
		StyleRange timeDisplayStyleRange;
		timeDisplayLength = wordCountAfterTime - wordCountBeforeTime;

		timeDisplayStyleRange = new StyleRange(wordCountBeforeTime,
				timeDisplayLength, white, null);

		timeDisplayStyleRange.font = displayTimeFont;
		displayer.setStyleRange(timeDisplayStyleRange);
		displayer.setLineAlignment(currentLineCountBeforeTime, 1, SWT.RIGHT);
	}

	/* This method appends end time of a task with fixed format */
	private void addEndTime(Task current) {

		displayer.append("By " + current.endTime.substring(11, 16) + " "
				+ current.endTime.substring(8, 10) + "/"
				+ current.endTime.substring(5, 7) + "/"
				+ current.endTime.substring(0, 4) + " ");
	}

	/* This method appends start time of a task with fixed format */
	private void addStartTime(Task current) {

		displayer.append("Start: " + current.startTime.substring(11, 16) + " "
				+ current.startTime.substring(8, 10) + "/"
				+ current.startTime.substring(5, 7) + "/"
				+ current.startTime.substring(0, 4) + " ");
	}

	/* This method appends start and end time of a task with fixed format */
	private void addBothTime(Task current) {

		displayer.append("Start: " + current.startTime.substring(11, 16) + " "
				+ current.startTime.substring(8, 10) + "/"
				+ current.startTime.substring(5, 7) + "/"
				+ current.startTime.substring(0, 4) + " End: "
				+ current.endTime.substring(11, 16) + " "
				+ current.endTime.substring(8, 10) + "/"
				+ current.endTime.substring(5, 7) + "/"
				+ current.endTime.substring(0, 4) + " ");
	}

	/* Display index of task with a style */
	private void displayIndexWithStyle(int i, Task currentTask) {

		int indexLocation = getCurrentWordCount(displayer);
		displayer.append(" " + i + " ");

		StyleRange indexStyleRange = new StyleRange(indexLocation, 2, white,
				null);

		indexStyleRange.font = new Font(display, "Arial",
				displayAreaFontSize + 3, SWT.BOLD);

		if (currentTask.isUrgent() != null) {
			indexStyleRange.background = red;
		}
		displayer.setStyleRange(indexStyleRange);
	}

	/**
	 * This method highlights search keywords inside a task name
	 * 
	 * @param current
	 *            is the task whose name contains a search keyword
	 * @param wordCountBeforeTaskName
	 *            is the task name location described by word count
	 */
	private void highlightKeyword(Task current, int wordCountBeforeTaskName) {

		int startLocation;
		for (int m = 0; m < current.getKeyWord().length; m++) {
			String currentKeyWord = current.getKeyWord()[m];
			int keyWordLength = current.getKeyWord()[m].length();

			/* If find a target, highlight it. */
			for (int n = -1; (n = current.taskName.indexOf(currentKeyWord,
					n + 1)) != -1;) {
				startLocation = n + wordCountBeforeTaskName;
				setSearchTargetHighlight(startLocation, keyWordLength);
			}
		}
		/* Clear the keyword and reset the task */
		logicObject.resetTaskKeyword(current);
	}

	/**
	 * This method set predefined highlight style
	 * 
	 * @param startLocation
	 *            is the start location of the highlight text
	 * @param offSet
	 *            is the length offset of the highlight text
	 */
	private void setSearchTargetHighlight(int startLocation, int offSet) {

		StyleRange targetStyleRange = new StyleRange(startLocation, offSet,
				white, null);

		targetStyleRange.background = shell.getDisplay().getSystemColor(
				SWT.COLOR_DARK_GREEN);
		displayer.setStyleRange(targetStyleRange);
	}

	/* This method enables the main window to be minimized to system tray */
	public void initializeMinTray() {

		final Tray tray = display.getSystemTray();
		final ToolTip tip = new ToolTip(shell, SWT.BALLOON
				| SWT.ICON_INFORMATION);
		tip.setAutoHide(true);
		tip.setMessage(MESSAGE_TRAY);

		/* Set icon image for the icon in system tray */
		final Image icon = new Image(display,
				ResourceLoader.load("image/sand.jpg"));
		final TrayItem trayItem = new TrayItem(tray, SWT.NONE);
		trayItem.setImage(icon);

		// When application launches, the tray item is invisible
		trayItem.setVisible(false);
		trayItem.setToolTipText(MESSAGE_MOTO);

		final Menu trayMenu = new Menu(shell, SWT.POP_UP);
		MenuItem showMenuItem = new MenuItem(trayMenu, SWT.PUSH);
		showMenuItem.setText("Display Main Window");

		// Display main window, hide tray icon
		showMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				toggleDisplay(shell, tray);
			}
		});

		trayMenu.setDefaultItem(showMenuItem);
		new MenuItem(trayMenu, SWT.SEPARATOR);

		// Quit option in tray
		MenuItem exitMenuItem = new MenuItem(trayMenu, SWT.PUSH);
		exitMenuItem.setText("quit");
		exitMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.dispose();
			}
		});

		// Click the icon in tray, toggle the main window
		trayItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				toggleDisplay(shell, tray);
			}
		});

		// Right-click in tray, display the control menu
		trayItem.addMenuDetectListener(new MenuDetectListener() {
			public void menuDetected(MenuDetectEvent e) {
				trayMenu.setVisible(true);
			}
		});

		// When minimized, remind user
		shell.addShellListener(new ShellAdapter() {

			public void shellIconified(ShellEvent e) {
				toggleDisplay(shell, tray);
				tip.setText(MESSAGE_MINIMIZED);
				trayItem.setToolTip(tip);
				tip.setVisible(true);
			}
		});
	}

	/* This methods enables minimizing and restoring of main window */
	private static void toggleDisplay(Shell shell, Tray tray) {

		try {
			shell.setVisible(!shell.isVisible());
			tray.getItem(0).setVisible(!shell.isVisible());
			if (shell.getVisible()) {
				shell.setMinimized(false);
				shell.setActive();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Initialize the main display window */
	public void initializeDisplay() {

		displayer.setForeground(white);
		displayer.setFont(displayFont);
		displayer.setEditable(false);
		final ScrollBar vBar = displayer.getVerticalBar();
		vBar.setVisible(false);

		/* Automatically scroll to bottom */
		displayer.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event e) {
				if (displayer.getLineCount() > 20) {
					vBar.setVisible(true);
				}
			}
		});
		FormData displayData = new FormData(470, 390);
		displayData.left = new FormAttachment(4);
		displayData.top = new FormAttachment(14);
		displayer.setLayoutData(displayData);
	}

	/* Initialize the title region of command history window */
	public void initializeHistoryTitle() {

		commandHistoryTitle.setText("Command History");
		commandHistoryTitle.setAlignment(SWT.CENTER);
		commandHistoryTitle.setEditable(false);
		commandHistoryTitle.setBackground(skyBlue);
		commandHistoryTitle.setForeground(white);
		commandHistoryTitle.setForeground(white);
		commandHistoryTitle.setFont(commandHistoryTitleFont);

		FormData commandHistoryTitleData = new FormData(253, 28);
		commandHistoryTitleData.left = new FormAttachment(67);
		commandHistoryTitleData.top = new FormAttachment(14);
		commandHistoryTitle.setLayoutData(commandHistoryTitleData);
	}

	/* Initialize the display region of command history window */
	public void initializeHistory() {

		commandHistory.setEditable(false);
		commandHistory.setForeground(white);
		commandHistory.setFont(commandHistoryFont);

		/* Initially hide the vertical scroll bar */
		final ScrollBar vBar = commandHistory.getVerticalBar();
		vBar.setVisible(false);

		/* Automatically scroll to bottom */
		commandHistory.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event e) {
				commandHistory.setTopIndex(commandHistory.getLineCount() - 1);
				if (commandHistory.getLineCount() > 10) {
					vBar.setVisible(true);
				} else {
					vBar.setVisible(false);
				}
			}
		});
		FormData commandHistoryData = new FormData(233, 180);
		commandHistoryData.right = new FormAttachment(96);
		commandHistoryData.top = new FormAttachment(19);
		commandHistory.setLayoutData(commandHistoryData);
	}

	/* Button to clear command history */
	public void initializeClearCommandHistoryButton() {

		Button clearCommandHistoryBt = new Button(shell, SWT.PUSH);
		clearCommandHistoryBt.setToolTipText("Clear the command history.");
		clearCommandHistoryBt.setText("Clear");
		clearCommandHistoryBt.setBackground(skyBlue);

		clearCommandHistoryBt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				commandHistory.setText("");
			}
		});

		FormData clearCommandHistoryData = new FormData(60, 27);
		clearCommandHistoryData.right = new FormAttachment(95);
		clearCommandHistoryData.top = new FormAttachment(14);
		clearCommandHistoryBt.setLayoutData(clearCommandHistoryData);
	}

	/* Button to trigger the background style dialog */
	public void initializeBackgroundButton(Display display) {

		Button changeBgBt = new Button(shell, SWT.PUSH);
		Image changeBtImage = new Image(display,
				ResourceLoader.load("image/changeBg.png"));
		changeBgBt.setToolTipText("Change the background style");
		changeBgBt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final Shell changeBg = setChangeDialog();
				center(changeBg);
				changeBg.open();
			}
		});

		Region startBtRegion = new Region();
		startBtRegion.add(circle(circularButtonRadius, circularButtonOffsetX,
				circularButtonOffsetY));
		changeBgBt.setRegion(startBtRegion);
		changeBgBt.setImage(changeBtImage);
		FormData changeBgData = new FormData(circularButtonFormData,
				circularButtonFormData);
		changeBgData.right = new FormAttachment(89);
		changeBgData.top = new FormAttachment(2);
		changeBgBt.setLayoutData(changeBgData);
	}

	/* Initialize the background style dialog */
	private Shell setChangeDialog() {

		final Shell changeBg = new Shell(shell);
		changeBg.setText("Background Styles");
		changeBg.setSize(730, 150);

		/* Names of background styles, also part of image file names */
		String[] backgroundList = { "Rainbow world", "Raining glass",
				"Music girl", "Maple leaf", "Deep blue", "Snowing world",
				"Natural sight" };

		/* Produce previews of backgrounds in form of square buttons */
		for (int i = 0; i < 7; i++) {

			/* String to generate preview image paths */
			final StringBuffer backgroundSmallImageFileName = new StringBuffer(
					"image/");

			/* String to generate background image paths */
			final StringBuffer backgroundImageFileName = new StringBuffer(
					"image/");

			/* Generate preview image paths */
			backgroundSmallImageFileName.append(backgroundList[i]);
			backgroundSmallImageFileName.append(" small");
			backgroundSmallImageFileName.append(".png");

			/* Generate background image paths */
			backgroundImageFileName.append(backgroundList[i]);
			backgroundImageFileName.append(".png");
			Image backgoundImageSmall = new Image(
					display,
					ResourceLoader.load(backgroundSmallImageFileName.toString()));

			Button backgroundBt = new Button(changeBg, SWT.PUSH);
			backgroundBt.setToolTipText(backgroundList[i]);

			/* Reset background according to clicked preview image */
			backgroundBt.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					backgroundImageName = backgroundImageFileName.toString();
					backgroundImage = new Image(display, ResourceLoader
							.load(backgroundImageName));
					setShell();
					saveBackgroungImage(backgroundImageName);
					changeBg.dispose();
				}
			});

			Region startBtRegion = new Region();
			startBtRegion.add(square(100));
			backgroundBt.setRegion(startBtRegion);
			backgroundBt.setImage(backgoundImageSmall);
			int xOffset = i * 100 + 10;
			backgroundBt.setBounds(xOffset, 15, 100, 100);
		}
		return changeBg;
	}

	public void initializeHelpButton() {

		Button helpBt = new Button(shell, SWT.PUSH);
		Image changeBtImage = new Image(display,
				ResourceLoader.load("/GUI/JustDoIt/image/help.png"));
		helpBt.setToolTipText(MESSAGE_HELP);

		/* Clicking help button trigger the help information dialog */
		helpBt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					InputStream resource = getClass().getResourceAsStream(
							"/GUI/JustDoIt/user_manual.pdf");
					try {
						File file = File.createTempFile("user_manual", ".pdf");
						file.deleteOnExit();
						OutputStream out = new FileOutputStream(file);
						try {
							int stream = resource.read();
							while (stream != -1) {
								out.write(stream);
								stream = resource.read();
							}
						} finally {
							out.close();
						}
						desktop.open(file);
						resource.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		Region startBtRegion = new Region();
		startBtRegion.add(circle(10, 16, 16));
		helpBt.setRegion(startBtRegion);
		helpBt.setImage(changeBtImage);
		FormData helpData = new FormData(33, 33);
		helpData.right = new FormAttachment(85);
		helpData.top = new FormAttachment(2);
		helpBt.setLayoutData(helpData);
	}

	/* Initialize the minimize button and quit button */
	public void initializeControlButton(Display display) {

		FormLayout layout = new FormLayout();
		shell.setLayout(layout);
		/* Details of quit button */
		Image quitBtImage = new Image(display,
				ResourceLoader.load("image/exit.png"));
		Button quitBt = new Button(shell, SWT.PUSH);
		quitBt.setToolTipText("quit");
		quitBt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.getDisplay().dispose();
				System.exit(0);
			}
		});
		Region startBtRegion = new Region();
		startBtRegion.add(circle(circularButtonRadius, circularButtonOffsetX,
				circularButtonOffsetY));
		quitBt.setRegion(startBtRegion);
		quitBt.setImage(quitBtImage);

		FormData quitData = new FormData(circularButtonFormData,
				circularButtonFormData);
		quitData.right = new FormAttachment(97);
		quitData.top = new FormAttachment(2);
		quitBt.setLayoutData(quitData);

		/* Details of minimize button */
		Image minBtImage = new Image(display,
				ResourceLoader.load("image/min2.png"));
		Button minBt = new Button(shell, SWT.PUSH);

		minBt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setMinimized(true);
			}
		});

		Region minBtRegion = new Region();
		minBtRegion.add(circle(circularButtonRadius, circularButtonOffsetX,
				circularButtonOffsetY));
		minBt.setRegion(minBtRegion);
		minBt.setImage(minBtImage);
		minBt.setToolTipText("minimize");

		FormData minData = new FormData(circularButtonFormData,
				circularButtonFormData);
		minData.right = new FormAttachment(93);
		minData.top = new FormAttachment(2);
		minBt.setLayoutData(minData);
	}

	public void initializeCalendar() {

		final DateTime calendar = new DateTime(shell, SWT.CALENDAR
				| SWT.COLOR_BLUE | SWT.BORDER);

		FormData calData = new FormData(253, 180);
		calData.right = new FormAttachment(96);
		calData.top = new FormAttachment(53);
		calendar.setLayoutData(calData);

		/*
		 * On clicking the date, automatically search for tasks on that
		 * 
		 * date
		 */
		calendar.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {

				/* Get time data from calendar */
				int year = calendar.getYear();
				int month = (calendar.getMonth() + 1);
				int day = calendar.getDay();

				/* Generate search command from calendar data */
				StringBuffer searchCommand = new StringBuffer("search ");
				searchCommand.append(Integer.toString(day) + "-");
				searchCommand.append(Integer.toString(month) + "-");
				searchCommand.append(Integer.toString(year));

				/* Append this search command in command history */
				addBoldTextInCommandHistory(commandHistory,
						searchCommand.toString());

				/* Execute search by time command and display results */
				returnMessage = uiSwitch.executeCommand(searchCommand
						.toString());

				/* Display feedback and search result(if any) */
				if (returnMessage == null || returnMessage.isEmpty()) {
					// do nothing
				} else {
					display(returnMessage.get(0).getTaskName());
					returnMessage.remove(0);
					clearScreen();
					display(returnMessage);
				}
			}
		});
	}

	/* This method generates a welcome splash when the application launches */
	public void initializeWelcome(final Display display) {

		final Image image = new Image(display,
				ResourceLoader.load("image/logo2.png"));
		final Shell splash = new Shell(SWT.ON_TOP);
		splash.setBackgroundImage(image);

		Label label = new Label(splash, SWT.NONE);
		label.setImage(image);
		FormLayout layout = new FormLayout();
		splash.setLayout(layout);
		splash.pack();
		Rectangle splashRect = splash.getBounds();
		Rectangle displayRect = display.getBounds();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		splash.setLocation(x, y);
		splash.open();

		/* Set the lifetime of the splash */
		display.asyncExec(new Runnable() {
			public void run() {
				try {
					Thread.sleep(700);
				} catch (Throwable e) {
				}
				/* When splash ends, open the main window */
				splash.close();
				image.dispose();
				shell.open();
			}
		});
	}

	/* Set shell-global keyboard shortcuts */
	public void setHotKeys(Display display) {

		display.addFilter(SWT.KeyDown, new Listener() {

			public void handleEvent(Event e) {
				// Ctrl + a: display all task
				if ((e.stateMask == SWT.CTRL) && (e.keyCode == 97)) {
					addBoldTextInCommandHistory(commandHistory, "display all");
					displayFeedback(uiSwitch.executeCommand("display all"));
				}
				// Ctrl + z: undo
				else if ((e.stateMask == SWT.CTRL) && (e.keyCode == 122)) {
					addBoldTextInCommandHistory(commandHistory, "undo");
					displayFeedback(uiSwitch.executeCommand("undo"));
				}
				// Ctrl + p: display pending task
				else if ((e.stateMask == SWT.CTRL) && (e.keyCode == 112)) {
					addBoldTextInCommandHistory(commandHistory,
							"display pending");
					displayFeedback(uiSwitch.executeCommand("display pending"));
				}
				// Ctrl + u: display unfinished task
				else if ((e.stateMask == SWT.CTRL) && (e.keyCode == 117)) {
					addBoldTextInCommandHistory(commandHistory,
							"display unfinished");
					displayFeedback(uiSwitch
							.executeCommand("display unfinished"));
				}
				// Esc: quit
				else if (e.keyCode == 27) {
					System.exit(0);
				}
			}
		});
	}

	/* This method put the target shell at center of screen */
	public void center(Shell shell) {

		Rectangle bds = shell.getDisplay().getBounds();
		Point p = shell.getSize();
		int nLeft = (bds.width - p.x) / 2;
		int nTop = (bds.height - p.y) / 2;
		shell.setBounds(nLeft, nTop, p.x, p.y);
	}

	/**
	 * This method generate a circular region
	 * 
	 * @param r
	 *            is the radius of this circle
	 * @param offSetX
	 *            is the length offset on x axis
	 * @param offSetY
	 *            is the length offset on y axis
	 */
	public int[] circle(int r, int offsetX, int offsetY) {

		int[] ring = new int[8 * r + 4];
		for (int i = 0; i < 2 * r + 1; i++) {
			int x = i - r;
			int y = (int) Math.sqrt(r * r - x * x);
			ring[2 * i] = offsetX + x;
			ring[2 * i + 1] = offsetY + y;
			ring[8 * r - 2 * i - 2] = offsetX + x;
			ring[8 * r - 2 * i - 1] = offsetY - y;
		}
		return ring;
	}

	/**
	 * This method generate a square region
	 * 
	 * @param length
	 *            is the side length of this square
	 */
	public int[] square(int length) {

		int[] newRec = new int[8];
		newRec[0] = 0;
		newRec[1] = 0;
		newRec[2] = 0;
		newRec[3] = length;
		newRec[4] = length;
		newRec[5] = length;
		newRec[6] = length;
		newRec[7] = 0;
		return newRec;
	}

	/**
	 * This method generate a region from a background image
	 * 
	 * @param display
	 *            is the display area of this region
	 * @param image
	 *            is the file path of the background image
	 */
	public static Region getBackRegionFromImage(Display display, String image) {

		ImageLoader loader = new ImageLoader();
		ImageData[] imageData = loader.load(ResourceLoader.load(image));
		Region region = new Region(display);
		ImageData data = imageData[0];
		ImageData mask = data.getTransparencyMask();
		org.eclipse.swt.graphics.Rectangle pixel = new org.eclipse.swt.graphics.Rectangle(
				0, 0, 1, 1);
		for (int y = 0; y < mask.height; y++) {
			for (int x = 0; x < mask.width; x++) {
				if (mask.getPixel(x, y) != 0) {
					pixel.x = data.x + x;
					pixel.y = data.y + y;
					region.add(pixel);
				}
			}
		}
		return region;
	}

	/* Adjust font size according to computer monitor resolution */
	private static void setFontSize() {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();
		int width = dim.width;

		switch (width) {
		case 1920: {
			inputCommandFontSize = 8;
			displayAreaFontSize = 10;
			displayHistoryFontSize = 8;
			break;

		}
		default: {
			inputCommandFontSize = 10;
			displayAreaFontSize = 12;
			displayHistoryFontSize = 10;
			break;
		}
		}
	}

	public static void main(String[] args) throws IOException {

		uiSwitch = new UIswitch();
		GUI app = new GUI();
	}
}
