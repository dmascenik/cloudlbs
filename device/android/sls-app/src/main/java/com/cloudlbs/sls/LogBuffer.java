package com.cloudlbs.sls;

import java.util.ArrayList;

import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.LogEvent;
import com.cloudlbs.sls.event.LoggerListener;
import com.cloudlbs.sls.utils.Logger;

/**
 * @author Dan Mascenik
 * 
 */
public class LogBuffer implements LoggerListener {

	private ArrayList<String> buffer = new ArrayList<String>();
	private int size = 20;

	public LogBuffer() {
		EventDispatcher.addListener(this);
	}

	@Override
	public void onLogEvent(LogEvent evt) {
		writeLine(evt.getLevel(), evt.getText());
	}

	public synchronized void writeLine(int level, String text) {
		buffer.add(0, getLevelString(level) + ": " + text);
		trimBuffer();
	}

	public String getContents() {
		StringBuffer sb = new StringBuffer();
		for (int i = buffer.size() - 1; i > -1; i--) {
			sb.append(buffer.get(i) + "\n");
		}
		return sb.toString();
	}

	public int getSize() {
		return size;
	}

	public int getCurrentSize() {
		return buffer.size();
	}

	public void setSize(int size) {
		if (size < 10 || size > 200) {
			throw new IllegalArgumentException("Log buffer must be between "
					+ "10 and 200 lines");
		}
		this.size = size;
		trimBuffer();
	}

	private void trimBuffer() {
		if (buffer.size() > size) {
			for (int i = size; i < buffer.size(); i++) {
				buffer.remove(i);
			}
		}
	}

	private String getLevelString(int level) {
		String s;
		switch (level) {
		case Logger.DEBUG:
			s = "D";
			break;

		case Logger.INFO:
			s = "I";
			break;

		case Logger.WARN:
			s = "W";
			break;

		case Logger.ERROR:
			s = "E";
			break;

		default:
			throw new IllegalArgumentException("Unknown log level: " + level);
		}
		return s;
	}

}
