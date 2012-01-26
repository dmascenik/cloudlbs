package com.cloudlbs.sls.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cloudlbs.sls.R;
import com.cloudlbs.sls.event.ModelEvent;
import com.cloudlbs.sls.ui.SettingsPresenter.Display;
import com.cloudlbs.sls.ui.mvp.Model;

public class SettingsView extends BaseView<SettingsPresenter> implements
		Display {

	private ProgressDialog startingDialog;
	private boolean isEmulatorMode = false;
	private boolean isConnected = false;

	public SettingsView() {
		super(new SettingsPresenter(), R.layout.settings);
	}

	@Override
	protected void handleModelChange(ModelEvent event) {
		Model model = event.getModel();

		EditText hn = getHostnameTextView();
		EditText pt = getPortTextView();
		EditText buf = getLogBufferSizeTextView();

		if (SLSStateModel.class.isAssignableFrom(model.getClass())) {
			SLSStateModel slsStateModel = (SLSStateModel) model;

			/*
			 * Show the progress spinner if restarting
			 */
			if (slsStateModel.isStarting() && startingDialog == null) {
				startingDialog = ProgressDialog.show(this, null,
						"Applying settings...", true, false);
			} else {
				if (startingDialog != null) {
					startingDialog.dismiss();
					startingDialog = null;
				}
			}

			if (slsStateModel.isConnected()) {
				isConnected = true;
				if (!isEmulatorMode) {
					hn.setEnabled(true);
					pt.setEnabled(true);
					getUseHttpsCheckbox().setEnabled(true);
				}
				buf.setEnabled(true);
				getEmulatorModeCheckbox().setEnabled(true);
				getDefaultsButton().setEnabled(true);
				getApplyButton().setEnabled(true);
			} else {
				isConnected = false;
				hn.setEnabled(false);
				pt.setEnabled(false);
				getUseHttpsCheckbox().setEnabled(false);
				buf.setEnabled(false);
				getEmulatorModeCheckbox().setEnabled(false);
				getDefaultsButton().setEnabled(false);
				getApplyButton().setEnabled(false);
			}
		} else if (SettingsModel.class.isAssignableFrom(model.getClass())) {
			SettingsModel settingsModel = (SettingsModel) model;
			getHostnameTextView().setText(settingsModel.getHostname());
			getPortTextView()
					.setText(Integer.toString(settingsModel.getPort()));
			getUseHttpsCheckbox().setChecked(settingsModel.isUseHttps());
			getLogBufferSizeTextView().setText(
					Integer.toString(settingsModel.getLogLines()));
			isEmulatorMode = settingsModel.isEmulatorMode();
			getEmulatorModeCheckbox().setChecked(isEmulatorMode);
			if (isEmulatorMode) {
				getHostnameTextView().setEnabled(false);
				getPortTextView().setEnabled(false);
				getUseHttpsCheckbox().setEnabled(false);
			} else {
				if (isConnected) {
					getHostnameTextView().setEnabled(true);
					getPortTextView().setEnabled(true);
					getUseHttpsCheckbox().setEnabled(true);
				}
			}
		}
	}

	public void displayValidationError(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setCancelable(true);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public EditText getHostnameTextView() {
		return (EditText) findViewById(R.id.settingsHostname);
	}

	@Override
	public EditText getPortTextView() {
		return (EditText) findViewById(R.id.settingsPort);
	}

	@Override
	public CheckBox getUseHttpsCheckbox() {
		return (CheckBox) findViewById(R.id.settingsUseHttps);
	}

	@Override
	public EditText getLogBufferSizeTextView() {
		return (EditText) findViewById(R.id.settingsLogBufferSize);
	}

	@Override
	public CheckBox getEmulatorModeCheckbox() {
		return (CheckBox) findViewById(R.id.settingsEmulatorMode);
	}

	@Override
	public Button getDefaultsButton() {
		return (Button) findViewById(R.id.settingsDefaultsButton);
	}

	@Override
	public Button getApplyButton() {
		return (Button) findViewById(R.id.settingsApplyButton);
	}

}
