package com.cloudlbs.web.noauth.client.view;

import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.google.gwt.user.client.ui.Widget;

public interface LoginForm<T> {

    /*
     * Things the view can ask the presenter to do
     */
    public interface Presenter<T> {
        void onSignInClicked();
        void onNewUserClicked();
    }

    /*
     * Things the presenter can ask the view to do
     */
    void setPresenter(Presenter<T> presenter);
    void showErrorMessage(String message);
    void clearForm();
    LoginCredentials getLoginCredentials();
    Widget asWidget();
}
