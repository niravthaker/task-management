package name.nirav.tasks.core.util;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import name.nirav.tasks.core.util.DateUtils;

import org.junit.Test;

public class DateUtilsTest {

	@Test
	public void testDuration() throws Throwable {
		assertEquals(1 , DateUtils.duration(parseDate("1/1/2001"), parseDate("1/2/2001")));
		assertEquals(12 , DateUtils.duration(parseDate("8/29/2001"), parseDate("9/10/2001")));
		assertEquals(8 , DateUtils.duration(parseDate("2/27/2004"), parseDate("3/6/2004")));
	}

	public static Date parseDate(String string) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		try {
			return sdf.parse(string);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
