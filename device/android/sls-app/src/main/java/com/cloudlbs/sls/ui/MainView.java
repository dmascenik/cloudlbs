package com.cloudlbs.sls.ui;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cloudlbs.sls.R;
import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.ModelEvent;
import com.cloudlbs.sls.ui.MainPresenter.Display;
import com.cloudlbs.sls.ui.mvp.Model;
import com.cloudlbs.sls.xmpp.ProcessorClient;

/**
 * @author Dan Mascenik
 * 
 */
public class MainView extends BaseView<MainPresenter> implements Display {

	private ProgressDialog startingDialog;

	public MainView() {
		super(new MainPresenter(), R.layout.main);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PackageManager manager = getPackageManager();
		String version;
		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			version = info.versionName;
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
		TextView tv = (TextView) findViewById(R.id.mainVersionName);
		tv.setText("Version " + version);
	}

	@Override
	protected void handleModelChange(ModelEvent evt) {
		Model model = evt.getModel();
		if (SLSStateModel.class.isAssignableFrom(model.getClass())) {
			SLSStateModel slsStateModel = (SLSStateModel) model;

			ToggleButton tb = (ToggleButton) getEnableDisableClickable();
			Button checkinButton = (Button) getCheckinClickable();
			ToggleButton pingToggle = (ToggleButton) getPingClickable();
			Button statusButton = (Button) getStatusClickable();
			TextView statusView = (TextView) getStatusView();

			// Base button state
			tb.setEnabled(false);
			checkinButton.setEnabled(false);
			pingToggle.setEnabled(false);
			statusButton.setEnabled(false);

			String statusText = "Disabled";
			statusView.setText(statusText);

			/*
			 * Show the progress spinner if starting
			 */
			if (slsStateModel.isStarting() && startingDialog == null) {
				startingDialog = ProgressDialog.show(this, null,
						"Starting Service...", true, false);
			} else {
				if (startingDialog != null) {
					startingDialog.dismiss();
					startingDialog = null;
				}
			}

			if (slsStateModel.isStarting() || slsStateModel.isStopping()) {

				// Starting or stopping - Leave everything disabled
				return;

			}

			// Is the service connected?
			if (slsStateModel.isConnected()) {
				tb.setEnabled(true);
			} else {

				// Service disconnected - Leave everything disabled
				return;

			}

			// Is the service running?
			if (slsStateModel.isEnabled()) {
				tb.setChecked(true);
				pingToggle.setEnabled(true);
				statusButton.setEnabled(true);

				int status = slsStateModel.getStatus();
				statusView.setText(getStatusText(status));
			} else {
				tb.setChecked(false);

				// Service is off - Leave everything else disabled
				return;
			}

			// Are we pinging?
			if (slsStateModel.isPinging()) {
				pingToggle.setChecked(true);
			} else {
				pingToggle.setChecked(false);
				checkinButton.setEnabled(true);
			}

		} else if (LastLocationModel.class.isAssignableFrom(model.getClass())) {
			LastLocationModel llm = (LastLocationModel) model;
			TextView coordsView = (TextView) getLocationCoordsView();
			TextView timeView = (TextView) getLocationTimeView();
			if (llm.isLocationKnown()) {
				DecimalFormat lldf = new DecimalFormat("#0.000000");
				DecimalFormat accdf = new DecimalFormat("#0.0");
				SimpleDateFormat sdf = new SimpleDateFormat(
						"M/dd/yyyy hh:mm:ss a");

				String latStr = lldf.format(llm.getLatitude());
				String longStr = lldf.format(llm.getLongitude());
				String accStr = accdf.format(llm.getErrorRadius());

				coordsView.setText(latStr + "/" + longStr + " (+/-" + accStr
						+ "m)");
				timeView.setText(sdf.format(new Date(llm.getTimestamp())));

				coordsView.setVisibility(View.VISIBLE);
				timeView.setVisibility(View.VISIBLE);
			} else {
				coordsView.setVisibility(View.INVISIBLE);
				timeView.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sls_main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return presenter.onOptionsItemSelected(item);
	}

	@Override
	public View getCheckinClickable() {
		return findViewById(R.id.mainCheckinButton);
	}

	@Override
	public View getEnableDisableClickable() {
		return findViewById(R.id.mainActivatorButton);
	}

	@Override
	public View getLocationCoordsView() {
		return findViewById(R.id.mainLastLocationCoords);
	}

	@Override
	public View getLocationTimeView() {
		return findViewById(R.id.mainLastLocationTime);
	}

	@Override
	public View getPingClickable() {
		return findViewById(R.id.mainPingerButton);
	}

	@Override
	public View getStatusClickable() {
		return findViewById(R.id.mainStatusButton);
	}

	@Override
	public View getStatusView() {
		return findViewById(R.id.mainStatus);
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

	private String getStatusText(int status) {
		status = status + 1;
		String str = null;
		switch (status) {
		case ProcessorClient.STATUS_ON_NETWORK:
			str = "No Network";
			break;

		case ProcessorClient.STATUS_HAS_PROCESSOR:
			str = "No Processor Designation";
			break;

		case ProcessorClient.STATUS_XMPP_CONNECTED:
			str = "XMPP Not Connected";
			break;

		case ProcessorClient.STATUS_XMPP_AUTHED:
			str = "XMPP Not Authenticated";
			break;

		case ProcessorClient.STATUS_HAS_ROSTER:
			str = "No XMPP Roster";
			break;

		case ProcessorClient.STATUS_PROCESSOR_ONLINE:
			str = "Designated Processor Offline";
			break;

		case ProcessorClient.STATUS_HAS_CHAT:
			str = "No XMPP Chat Session";
			break;

		case 8:
			str = "All Systems Go!";
			break;

		default:
			break;
		}

		return str;
	}

}
