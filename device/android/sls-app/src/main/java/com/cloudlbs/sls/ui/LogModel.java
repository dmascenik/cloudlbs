package com.cloudlbs.sls.ui;

import com.cloudlbs.sls.ui.mvp.Model;

/**
 * @author Dan Mascenik
 * 
 */
public class LogModel extends Model {

	private String logContents;

	public String getLogContents() {
		return logContents;
	}

	public void setLogContents(String logContents) {
		this.logContents = logContents;
		setHasChanges(true);
		notifyViews();
	}

}
