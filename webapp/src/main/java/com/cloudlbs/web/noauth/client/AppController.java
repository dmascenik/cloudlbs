package com.cloudlbs.web.noauth.client;

import com.cloudlbs.web.core.gwt.Presenter;
import com.cloudlbs.web.noauth.client.command.CancelNewUserCommand;
import com.cloudlbs.web.noauth.client.command.CreateUserCommand;
import com.cloudlbs.web.noauth.client.command.LoginSubmitCommand;
import com.cloudlbs.web.noauth.client.command.NewUserCommand;
import com.cloudlbs.web.noauth.client.event.CancelCreateUserEvent;
import com.cloudlbs.web.noauth.client.event.CreateUserEvent;
import com.cloudlbs.web.noauth.client.event.LoginSubmitEvent;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEvent;
import com.cloudlbs.web.noauth.client.presenter.LoginFormPresenter;
import com.cloudlbs.web.noauth.client.presenter.NewUserFormPresenter;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class AppController implements Presenter, ValueChangeHandler<String> {

	public static final String HISTORY_LOGIN = "login";
	public static final String HISTORY_NEW_USER = "newuser";

	private final HandlerManager eventBus;
	private HasWidgets container;

	@Inject private LoginFormPresenter loginFormPresenter;
	@Inject private NewUserFormPresenter newUserFormPresenter;

	@Inject
	public AppController(HandlerManager eventBus) {
		this.eventBus = eventBus;
		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);
		eventBus.addHandler(LoginSubmitEvent.TYPE, new LoginSubmitCommand());
		eventBus.addHandler(NewUserRequestEvent.TYPE, new NewUserCommand());
		eventBus.addHandler(CancelCreateUserEvent.TYPE,
				new CancelNewUserCommand());
		eventBus.addHandler(CreateUserEvent.TYPE, new CreateUserCommand());
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		if (token != null) {
			if (token.equals(HISTORY_LOGIN)) {
				loginFormPresenter.go(container);
			} else if (token.equals(HISTORY_NEW_USER)) {
				newUserFormPresenter.go(container);
			}
		}
	}

	@Override
	public void go(HasWidgets container) {
		this.container = container;
		if ("".equals(History.getToken())) {
			History.newItem(HISTORY_LOGIN);
		} else {
			History.fireCurrentHistoryState();
		}
	}

}
