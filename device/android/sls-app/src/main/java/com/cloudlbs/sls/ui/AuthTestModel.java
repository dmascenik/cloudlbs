package com.cloudlbs.sls.ui;

import com.cloudlbs.sls.ui.mvp.Model;

/**
 * @author Dan Mascenik
 * 
 */
public class AuthTestModel extends Model {

	private String result = "";

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
		setHasChanges(true);
		notifyViews();
	}

}
