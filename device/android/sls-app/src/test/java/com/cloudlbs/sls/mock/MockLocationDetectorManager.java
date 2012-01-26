package com.cloudlbs.sls.mock;

import java.util.Date;

import com.cloudlbs.sls.location.LocationDetectorManager;
import com.cloudlbs.sls.location.LocationProcessor;
import com.cloudlbs.sls.utils.Logger;

/**
 * @author Dan Mascenik
 * 
 */
public class MockLocationDetectorManager extends LocationDetectorManager {

	private boolean isDetectorActive = false;

	public MockLocationDetectorManager(LocationProcessor processor,
			int gpsWarmupMillis, int gpsMaxIdleMillis, int gpsFailedWaitMillis) {
		super(processor, gpsWarmupMillis, gpsMaxIdleMillis, gpsFailedWaitMillis);
	}

	@Override
	protected void doDeactivateLocationDetector() {
		Logger.debug("Deactivating detector");
		isDetectorActive = false;
	}

	@Override
	protected void doActivateLocationDetector() {
		Logger.debug("Activating detector");
		isDetectorActive = true;
	}

	@Override
	protected boolean isLocationDetectorActive() {
		return isDetectorActive;
	}

	@Override
	protected void cancelFutureTask(Runnable r) {
		// does nothing
	}

	protected void scheduleFutureTask(final Runnable r, long uptime) {
		long now = new Date().getTime();
		final long waitTime = uptime - now;
		if (waitTime > 0) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(waitTime);
					} catch (InterruptedException e) {
						return;
					}
					r.run();
				}
			}).start();
		}
	}

	@Override
	protected void scheduleTaskAfterDelay(Runnable r, long delay) {
		scheduleFutureTask(r, System.currentTimeMillis() + delay);
	}

}
