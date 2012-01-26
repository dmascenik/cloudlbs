package com.cloudlbs.sls.ui.mvp;

import android.app.Activity;

/**
 * @author Dan Mascenik
 * 
 */
public interface Presenter<V extends Activity> {

	public V getContext();

	public void setContext(V context);

	public void registerListeners();

	public void initializeModels();

}
