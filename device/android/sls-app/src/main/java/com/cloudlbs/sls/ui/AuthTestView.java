package com.cloudlbs.sls.ui;

import android.view.View;
import android.widget.TextView;

import com.cloudlbs.sls.R;
import com.cloudlbs.sls.event.ModelEvent;
import com.cloudlbs.sls.ui.AuthTestPresenter.Display;
import com.cloudlbs.sls.ui.mvp.Model;
import com.cloudlbs.sls.utils.Logger;

/**
 * @author Dan Mascenik
 * 
 */
public class AuthTestView extends BaseView<AuthTestPresenter> implements
		Display {

	public AuthTestView() {
		super(new AuthTestPresenter(), R.layout.auth_test);
	}

	@Override
	protected void handleModelChange(ModelEvent evt) {
		Logger.debug("Got model change");

		Model model = evt.getModel();
		TextView resultText = (TextView) getTestResultView();
		if (AuthTestModel.class.isAssignableFrom(model.getClass())) {
			AuthTestModel authModel = (AuthTestModel) model;
			resultText.setText(authModel.getResult());
		}
	}

	@Override
	public View getUsernameView() {
		return findViewById(R.id.authUsernameTextInput);
	}

	@Override
	public View getPasswordView() {
		return findViewById(R.id.authPasswordTextInput);
	}

	@Override
	public View getTestClickable() {
		return findViewById(R.id.authenticationTestButton);
	}

	@Override
	public View getTestResultView() {
		return findViewById(R.id.authenticationTestResultsText);
	}

	@Override
	public View getResetClickable() {
		return findViewById(R.id.authenticationResetButton);
	}

}
