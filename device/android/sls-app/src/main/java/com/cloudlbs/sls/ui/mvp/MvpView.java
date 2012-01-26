package com.cloudlbs.sls.ui.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.ModelEvent;
import com.cloudlbs.sls.event.ModelListener;
import com.cloudlbs.sls.utils.Logger;

/**
 * This extension of the Android {@link Activity} is registered to listen for
 * {@link ModelEvent}s during {@link #onCreate(Bundle)} and is unregistered
 * during {@link #onPause()}. Be sure to include the "super" call in both of
 * these methods or model change notification will not work.
 * 
 * @author Dan Mascenik
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class MvpView<P extends Presenter> extends Activity implements
		ModelListener {

	protected P presenter;
	private int layoutResID;

	@SuppressWarnings("unchecked")
	public MvpView(P presenter, int layoutResID) {
		if (presenter == null) {
			throw new IllegalArgumentException("presenter cannot be null");
		}
		this.presenter = presenter;
		this.layoutResID = layoutResID;
		presenter.setContext(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventDispatcher.initialize();
		EventDispatcher.addListener(this);
		setContentView(layoutResID);
		presenter.registerListeners();
		presenter.initializeModels();
	}

	@Override
	protected void onPause() {
		EventDispatcher.removeListener(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventDispatcher.addListener(this);
	}

	protected abstract void handleModelChange(ModelEvent evt);

	private Handler mainThreadHandler = new Handler();

	@Override
	public void onModelEvent(final ModelEvent evt) {
		if (!evt.isNoLogging()) {
			/*
			 * If this was a model event for a log buffer, for example, this
			 * would cause a stack overflow
			 */
			Logger.debug(Logger.logTag + "-ui", "Received ModelEvent for "
					+ evt.getModel());
		}
		Runnable modelChange = new Runnable() {

			@Override
			public void run() {
				if (!evt.isNoLogging()) {
					Logger.debug(
							Logger.logTag + "-ui",
							"Calling handleModelChange(...) for "
									+ evt.getModel());
				}
				handleModelChange(evt);
			}
		};
		mainThreadHandler.post(modelChange);
	}
}
