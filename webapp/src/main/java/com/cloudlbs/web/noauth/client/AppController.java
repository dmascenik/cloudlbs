package com.cloudlbs.web.noauth.client;

import com.cloudlbs.web.core.gwt.Presenter;
import com.cloudlbs.web.noauth.client.event.CancelCreateUserEvent;
import com.cloudlbs.web.noauth.client.event.CancelCreateUserEventHandler;
import com.cloudlbs.web.noauth.client.event.CreateUserEvent;
import com.cloudlbs.web.noauth.client.event.CreateUserEventHandler;
import com.cloudlbs.web.noauth.client.event.LoginSubmitEvent;
import com.cloudlbs.web.noauth.client.event.LoginSubmitEventHandler;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEvent;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEventHandler;
import com.cloudlbs.web.noauth.client.presenter.LoginFormPresenter;
import com.cloudlbs.web.noauth.client.presenter.NewUserFormPresenter;
import com.cloudlbs.web.noauth.client.view.LoginFormImpl;
import com.cloudlbs.web.noauth.client.view.NewUserFormImpl;
import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.cloudlbs.web.noauth.shared.model.NewUserDetails;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppController implements Presenter, ValueChangeHandler<String> {

	public static final String HISTORY_LOGIN = "login";
	public static final String HISTORY_NEW_USER = "newuser";

	private final HandlerManager eventBus;
	private HasWidgets container;
	private LoginFormImpl<LoginCredentials> loginFormView;
	private NewUserFormImpl<NewUserDetails> newUserView;

	public AppController(HandlerManager eventBus) {
		this.eventBus = eventBus;
		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);

		eventBus.addHandler(LoginSubmitEvent.TYPE,
				new LoginSubmitEventHandler() {

					@Override
					public void onLoginSubmit(LoginSubmitEvent event) {
						doLoginSubmit(event.getCredentials());
					}
				});

		eventBus.addHandler(NewUserRequestEvent.TYPE,
				new NewUserRequestEventHandler() {

					@Override
					public void onNewUserRequest(NewUserRequestEvent event) {
						doNewUserRequest();
					}
				});

		eventBus.addHandler(CancelCreateUserEvent.TYPE,
				new CancelCreateUserEventHandler() {

					@Override
					public void onCancelCreateUser(CancelCreateUserEvent event) {
						doCancelCreateUser();
					}
				});

		eventBus.addHandler(CreateUserEvent.TYPE, new CreateUserEventHandler() {

			@Override
			public void onCreateUser(CreateUserEvent event) {
				doCreateUser(event.getUserDetails());
			}
		});

	}

	private void doLoginSubmit(LoginCredentials credentials) {
		// TODO
		System.out.println("Logging in " + credentials.getUsername());
	}

	private void doCreateUser(NewUserDetails newUserDetails) {
		// TODO
		System.out.println("Creating user  " + newUserDetails.getUsername());
	}

	private void doNewUserRequest() {
		History.newItem(HISTORY_NEW_USER);
	}

	private void doCancelCreateUser() {
		History.newItem(HISTORY_LOGIN);
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();

		if (token != null) {
			if (token.equals(HISTORY_LOGIN)) {
				if (loginFormView == null) {
					loginFormView = new LoginFormImpl<LoginCredentials>();

				}
				new LoginFormPresenter(eventBus, loginFormView).go(container);
			} else if (token.equals(HISTORY_NEW_USER)) {
				if (newUserView == null) {
					newUserView = new NewUserFormImpl<NewUserDetails>();
				}
				new NewUserFormPresenter(eventBus, newUserView).go(container);
			}
		}
	}

	@Override
	public void go(HasWidgets container) {
		this.container = container;
		if ("".equals(History.getToken())) {
			// Default view is log in
			History.newItem(HISTORY_LOGIN);
		} else {
			History.fireCurrentHistoryState();
		}
	}

}
