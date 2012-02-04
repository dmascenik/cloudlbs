package com.cloudlbs.web.noauth.client.view;

import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
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
public class LoginFormImpl<T> extends Composite implements LoginForm<T> {

	@UiTemplate("LoginForm.ui.xml")
	interface Binder extends UiBinder<Widget, LoginFormImpl<?>> {
	}

	private static Binder uiBinder = GWT.create(Binder.class);

	@UiField
	TextBox username;

	@UiField
	PasswordTextBox password;

	@UiField
	Button signIn;

	@UiField
	Label errorLabel;

	private Presenter<T> presenter;

	public LoginFormImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		errorLabel.setVisible(false);
	}

	@UiHandler("signIn")
	void onSignInClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSignInClicked();
		}
	}

	@UiHandler("newUser")
	void onNewUserClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onNewUserClicked();
		}
	}

	@Override
	public LoginCredentials getLoginCredentials() {
		String user = username.getValue();
		String passwd = password.getValue();
		return new LoginCredentials(user, passwd);
	}

	@Override
	public void setPresenter(Presenter<T> presenter) {
		this.presenter = presenter;
	}

	@Override
	public Widget asWidget() {
		return this;
	}

}
