package com.cloudlbs.web.noauth.client.presenter;

import com.cloudlbs.web.core.gwt.Presenter;
import com.cloudlbs.web.noauth.client.event.LoginSubmitEvent;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEvent;
import com.cloudlbs.web.noauth.client.view.LoginForm;
import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class LoginFormPresenter implements Presenter,
		LoginForm.Presenter<LoginCredentials> {

	private final HandlerManager eventBus;
	private final LoginForm<LoginCredentials> view;

	public LoginFormPresenter(HandlerManager eventBus,
			LoginForm<LoginCredentials> view) {
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
	}

	@Override
	public void onSignInClicked() {
		eventBus.fireEvent(new LoginSubmitEvent(view.getLoginCredentials()));
	}

	@Override
	public void onNewUserClicked() {
		eventBus.fireEvent(new NewUserRequestEvent());
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

}
