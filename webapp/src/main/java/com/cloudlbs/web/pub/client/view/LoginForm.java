package com.cloudlbs.web.pub.client.view;

import com.cloudlbs.web.core.gwt.ui.View;
import com.cloudlbs.web.pub.shared.model.UsernamePasswordAuthentication;

public interface LoginForm<T> extends View {

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
    void redirectToAuthenticated();
    UsernamePasswordAuthentication getLoginCredentials();

}
