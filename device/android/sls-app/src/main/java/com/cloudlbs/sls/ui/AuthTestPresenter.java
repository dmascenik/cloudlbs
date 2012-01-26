package com.cloudlbs.sls.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.cloudlbs.sls.SLSService;
import com.cloudlbs.sls.core.AuthenticationStatus;
import com.cloudlbs.sls.ui.mvp.ModelLocator;
import com.cloudlbs.sls.utils.Logger;

/**
 * @author Dan Mascenik
 * 
 */
public class AuthTestPresenter implements ServiceBoundPresenter<AuthTestView> {

	private AuthTestView context;
	private SLSStateModel slsStateModel;
	private SLSService slsService;
	private AuthTestModel authModel;

	public interface Display {
		View getUsernameView();

		View getPasswordView();

		View getTestClickable();

		View getResetClickable();

		View getTestResultView();
	}

	@Override
	public void initializeModels() {
		slsStateModel = ModelLocator.getModel(SLSStateModel.class);
		slsStateModel.setConnected(false);
		authModel = ModelLocator.getModel(AuthTestModel.class);
	}

	@Override
	public void registerListeners() {
		context.getTestClickable().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				testAuthentication();
			}
		});

		context.getResetClickable().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resetFields();
			}
		});
	}

	private void testAuthentication() {
		String username = ((EditText) context.getUsernameView()).getText()
				.toString().trim();
		String password = ((EditText) context.getPasswordView()).getText()
				.toString().trim();

		AuthenticationStatus auth = slsService.authenticate(
				SLSService.SYSTEM_IDENTIFIER, username, password, null);

		if (auth.getIsSuccessful()) {
			authModel.setResult("SUCCESS");
			Logger.debug("Test authentication success");
		} else {
			StringBuffer result = new StringBuffer("FAILED: ");
			switch (auth.getFaultCode()) {
			case AuthenticationStatus.FAULT_CREDENTIALS:
				result.append("Bad Credentials");
				break;

			case AuthenticationStatus.FAULT_NO_NETWORK:
				result.append("No Network");
				break;

			case AuthenticationStatus.FAULT_SERVICE_DISABLED:
				result.append("Service Disabled");
				break;

			case AuthenticationStatus.FAULT_SERVICE_FAILURE:
				result.append("Service Failure");
				break;

			case AuthenticationStatus.FAULT_USER_DISABLED:
				result.append("User Disabled");
				break;

			case AuthenticationStatus.FAULT_INVALID_APP:
				result.append("Invalid App");
				break;

			case AuthenticationStatus.FAULT_APP_DISABLED:
				result.append("App Disabled on this Device");
				break;

			case AuthenticationStatus.FAULT_INVALID_API_KEY:
				result.append("Invalid API Key");
				break;

			default:
				break;
			}
			String s = result.toString();
			Logger.debug("Test authentication failed - " + s);
			authModel.setResult(s);
		}
	}

	private void resetFields() {
		((EditText) context.getUsernameView()).setText("");
		((EditText) context.getPasswordView()).setText("");
		((TextView) context.getTestResultView()).setText("");
	}

	@Override
	public void onServiceConnected(SLSService service) {
		slsService = service;
		slsStateModel.setConnected(true);
		slsStateModel.setEnabled(slsService.isEnabled());
	}

	@Override
	public void onServiceDisconnected() {
		slsStateModel.setConnected(false);
		slsService = null;
	}

	@Override
	public AuthTestView getContext() {
		return context;
	}

	@Override
	public void setContext(AuthTestView context) {
		this.context = context;
	}

}
