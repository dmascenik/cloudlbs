package com.cloudlbs.sls.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.cloudlbs.sls.SLSService;
import com.cloudlbs.sls.dao.PreferencesDao;
import com.cloudlbs.sls.ui.mvp.ModelLocator;
import com.cloudlbs.sls.utils.Logger;

public class SettingsPresenter implements
		ServiceBoundPresenter<SettingsView> {

	private SLSStateModel slsStateModel;
	private SettingsModel settingsModel;
	private SLSService slsService;
	private SettingsView context;

	public interface Display {
		View getHostnameTextView();

		View getPortTextView();

		View getUseHttpsCheckbox();

		View getLogBufferSizeTextView();

		View getEmulatorModeCheckbox();

		View getDefaultsButton();

		View getApplyButton();
	}

	@Override
	public void registerListeners() {
		CheckBox emulatorCheckBox = context.getEmulatorModeCheckbox();
		emulatorCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton checkbox,
							boolean checked) {
						toggleEmulatorMode(checked, false);
					}
				});

		Button defaultsButton = context.getDefaultsButton();
		defaultsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View defaultsButton) {
				settingsModel.deferNotifications();
				settingsModel
						.setHostname(PreferencesDao.VALUE_MASTER_HOSTNAME_DEF);
				settingsModel.setPort(PreferencesDao.VALUE_MASTER_PORT_DEF);
				settingsModel.setUseHttps(PreferencesDao.VALUE_USE_HTTPS_DEF);
				settingsModel
						.setEmulatorMode(PreferencesDao.VALUE_EMULATOR_MODE_DEF);
				settingsModel
						.setLogLines(PreferencesDao.VALUE_LOG_BUFF_SIZE_DEF);
				toggleEmulatorMode(PreferencesDao.VALUE_EMULATOR_MODE_DEF, true);
			}
		});

		Button applyButton = context.getApplyButton();
		applyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View applyButton) {
				applySettings();
			}
		});

	}

	/**
	 * Applies the new settings, restarting the SLS if it is currently running.
	 * 
	 */
	private void applySettings() {
		/*
		 * First, validate the fields
		 */
		String hostname = context.getHostnameTextView().getText().toString()
				.trim();
		String port = context.getPortTextView().getText().toString().trim();
		boolean useHttps = context.getUseHttpsCheckbox().isChecked();
		boolean emulatorMode = context.getEmulatorModeCheckbox().isChecked();
		String logSize = context.getLogBufferSizeTextView().getText()
				.toString().trim();

		if (!hostname
				.matches("^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$")
				&& !hostname.matches("^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}"
						+ "|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]"
						+ "|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}"
						+ "|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}"
						+ "|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]"
						+ "|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$")) {
			context.displayValidationError("Invalid hostname");
			return;
		}

		int portInt;
		try {
			portInt = Integer.parseInt(port);
		} catch (Exception e) {
			context.displayValidationError("Invalid port number");
			return;
		}
		if (portInt < 80 || portInt > 65535) {
			context.displayValidationError("Invalid port number");
			return;
		}

		int logSizeInt;
		try {
			logSizeInt = Integer.parseInt(logSize);
		} catch (Exception e) {
			context.displayValidationError("Invalid log buffer size");
			return;
		}
		if (logSizeInt < 10 || logSizeInt > 200) {
			context.displayValidationError("Log buffer size must be between 10 and 200");
			return;
		}

		/*
		 * Log buffer can be managed without a restart
		 */
		slsService.setLogBufferSize(logSizeInt);

		if (!hostname.equals(slsService.getMasterHostname())
				|| portInt != slsService.getMasterPort()
				|| useHttps != slsService.getUseHttps()
				|| emulatorMode != slsService.getEmulatorMode()) {
			/*
			 * Something changed that requires a restart
			 */
			boolean wasRunning = slsService.isEnabled();
			if (wasRunning) {
				Logger.debug("Service was running, will try to restart");

				/*
				 * Do this in the current thread. It's fast, but still collides
				 * with the startup task later.
				 */
				slsStateModel.setStopping(true);
				slsService.setEnabled(false);
				slsStateModel.deferNotifications();
				slsStateModel.setEnabled(slsService.isEnabled());
				slsStateModel.setStopping(false);
				slsStateModel.forceNotifyViews();
			}
			if (!emulatorMode) {
				slsService.setMasterHostname(hostname);
				slsService.setMasterPort(portInt);
				slsService.setUseHttps(useHttps);
			}
			slsService.setEmulatorMode(emulatorMode);
			Logger.debug("Service settings updated");
			if (wasRunning) {
				slsStateModel.setStarting(true);
				StartStopTask sst = new StartStopTask(slsStateModel,
						slsService, true);
				sst.execute();
			}
			refreshSettingsFromService();
		}
	}

	/**
	 * This just updates the enabled state of the UI widgets. Nothing is
	 * persisted until the user clicks Apply.
	 * 
	 * @param isEmulatorMode
	 */
	private void toggleEmulatorMode(boolean isEmulatorMode, boolean useDefaults) {
		TextView hn = context.getHostnameTextView();
		TextView pt = context.getPortTextView();
		CheckBox https = context.getUseHttpsCheckbox();

		settingsModel.deferNotifications();
		settingsModel.setEmulatorMode(isEmulatorMode);

		if (isEmulatorMode) {
			hn.setEnabled(false);
			pt.setEnabled(false);
			https.setEnabled(false);
			settingsModel.setHostname(PreferencesDao.EMULATOR_HOST);
			settingsModel.setPort(PreferencesDao.EMULATOR_PORT);
			settingsModel.setUseHttps(false);
		} else {
			hn.setEnabled(true);
			pt.setEnabled(true);
			https.setEnabled(true);
			if (useDefaults) {
				/*
				 * If we're resetting to defaults, don't reload the values from
				 * the service
				 */
			} else {
				settingsModel.setHostname(slsService.getMasterHostname());
				settingsModel.setPort(slsService.getMasterPort());
				settingsModel.setUseHttps(slsService.getUseHttps());
			}
		}

		if (!useDefaults) {
			settingsModel.setLogLines(Integer.parseInt(context
					.getLogBufferSizeTextView().getText().toString().trim()));
		}

		settingsModel.forceNotifyViews();
	}

	private void refreshSettingsFromService() {
		settingsModel.deferNotifications();
		settingsModel.setEmulatorMode(slsService.getEmulatorMode());
		settingsModel.setHostname(slsService.getMasterHostname());
		settingsModel.setPort(slsService.getMasterPort());
		settingsModel.setUseHttps(slsService.getUseHttps());
		settingsModel.setLogLines(slsService.getLogBufferSize());
		settingsModel.forceNotifyViews();
	}

	@Override
	public void initializeModels() {
		slsStateModel = ModelLocator.getModel(SLSStateModel.class);
		slsStateModel.notifyViews();
		settingsModel = ModelLocator.getModel(SettingsModel.class);
	}

	@Override
	public void onServiceConnected(SLSService service) {
		slsService = service;
		Logger.debug("Notifying view that service is connected");
		slsStateModel.setConnected(true);
		refreshSettingsFromService();
	}

	@Override
	public void onServiceDisconnected() {
		slsStateModel.setConnected(false);
		slsService = null;
	}

	@Override
	public void setContext(SettingsView context) {
		this.context = context;
	}

	@Override
	public SettingsView getContext() {
		return context;
	}

}
