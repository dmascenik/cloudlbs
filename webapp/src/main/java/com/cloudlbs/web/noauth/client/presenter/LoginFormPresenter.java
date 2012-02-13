package com.cloudlbs.web.noauth.client.presenter;

import com.cloudlbs.web.core.gwt.BaseAsyncCallback;
import com.cloudlbs.web.core.gwt.Presenter;
import com.cloudlbs.web.noauth.client.RPCLoginServiceAsync;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEvent;
import com.cloudlbs.web.noauth.client.view.LoginForm;
import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class LoginFormPresenter implements Presenter, LoginForm.Presenter<LoginCredentials> {

    private final RPCLoginServiceAsync loginService;
    private final HandlerManager eventBus;
    private final LoginForm<LoginCredentials> view;

    @Inject
    public LoginFormPresenter(HandlerManager eventBus, LoginForm<LoginCredentials> view,
            RPCLoginServiceAsync loginService) {
        this.eventBus = eventBus;
        this.view = view;
        this.view.setPresenter(this);
        this.loginService = loginService;
    }

    @Override
    public void onSignInClicked() {
        LoginCredentials creds = view.getLoginCredentials();
        System.out.println("Logging in " + creds.getUsername());

        loginService.login(creds, new BaseAsyncCallback<Boolean>(view.getWrapper()) {
            @Override
            public void onSuccess(Boolean result) {
                System.out.println("Login " + (result ? "success" : "failed"));
                if (!result) {
                    view.showErrorMessage("Username or password is incorrect");
                }
            }
        });
    }

    @Override
    public void onNewUserClicked() {
        eventBus.fireEvent(new NewUserRequestEvent());
    }

    @Override
    public void go(HasWidgets container) {
        view.clearForm();
        container.clear();
        container.add(view.asWidget());
    }

}
