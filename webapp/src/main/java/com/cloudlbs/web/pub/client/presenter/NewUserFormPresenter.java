package com.cloudlbs.web.pub.client.presenter;

import com.cloudlbs.web.core.gwt.Presenter;
import com.cloudlbs.web.pub.client.event.CancelCreateUserEvent;
import com.cloudlbs.web.pub.client.view.NewUserForm;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class NewUserFormPresenter implements Presenter,
		NewUserForm.Presenter<NewUserDetails> {

	private HandlerManager eventBus;
	private NewUserForm<NewUserDetails> view;

	@Inject
	public NewUserFormPresenter(HandlerManager eventBus,
			NewUserForm<NewUserDetails> view) {
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
	}

	@Override
	public void onSubmitClicked() {
        NewUserDetails userDetails = view.getNewUserDetails();
        System.out.println("Creating " + userDetails.getUsername());

//        loginService.login(creds, new AsyncCallback<Boolean>() {
//
//            @Override
//            public void onSuccess(Boolean result) {
//                System.out.println("Login " + (result ? "success" : "failed"));
//                if (!result) {
//                    view.showErrorMessage("Username or password is incorrect");
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable caught) {
//                caught.printStackTrace();
//                view.showErrorMessage(caught.getMessage());
//            }
//        });
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
