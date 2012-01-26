package com.cloudlbs.sls.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.cloudlbs.sls.BaseSLSService.LocalServiceBinder;
import com.cloudlbs.sls.SLSService;
import com.cloudlbs.sls.ui.mvp.MvpView;
import com.cloudlbs.sls.ui.mvp.Presenter;

/**
 * @author Dan Mascenik
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class BaseView<P extends Presenter> extends MvpView<P> {

	private ServiceConnection conn;

	/**
	 * @param presenter
	 * @param layoutResID
	 */
	public BaseView(P presenter, int layoutResID) {
		super(presenter, layoutResID);
	}

	@Override
	protected void onResume() {
		super.onResume();

		conn = new LocalServiceConnection(
				(ServiceBoundPresenter) presenter);

		/*
		 * The Application also calls startService(...), but in case it later
		 * died, we don't want it to go back to shutting down on unbind because
		 * this will cause the service to bounce every time an activity changes.
		 * Calling startService(...) again here will persistently restart the
		 * service if it died, and won't have any effect if the service is still
		 * running.
		 */
		startService(new Intent(SLSService.LOCAL_SERVICE_ACTION));

		// Bind as usual
		bindService(new Intent(SLSService.LOCAL_SERVICE_ACTION), conn,
				Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onPause() {
		unbindService(conn);
		super.onPause();
	}

	/**
	 * This is how all the controller classes in the SLS debugger get access to
	 * the full suite of {@link SLSService} methods.
	 * 
	 * @author Dan Mascenik
	 * 
	 */
	public class LocalServiceConnection implements ServiceConnection {

		private SLSService localService;
		private ServiceBoundPresenter presenter;

		public LocalServiceConnection(ServiceBoundPresenter presenter) {
			this.presenter = presenter;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.content.ServiceConnection#onServiceConnected(android.content.
		 * ComponentName, android.os.IBinder)
		 */
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			localService = (SLSService) ((LocalServiceBinder) service)
					.getService();
			presenter.onServiceConnected(localService);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.content.ServiceConnection#onServiceDisconnected(android.content
		 * .ComponentName)
		 */
		@Override
		public void onServiceDisconnected(ComponentName name) {
			localService = null;
			presenter.onServiceDisconnected();
		}

		public SLSService getLocalService() {
			return localService;
		}
	}

}
