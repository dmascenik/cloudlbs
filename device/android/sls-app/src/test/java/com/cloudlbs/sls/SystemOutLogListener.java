package com.cloudlbs.sls;

import com.cloudlbs.sls.event.LogEvent;

/**
 * @author Dan Mascenik
 * 
 */
public class SystemOutLogListener {

	public void onLogEvent(LogEvent e) {
		System.out.println(e.getText());
	}

}
