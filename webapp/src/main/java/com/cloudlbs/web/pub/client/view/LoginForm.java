package com.cloudlbs.web.pub.client.view;

import com.cloudlbs.web.core.gwt.ui.View;
import com.cloudlbs.web.pub.shared.model.UsernamePasswordAuthentication;
import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;

@Singleton
@ImplementedBy(LoginFormImpl.class)
public interface LoginForm extends View {

    /*
     * Things the view can ask the presenter to do
     */
    public interface Presenter {
        void onSignInClicked();
        void onNewUserClicked();
    }

    /*
     * Things the presenter can ask the view to do
     */
    void setPresenter(Presenter presenter);
    void showErrorMessage(String message);
    void clearForm();
    void redirectToAuthenticated();
    UsernamePasswordAuthentication getLoginCredentials();

}
