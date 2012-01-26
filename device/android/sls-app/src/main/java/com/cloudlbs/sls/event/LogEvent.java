package com.cloudlbs.sls.event;

/**
 * A simple event type that contains a log level and a text string.<br/>
 * <br/>
 * This type of event is intended for simple debugging with the SLS debugger
 * UI, and is not recommended for use in production apps.
 * 
 * @author Dan Mascenik
 * 
 */
public class LogEvent implements SLSEvent {

	private String text;
	private int level;

	public LogEvent(int level, String text) {
		this.text = text;
		this.level = level;
	}

	public String getText() {
		return text;
	}

	public int getLevel() {
		return level;
	}

}
