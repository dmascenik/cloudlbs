package com.cloudlbs.web.noauth.client.view;

import com.cloudlbs.web.noauth.shared.model.NewUserDetails;
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
public class NewUserFormImpl<T> extends Composite implements NewUserForm<T> {

	@UiTemplate("NewUserForm.ui.xml")
	interface Binder extends UiBinder<Widget, NewUserFormImpl<?>> {
	}

	private static Binder uiBinder = GWT.create(Binder.class);

	@UiField
	TextBox username;

	@UiField
	TextBox email;

	@UiField
	PasswordTextBox password;

	@UiField
	PasswordTextBox passwordConf;

	@UiField
	Button createUser;

	@UiField
	Label errorLabel;

	private Presenter<T> presenter;

	public NewUserFormImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		errorLabel.setVisible(false);
	}

	@UiHandler("createUser")
	void onCreateUserClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSubmitClicked();
		}
	}

	@UiHandler("cancel")
	void onCancelClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCancelClicked();
		}
	}

	@Override
	public NewUserDetails getNewUserDetails() {
		String user = username.getValue();
		String emailStr = email.getValue();
		String passwd = password.getValue();
		return new NewUserDetails(user, emailStr, passwd);
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
