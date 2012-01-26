package com.cloudlbs.sls.ui;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.cloudlbs.sls.R;
import com.cloudlbs.sls.SLSService;
import com.cloudlbs.sls.core.LocationRequestParams;
import com.cloudlbs.sls.core.SLSStatus;
import com.cloudlbs.sls.event.LocationReadingListener;
import com.cloudlbs.sls.location.LocationReading;
import com.cloudlbs.sls.ui.mvp.ModelLocator;

/**
 * @author Dan Mascenik
 * 
 */
public class MainPresenter implements ServiceBoundPresenter<MainView>,
		LocationReadingListener {

	private SLSStateModel slsStateModel;
	private LastLocationModel lastLocationModel;
	private SLSService slsService;
	private MainView context;

	public interface Display {
		View getEnableDisableClickable();

		View getCheckinClickable();

		View getPingClickable();

		View getStatusClickable();

		View getLocationCoordsView();

		View getLocationTimeView();

		View getStatusView();

	}

	@Override
	public void registerListeners() {
		View activator = context.getEnableDisableClickable();
		activator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleEnabledState();
			}
		});

		View checkin = context.getCheckinClickable();
		checkin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doCheckin();
			}
		});

		View pingToggle = context.getPingClickable();
		pingToggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				togglePinger();
			}
		});

		View statusButton = context.getStatusClickable();
		statusButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getStatus();
			}
		});
	}

	/**
	 * Does a "checkin" by scheduling an immediate location reading
	 */
	private void doCheckin() {
		lastLocationModel.setLocationKnown(false);
		slsService.getLocation(null, new LocationRequestParams());
	}

	private void getStatus() {
		SLSStatus status = slsService.getStatus();
		slsStateModel.setStatus(status.getFaultCode());
	}

	/**
	 * Starts or stops location pinging at a 5s frequency
	 */
	private synchronized void togglePinger() {
		if (!slsStateModel.isPinging()) {
			// Start pinging
			LocationRequestParams params = new LocationRequestParams();
			params.setFrequencySeconds(5);
			slsService.getLocation(null, params);
			slsStateModel.setPinging(true);
		} else {
			// Cancel pinging
			slsService.cancelReadings(null);
			slsStateModel.setPinging(false);
		}
	}

	/**
	 * Turns the service on and off
	 */
	private void toggleEnabledState() {
		if (slsStateModel.isStarting() || slsStateModel.isStopping()) {
			// ignore
			return;
		}

		boolean isStartup = !slsService.isEnabled();
		if (isStartup) {
			slsStateModel.setStarting(true);
		} else {
			slsStateModel.setStopping(true);
		}

		StartStopTask sst = new StartStopTask(slsStateModel, slsService,
				isStartup);
		sst.execute();
	}

	@Override
	public void onLocationReading(LocationReading reading) {
		if (SLSService.SYSTEM_IDENTIFIER.equals(reading.getApiKey())) {
			lastLocationModel.deferNotifications();
			lastLocationModel.setLatitude(reading.getLatitude());
			lastLocationModel.setLongitude(reading.getLongitude());
			lastLocationModel.setAltitude(reading.getAltitude());
			lastLocationModel.setErrorRadius(reading.getErrorRadius());
			lastLocationModel.setTimestamp(reading.getTimestamp());
			lastLocationModel.setLocationKnown(true);
			lastLocationModel.notifyViews(true);
		}
	}

	@Override
	public void initializeModels() {
		slsStateModel = ModelLocator.getModel(SLSStateModel.class);
		lastLocationModel = ModelLocator.getModel(LastLocationModel.class);
	}

	@Override
	public void onServiceConnected(SLSService service) {
		slsService = service;
		slsStateModel.deferNotifications();
		slsStateModel.setConnected(true);
		slsStateModel.setEnabled(slsService.isEnabled());
		slsStateModel.setPinging(slsService.isPinging(null));
		slsStateModel.notifyViews(true);
	}

	@Override
	public void onServiceDisconnected() {
		slsStateModel.setConnected(false);
		slsService = null;
	}

	/**
	 * @param item
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemExit:
			context.finish();
			break;
		case R.id.menuItemAuthTest:
			context.startActivity(new Intent(context, AuthTestView.class));
			break;
		case R.id.menuItemLog:
			context.startActivity(new Intent(context, LogView.class));
			break;
		case R.id.menuItemSettings:
			context.startActivity(new Intent(context, SettingsView.class));
			break;
		default:
			throw new IllegalArgumentException("Menu item not handled");
		}
		return true;
	}

	@Override
	public MainView getContext() {
		return context;
	}

	@Override
	public void setContext(MainView context) {
		this.context = context;
	}

}
