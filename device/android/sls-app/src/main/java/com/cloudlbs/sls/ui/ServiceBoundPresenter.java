package com.cloudlbs.sls.ui;

import android.app.Activity;

import com.cloudlbs.sls.SLSService;
import com.cloudlbs.sls.ui.mvp.Presenter;

/**
 * @author Dan Mascenik
 * 
 */
public interface ServiceBoundPresenter<V extends Activity> extends
		Presenter<V> {

	public void onServiceConnected(SLSService service);

	public void onServiceDisconnected();

}
