package name.nirav.tasks.core.util;


import java.util.Date;

import org.joda.time.DateMidnight;
import org.joda.time.Days;

public class DateUtils {

	public static int duration(Date startDate, Date endDate) {
		return Days.daysBetween(new DateMidnight(startDate), new DateMidnight(endDate)).getDays();
	}

}
