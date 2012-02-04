package com.cloudlbs.web.noauth.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author danmascenik
 * 
 */
public class LoginForm extends Composite {

	interface Binder extends UiBinder<Widget, LoginForm> {
	}

	private static Binder uiBinder = GWT.create(Binder.class);

	public LoginForm() {
		initWidget(uiBinder.createAndBindUi(this));
		errorLabel.setVisible(false);
	}

	@UiField TextBox username;
	@UiField PasswordTextBox password;
	@UiField Button signIn;
	@UiField Label errorLabel;
}
