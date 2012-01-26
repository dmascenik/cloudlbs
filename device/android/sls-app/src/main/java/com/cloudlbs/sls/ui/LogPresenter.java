package com.cloudlbs.sls.ui;

import android.view.View;
import android.view.View.OnClickListener;

import com.cloudlbs.sls.R;
import com.cloudlbs.sls.SLSService;
import com.cloudlbs.sls.event.LogEvent;
import com.cloudlbs.sls.event.LoggerListener;
import com.cloudlbs.sls.ui.mvp.ModelLocator;
import com.cloudlbs.sls.utils.Logger;

/**
 * @author Dan Mascenik
 * 
 */
public class LogPresenter implements ServiceBoundPresenter<LogView>,
		LoggerListener {

	private LogModel logModel;
	private SLSService service;
	private LogView context;

	public interface Display {
		View getDebugClickable();

		View getInfoClickable();

		View getWarnClickable();

		View getErrorClickable();

		void setData(String logContent);
	}

	@Override
	public void initializeModels() {
		logModel = ModelLocator.getModel(LogModel.class);

		// Definitely don't want to log when the log gets updated
		logModel.setNoLogging(true);
	}

	@Override
	public void onLogEvent(LogEvent evt) {
		// In case we get an event before the service binds
		if (service != null) {
			logModel.setLogContents(service.getLogContents());
		}
	}

	@Override
	public void registerListeners() {
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View view) {
				handleLogLevelClick(view);
			}
		};

		context.getDebugClickable().setOnClickListener(ocl);
		context.getInfoClickable().setOnClickListener(ocl);
		context.getWarnClickable().setOnClickListener(ocl);
		context.getErrorClickable().setOnClickListener(ocl);
	}

	public void handleLogLevelClick(View view) {
		switch (view.getId()) {
		case R.id.logLevelDebug:
			Logger.setLogLevel(Logger.DEBUG);
			break;

		case R.id.logLevelInfo:
			Logger.setLogLevel(Logger.INFO);
			break;

		case R.id.logLevelWarn:
			Logger.setLogLevel(Logger.WARN);
			break;

		case R.id.logLevelError:
			Logger.setLogLevel(Logger.ERROR);
			break;

		default:
			// do nothing
			break;
		}
	}

	@Override
	public void onServiceConnected(SLSService service) {
		this.service = service;
		logModel.setLogContents(service.getLogContents());
	}

	@Override
	public void onServiceDisconnected() {
		this.service = null;
	}

	@Override
	public LogView getContext() {
		return context;
	}

	@Override
	public void setContext(LogView context) {
		this.context = context;
	}

}
