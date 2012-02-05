package com.cloudlbs.web.noauth.client.presenter;

import com.cloudlbs.web.core.gwt.Presenter;
import com.cloudlbs.web.noauth.client.event.CancelCreateUserEvent;
import com.cloudlbs.web.noauth.client.event.CreateUserEvent;
import com.cloudlbs.web.noauth.client.view.NewUserForm;
import com.cloudlbs.web.noauth.shared.model.NewUserDetails;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class NewUserFormPresenter implements Presenter,
		NewUserForm.Presenter<NewUserDetails> {

	private final HandlerManager eventBus;
	private final NewUserForm<NewUserDetails> view;

	@Inject
	public NewUserFormPresenter(HandlerManager eventBus,
			NewUserForm<NewUserDetails> view) {
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
	}

	@Override
	public void onSubmitClicked() {
		eventBus.fireEvent(new CreateUserEvent(view.getNewUserDetails()));
	}

	@Override
	public void onCancelClicked() {
		eventBus.fireEvent(new CancelCreateUserEvent());
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

}
