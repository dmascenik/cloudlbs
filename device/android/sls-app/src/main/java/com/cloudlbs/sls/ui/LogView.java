package com.cloudlbs.sls.ui;

import android.view.View;
import android.widget.TextView;

import com.cloudlbs.sls.R;
import com.cloudlbs.sls.SLSService;
import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.ModelEvent;
import com.cloudlbs.sls.ui.LogPresenter.Display;
import com.cloudlbs.sls.ui.mvp.Model;

/**
 * This is the main entry screen for the SLS debugger. The purpose of this
 * application is to provide direct access to the activity and status of the SLS
 * service. Normally, other applications would bind to the SLS service and
 * simply receive data from it.
 * 
 * @see SLSService
 * @author Dan Mascenik
 */
public class LogView extends BaseView<LogPresenter> implements
		Display {

	public LogView() {
		super(new LogPresenter(), R.layout.log);
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventDispatcher.addListener(presenter);
	}

	@Override
	protected void onPause() {
		EventDispatcher.removeListener(presenter);
		super.onPause();
	}

	@Override
	protected void handleModelChange(ModelEvent evt) {
		Model model = evt.getModel();
		if (LogModel.class.isAssignableFrom(model.getClass())) {
			LogModel logModel = (LogModel) model;
			setData(logModel.getLogContents());
		}
	}

	@Override
	public void setData(String logContent) {
		TextView t = (TextView) findViewById(R.id.logBufferView);
		t.setText(logContent);
	}

	@Override
	public View getDebugClickable() {
		return findViewById(R.id.logLevelDebug);
	}

	@Override
	public View getInfoClickable() {
		return findViewById(R.id.logLevelInfo);
	}

	@Override
	public View getWarnClickable() {
		return findViewById(R.id.logLevelWarn);
	}

	@Override
	public View getErrorClickable() {
		return findViewById(R.id.logLevelError);
	}

}