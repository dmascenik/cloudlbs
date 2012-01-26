package com.cloudlbs.sls.event;

import com.cloudlbs.sls.location.ReadingSchedule;

/**
 * One of these is fired whenever the {@link ReadingSchedule} changes for any
 * reason; e.g., a reading is added, removed, completed, abandoned, etc.
 * 
 * @author Dan Mascenik
 * 
 */
public class ReadingScheduleChangeEvent implements SLSEvent {

	private ReadingSchedule schedule;

	public ReadingScheduleChangeEvent(ReadingSchedule schedule) {
		this.schedule = schedule;
	}

	public ReadingSchedule getSchedule() {
		return schedule;
	}

}
