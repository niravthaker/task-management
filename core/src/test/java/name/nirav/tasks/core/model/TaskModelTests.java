package name.nirav.tasks.core.model;

import static name.nirav.tasks.core.util.DateUtilsTest.parseDate;
import static org.junit.Assert.assertEquals;

import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;

import name.nirav.tasks.core.model.impl.FreeFormTask;

import org.junit.Test;

public class TaskModelTests {
	@Test
	public void testTimeline(){
		Task fftTask = newTask("Test", "1/1/2001", "1/2/2001");
		assertEquals("Timeline mismatch.", timeline("1/1/2001", "1/2/2001"),fftTask.timeline());
		FreeFormTask firstChild = newTask("SubTest", "1/3/2001", "1/4/2001");
		fftTask.addTask(firstChild);
		assertEquals("Timeline mismatch.", timeline("1/1/2001", "1/4/2001"),fftTask.timeline());
		FreeFormTask firstChildsChild = newTask("SubTest2", "1/6/2001", "1/10/2001");
		firstChild.addTask(firstChildsChild);
		assertEquals("Timeline mismatch.", timeline("1/1/2001", "1/10/2001"),fftTask.timeline());
		assertEquals("Timeline mismatch.", timeline("1/3/2001", "1/10/2001"),firstChild.timeline());
	}

	private Entry<Date, Date> timeline(String start, String end) {
		return new AbstractMap.SimpleEntry<Date, Date>(parseDate(start), parseDate(end));
	}

	@Test
	public void testDuration() throws Throwable{
		Task fftTask = newTask("Test", "1/1/2001", "1/2/2001");
		assertEquals("Date duration for single task failed.", 1,fftTask.duration());
		FreeFormTask firstChild = newTask("SubTest", "1/3/2001", "1/4/2001");
		fftTask.addTask(firstChild);
		assertEquals("Date duration for composite task failed.", 3,fftTask.duration());
		FreeFormTask firstChildsChild = newTask("SubTest2", "1/6/2001", "1/10/2001");
		firstChild.addTask(firstChildsChild);
		assertEquals("Date duration for composite task failed.", 7,firstChild.duration());
		assertEquals("Date duration for composite task failed.", 4,firstChildsChild.duration());
		assertEquals("Date duration for composite task failed.", 9,fftTask.duration());
	}
	@Test
	public void testProgressFFT(){
		Task fftTask = newTask(10);
		assertEquals("Progress for single task failed.", 10,fftTask.getProgress());
		fftTask.addTask(newTask(20));
		assertEquals("Progress for composite task failed.", 15,fftTask.getProgress());
		fftTask.addTask(newTask(30));
		assertEquals("Progress for composite task failed.", 20,fftTask.getProgress());

	}
	public static FreeFormTask newTask(String title, String startDate, String endDate) {
		FreeFormTask freeFormTask = new FreeFormTask(title, parseDate(startDate), parseDate(endDate), 0);
		freeFormTask.setId(String.valueOf(counter++));
		return freeFormTask;
	}
	public static FreeFormTask newTask(int progress) {
		FreeFormTask freeFormTask = new FreeFormTask("Untitled" + progress, parseDate("1/1/2001"), parseDate("1/1/2001"), progress);
		freeFormTask.setId(String.valueOf(counter++));
		return freeFormTask;
	}
	
	static int counter = 0;
	
	
	public static FreeFormTask newTask(int progress, int duration) {
		return new FreeFormTask("Untitled" + progress, dateFromToday(-duration), new Date(), progress);
	}

	public static Date dateFromToday(int i) {
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.DAY_OF_YEAR, i);
		return instance.getTime();
	}

	public static Task newTask(String id, String title) {
		FreeFormTask task = new FreeFormTask(title, new Date(), new Date(), 0);
		task.setId(id);
		return task;
	}
}
