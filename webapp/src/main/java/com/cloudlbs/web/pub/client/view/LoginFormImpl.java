package com.cloudlbs.web.pub.client.view;

import com.cloudlbs.web.core.gwt.AppConstants;
import com.cloudlbs.web.core.gwt.ui.BaseViewImpl;
import com.cloudlbs.web.core.gwt.ui.wrapper.StandardPanel;
import com.cloudlbs.web.pub.shared.model.UsernamePasswordAuthentication;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * 
 * @author danmascenik
 * 
 */
public class LoginFormImpl extends BaseViewImpl implements LoginForm {

    @UiTemplate("LoginForm.ui.xml")
    interface Binder extends UiBinder<Widget, LoginFormImpl> {
    }

    private static Binder uiBinder = GWT.create(Binder.class);
    private Presenter presenter;
    private AppConstants constants;

    @UiField TextBox username;
    @UiField PasswordTextBox password;
    @UiField Button signIn;
    @UiField Label errorLabel;
    @UiField Image workingSpinner;

    @Inject
    public LoginFormImpl(StandardPanel wrapper, AppConstants constants) {
        super(wrapper);
        initWidget(uiBinder.createAndBindUi(this));
        workingSpinner.setVisible(false);
        errorLabel.setVisible(false);
        wrapper.setContents(this);
    }

    @UiHandler("signIn")
    void onSignInClicked(ClickEvent event) {
        errorLabel.setVisible(false);
        presenter.onSignInClicked();
    }

    @UiHandler("newUser")
    void onNewUserClicked(ClickEvent event) {
        errorLabel.setVisible(false);
        presenter.onNewUserClicked();
    }

    @Override
    public void setWorking(boolean isWorking) {
        workingSpinner.setVisible(isWorking);
        signIn.setEnabled(!isWorking);
    }

    @Override
    public void showErrorMessage(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @Override
    public void redirectToAuthenticated() {
        Window.Location.replace(constants.loginTarget());
    }

    @Override
    public void clearForm() {
        errorLabel.setVisible(false);
        username.setValue("");
        password.setValue("");
    }

    @Override
    public UsernamePasswordAuthentication getLoginCredentials() {
        String user = username.getValue();
        String passwd = password.getValue();
        return new UsernamePasswordAuthentication(user, passwd);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

}
