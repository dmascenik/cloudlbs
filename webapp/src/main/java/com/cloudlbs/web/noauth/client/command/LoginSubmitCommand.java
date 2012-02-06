package com.cloudlbs.web.noauth.client.command;

import com.cloudlbs.web.noauth.client.LoginServiceAsync;
import com.cloudlbs.web.noauth.client.event.LoginSubmitEvent;
import com.cloudlbs.web.noauth.client.event.LoginSubmitEventHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class LoginSubmitCommand implements LoginSubmitEventHandler {

    @Inject LoginServiceAsync loginService;

    @Override
    public void onLoginSubmit(LoginSubmitEvent event) {
        // TODO
        System.out.println("Logging in " + event.getCredentials().getUsername());

        loginService.login(event.getCredentials(), new AsyncCallback<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {
                System.out.println("Login " + (result ? "success" : "failed"));
            }

            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }
        });
    }

}
