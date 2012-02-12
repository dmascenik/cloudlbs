package com.cloudlbs.web.noauth.client.view;

import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.google.gwt.user.client.ui.Widget;

public interface LoginForm<T> {

    public interface Presenter<T> {
        void onSignInClicked();
        void onNewUserClicked();
    }

    void setPresenter(Presenter<T> presenter);
    LoginCredentials getLoginCredentials();
    Widget asWidget();
}
