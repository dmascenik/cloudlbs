package com.cloudlbs.sls.utils;

import android.util.Log;

import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.LogEvent;

/**
 * Logs to the DDMS logging console and dispatches {@link LogEvent}s to the
 * {@link EventDispatcher}. For better DDMS log filtering, call
 * {@link #setLogTag(String)} with a value that is particular to the app. The
 * default log tag is "sls-core".
 * 
 * @author Dan Mascenik
 * 
 */
public class Logger {

	public static String logTag = "sls-core";

	public static final int DEBUG = 0;
	public static final int INFO = 1;
	public static final int WARN = 2;
	public static final int ERROR = 3;

	private static int level = INFO;

	public static void setLogLevel(int newLevel) {
		if (newLevel < 0 || newLevel > 3) {
			throw new IllegalArgumentException(
					"Log level must be between 0 and 3");
		}
		level = newLevel;
	}

	public static void setLogTag(String logTag) {
		Logger.logTag = logTag;
	}

	public static int getLogLevel() {
		return level;
	}

	public static void debug(String logTag, String text) {
		doLog(DEBUG, text);
		try {
			Log.d(logTag, text);
		} catch (Exception e) {
			// nevermind
		}
	}

	public static void debug(String text) {
		debug(logTag, text);
	}

	public static void debug(String logTag, String text, Exception e) {
		debug(logTag, getLogText(text, e));
	}

	public static void debug(String text, Exception e) {
		debug(getLogText(text, e));
	}

	public static void info(String logTag, String text) {
		doLog(INFO, text);
		try {
			Log.i(logTag, text);
		} catch (Exception e) {
			// nevermind
		}
	}

	public static void info(String text) {
		info(logTag, text);
	}

	public static void info(String logTag, String text, Exception e) {
		info(logTag, getLogText(text, e));
	}

	public static void info(String text, Exception e) {
		info(getLogText(text, e));
	}

	public static void warn(String logTag, String text) {
		doLog(WARN, text);
		try {
			Log.w(logTag, text);
		} catch (Exception e) {
			// nevermind
		}
	}

	public static void warn(String text) {
		warn(logTag, text);
	}

	public static void warn(String logTag, String text, Exception e) {
		warn(logTag, getLogText(text, e));
	}

	public static void warn(String text, Exception e) {
		warn(getLogText(text, e));
	}

	public static void error(String text) {
		error(logTag, text);
	}

	public static void error(String logTag, String text, Exception e) {
		error(logTag, getLogText(text, e));
	}

	public static void error(String text, Exception e) {
		error(getLogText(text, e));
	}

	public static void error(String logTag, String text) {
		doLog(ERROR, text);
		try {
			Log.e(logTag, text);
		} catch (Exception e) {
			// nevermind
		}
	}

	/**
	 * Generates a tight-formatted stack trace for display on the device.
	 * 
	 * @param text
	 * @param e
	 * @return
	 */
	private static String getLogText(String text, Exception e) {
		StackTraceElement[] elements = e.getStackTrace();
		StringBuffer sb = new StringBuffer(text + "\n");
		sb.append(e.getClass().getSimpleName() + ": " + e.getMessage());
		for (int i = 0; i < elements.length; i++) {
			String className = elements[i].getClassName();
			String c = className.substring(className.lastIndexOf(".") + 1);
			sb.append("\n   " + c + ":" + elements[i].getLineNumber());
		}
		if (e.getCause() != null) {
			Throwable e0 = e.getCause();
			sb.append("\nCause: " + e0.getClass().getSimpleName() + ": "
					+ e0.getMessage());
			StackTraceElement[] subElements = e0.getStackTrace();
			for (int i = 0; i < subElements.length; i++) {
				String className = subElements[i].getClassName();
				String c = className.substring(className.lastIndexOf(".") + 1);
				sb.append("\n   " + c + ":" + subElements[i].getLineNumber());
			}
		}
		return sb.toString();
	}

	private static void doLog(int lev, String text) {
		if (lev >= level) {
			EventDispatcher.dispatchEvent(new LogEvent(lev, text));
		}
	}

}
