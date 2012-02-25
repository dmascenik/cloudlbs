package com.cloudlbs.web.noauth.client.presenter;

import com.cloudlbs.web.core.gwt.BaseAsyncCallback;
import com.cloudlbs.web.core.gwt.Presenter;
import com.cloudlbs.web.i18n.msg.Messages;
import com.cloudlbs.web.noauth.client.RPCLoginServiceAsync;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEvent;
import com.cloudlbs.web.noauth.client.view.LoginForm;
import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
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
//        try {
//            RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, "j_spring_security_check");
//            rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
//            rb.setHeader("Cache-Control", "max-age=0");
//            rb.setHeader("Accepts", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//            rb.sendRequest(
//                    "j_username=" + creds.getUsername() + "&j_password=" + creds.getPassword() + "&submit=Login",
//                    new RequestCallback() {
//                        public void onError(Request request, Throwable exception) {
//                            view.alert(messages.unknownError());
//                        }
//                        public void onResponseReceived(Request request, Response response) {
//                            Window.Location.replace(response.getHeader("Location"));
//                        }
//                    });
//        } catch (Exception e) {
//            view.alert(messages.unknownError());
//        }

        loginService.login(creds, new BaseAsyncCallback<Boolean>(view.getWrapper(), messages) {
            @Override
            public void onSuccess(Boolean result) {
                if (!result) {
                    view.showErrorMessage(messages.usernameOrPasswordIncorrect());
                } else {
                    Window.Location.replace("main.html");
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
