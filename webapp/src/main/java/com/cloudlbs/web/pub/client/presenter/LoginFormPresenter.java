package com.cloudlbs.web.pub.client.presenter;

import com.cloudlbs.web.core.gwt.ui.BaseAsyncCallback;
import com.cloudlbs.web.core.gwt.ui.Presenter;
import com.cloudlbs.web.i18n.msg.Messages;
import com.cloudlbs.web.pub.client.AppController.HistoryToken;
import com.cloudlbs.web.pub.client.event.ChangeViewEvent;
import com.cloudlbs.web.pub.client.rpc.RPCUserServiceAsync;
import com.cloudlbs.web.pub.client.view.LoginForm;
import com.cloudlbs.web.pub.shared.model.UsernamePasswordAuthentication;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class LoginFormPresenter implements Presenter, LoginForm.Presenter<UsernamePasswordAuthentication> {

    private RPCUserServiceAsync userService;
    private HandlerManager eventBus;
    private LoginForm<UsernamePasswordAuthentication> view;
    private Messages messages;

    @Inject
    public LoginFormPresenter(HandlerManager eventBus, LoginForm<UsernamePasswordAuthentication> view,
            RPCUserServiceAsync userService, Messages messages) {
        this.eventBus = eventBus;
        this.view = view;
        this.view.setPresenter(this);
        this.userService = userService;
        this.messages = messages;
    }

    @Override
    public void onSignInClicked() {
        UsernamePasswordAuthentication creds = view.getLoginCredentials();
        userService.login(creds, new BaseAsyncCallback<Boolean>(view, messages) {
            @Override
            public void success(Boolean result) {
                if (!result) {
                    view.showErrorMessage(messages.usernameOrPasswordIncorrect());
                } else {
                    view.redirectToAuthenticated();
                }
            }
        });
    }

    @Override
    public void onNewUserClicked() {
        eventBus.fireEvent(new ChangeViewEvent(HistoryToken.NEW_USER));
    }

    @Override
    public void go(HasWidgets container) {
        view.clearForm();
        container.clear();
        container.add(view.asWidget());
    }

}
