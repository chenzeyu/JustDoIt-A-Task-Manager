package task.JustDoIt;

import org.joda.time.*;
import org.joda.time.format.*;

/**
 * This class implements Task structure
 * 
 * @author Chen Zeyu
 */
public class Task implements Comparable<Task> {
	
	private static final String defaultStartTime = "1991-11-11 23:59:00.000";
	private static final String defaultEndTime = "2091-11-11 23:59:00.000";
	private static final String defaultTaggedWord = "k";
	private static final String defaultStatus = "pending";
	
	private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final String URGENT_MESSAGE = "[in %1$s h %2$s min!]";
	
	private static final int LOCATION_TASKNAME = 0;
	private static final int LOCATION_STARTTIME = 1;
	private static final int LOCATION_ENDTIME = 2;
	private static final int LOCATION_TAG = 3;
	
	public String taskName = null;
	public String startTime = null;
	public String endTime = null; // also treated as deadline if there's no
									// startTime
	public String taskStatus = null; // completed, pending, unfinished
	public String taggedWord = null;
	private String[] keyWord = null;

	/**
	 * Constructor
	 * 
	 * @param args
	 *            an array of string contains 5 elements,args[0] is the
	 *            taskName, args[1] is the startTime args[2] is the endTime,
	 *            args[3] is the taggedWord, args[0] cannot be, if other
	 *            elements in args are null, then set the corresponding
	 *            attributes to default values and taskStatus is set to be
	 *            pending
	 */
	//@author A0091683X
	public Task(String[] args) {
		taskName = args[LOCATION_TASKNAME];
		if (args[LOCATION_STARTTIME] != null){
			startTime = args[LOCATION_STARTTIME];
		} else{
			startTime = defaultStartTime;
		}
		
		if (args[LOCATION_ENDTIME] != null){
			endTime = args[LOCATION_ENDTIME];
		} else{
			endTime = defaultEndTime;
		}
		
		if (args[LOCATION_TAG] != null){
			taggedWord = args[LOCATION_TAG];
		} else{
			taggedWord = defaultTaggedWord;
		}
		
		taskStatus = defaultStatus;

	}

	/**
	 * Constructor
	 * 
	 * @param name
	 * 				is the name of the task
	 */
	public Task(String name) {
		assert name != null;
		taskName = name;
	}

	/**
	 * Constructor
	 * 
	 * @param other
	 *            clones a new Task instance from Task other
	 * 
	 */
	public Task(Task other) {
		taskName = other.getTaskName();
		startTime = other.getStartTime();
		endTime = other.getEndTime();
		taskStatus = other.getTaskStatus();
		taggedWord = other.getTaggedWord();
		//keyWord = other.getKeyWord();

	}

	/**
	 * Constructor
	 * 
	 * A do-nothing constructor for yaml format's requirement.
	 */
	public Task() {

	}

	/**
	 * parse string to a DateTime object
	 * 
	 * @param s
	 *            a string with format "yyyy-MM-dd"
	 * @return the converted DateTime from s
	 */
	//@author A0091683X
	public LocalDateTime parseStringToDate(String s) {
		assert s != null;
		try {
			LocalDateTime date;
			DateTimeFormatter fmt = DateTimeFormat.forPattern(TIME_FORMAT);
			date = fmt.parseDateTime(s).toLocalDateTime();
			return date;
		} catch (Exception e) {
			System.out.println("Exception :" + e);
		}
		return null;
	}

	/**
	 * Getter method for taskName
	 * 
	 * @return the name of task
	 * 
	 */
	//@author A0091683X
	public String getTaskName() {
		return taskName;
	}

	/**
	 * Setter method for taskName
	 * 
	 * @param taskName
	 *            the String of the taskName
	 * 
	 */
	//@author A0091683X
	public void setTaskName(String taskName) {
		assert taskName != null;
		this.taskName = taskName;
	}

	/**
	 * Getter method for startTime
	 * 
	 * @return the startTime of task
	 * 
	 */
	//@author A0091683X
	public String getStartTime() {
		return startTime;
	}

	/**
	 * Setter method for startTime
	 * 
	 * @param startTime
	 *            the String of startTime
	 */
	//@author A0091683X
	public void setStartTime(String startTime) {
		assert startTime != null;
		this.startTime = startTime;
	}

	/**
	 * Getter method for endTime
	 * 
	 * @return the endTime of task
	 * 
	 */
	//@author A0091683X
	public String getEndTime() {
		return endTime;
	}

	/**
	 * Setter method for taskName
	 * 
	 * @param endTime
	 *            the String of endTime
	 * 
	 */
	//@author A0091683X
	public void setEndTime(String endTime) {
		assert endTime != null;
		this.endTime = endTime;
	}

	/**
	 * Getter method for taskStatus
	 * 
	 * @return the status of task
	 * 
	 */
	//@author A0091683X
	public String getTaskStatus() {
		return taskStatus;
	}

	/**
	 * Setter method for taskName
	 * 
	 * @param taskStatus
	 *            the String of taskStatus
	 * 
	 */
	//@author A0091683X
	public void setTaskStatus(String taskStatus) {
		assert taskStatus != null;
		this.taskStatus = taskStatus;
	}

	/**
	 * Getter method for taggedWord
	 * 
	 * @return the taggedWord of task
	 * 
	 */
	//@author A0091683X
	public String getTaggedWord() {
		return taggedWord;
	}
	
	//@author A0091683X
	public String[] getKeyWord() {
		if(keyWord != null){
	    	return keyWord;
		} else{
			return null;
		}
	}
	
	//@author A0091683X
	public void setKeyWord(String keyWord) {
		if(keyWord==null){
			this.keyWord=null;
		} else{
		  this.keyWord = keyWord.split("\\s");
		}	
	}

	/**
	 * Setter method for taggedWord
	 * 
	 * @param taggedWord
	 *            the String of taggedWord
	 * 
	 */
	//@author A0091683X
	public void setTaggedWord(String taggedWord) {
		this.taggedWord = taggedWord;
	}

	/**
	 * To determine if a task's startTime is equal to default value
	 * 
	 * @return true if startTime is equal to defaultStartTime, false otherwise.
	 * 
	 */
	//@author A0091683X
	public boolean isDefaultStart() {
		return startTime.equalsIgnoreCase(defaultStartTime);
	}

	/**
	 * To determine if a task's endTime is equal to default value
	 * 
	 * @return true if endTime is equal to defaultEndTime, false otherwise.
	 * 
	 */
	//@author A0091683X
	public boolean isDefaultEnd() {
		return endTime.equalsIgnoreCase(defaultEndTime);
	}

	/**
	 * To determine if a task's taggedWord is equal to default value
	 * 
	 * @return true if taggedWord is equal to defaultTaggedWord, false
	 *         otherwise.
	 * 
	 */
	//@author A0091683X
	public boolean isDefaultTag() {
		return taggedWord.equalsIgnoreCase(defaultTaggedWord);
	}

	/**
	 * To determine if a task is urgent(i.e. due within 24 hours)
	 * 
	 * @return Amount of time before deadLine in a format as [in %1$s h %2$s
	 *         m !], null if not urgent.
	 */
	//@author A0091683X
	public String isUrgent() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime deadLine = parseStringToDate(endTime);
		Integer hoursBeforeDeadLine = Hours.hoursBetween(now, deadLine)
				.getHours();
		Integer minutesBeforeDeadLine = Minutes.minutesBetween(now,
				deadLine.minusHours(hoursBeforeDeadLine)).getMinutes();

		if (hoursBeforeDeadLine < 24 && hoursBeforeDeadLine >= 0 && minutesBeforeDeadLine >= 0) {
			return String.format(URGENT_MESSAGE,
					hoursBeforeDeadLine.toString(),
					minutesBeforeDeadLine.toString());
		}
		return null;
	}
	
	public boolean isValidTime() {
		LocalDateTime start = parseStringToDate(startTime);
		LocalDateTime end = parseStringToDate(endTime);
		return start.isBefore(end);
	}

	// override compareTo for sorting tasks by deadline
	//@author A0091683X
	@Override
	public int compareTo(Task t) {
		if (this.endTime != null && t.endTime != null){
			return parseStringToDate(this.endTime).compareTo(
					parseStringToDate(t.endTime));
		}

		if (t.endTime == null){
			return -1;
		} else{
			return 1;
		}
	}
}
