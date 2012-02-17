package com.cloudlbs.web.noauth.client.presenter;

import com.cloudlbs.web.core.gwt.BaseAsyncCallback;
import com.cloudlbs.web.core.gwt.Presenter;
import com.cloudlbs.web.i18n.msg.Messages;
import com.cloudlbs.web.noauth.client.RPCLoginServiceAsync;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEvent;
import com.cloudlbs.web.noauth.client.view.LoginForm;
import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class LoginFormPresenter implements Presenter, LoginForm.Presenter<LoginCredentials> {

    private RPCLoginServiceAsync loginService;
    private HandlerManager eventBus;
    private LoginForm<LoginCredentials> view;
    private Messages messages;

    @Inject
    public LoginFormPresenter(HandlerManager eventBus, LoginForm<LoginCredentials> view,
            RPCLoginServiceAsync loginService, Messages messages) {
        this.eventBus = eventBus;
        this.view = view;
        this.view.setPresenter(this);
        this.loginService = loginService;
        this.messages = messages;
    }

    @Override
    public void onSignInClicked() {
        LoginCredentials creds = view.getLoginCredentials();
        loginService.login(creds, new BaseAsyncCallback<Boolean>(view.getWrapper(), messages) {
            @Override
            public void onSuccess(Boolean result) {
                if (!result) {
                    view.showErrorMessage(messages.usernameOrPasswordIncorrect());
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
