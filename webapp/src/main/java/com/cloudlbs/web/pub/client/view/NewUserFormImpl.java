package com.cloudlbs.web.pub.client.view;

import com.cloudlbs.web.core.gwt.Resources;
import com.cloudlbs.web.core.gwt.css.CloudLBSStyles;
import com.cloudlbs.web.core.gwt.ui.BaseViewImpl;
import com.cloudlbs.web.core.gwt.ui.wrapper.StandardPanel;
import com.cloudlbs.web.core.shared.FormValidations;
import com.cloudlbs.web.i18n.msg.Messages;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
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
public class NewUserFormImpl<T> extends BaseViewImpl implements NewUserForm<T> {

    @UiTemplate("NewUserForm.ui.xml")
    interface Binder extends UiBinder<Widget, NewUserFormImpl<?>> {
    }

    private static Binder uiBinder = GWT.create(Binder.class);
    private Messages messages;
    private CloudLBSStyles style;

    @UiField TextBox username;
    @UiField TextBox email;
    @UiField PasswordTextBox password;
    @UiField PasswordTextBox passwordConf;
    @UiField Button createUser;
    @UiField Button cancel;
    @UiField Label userNameErrorLabel;
    @UiField Label emailErrorLabel;
    @UiField Label passwordErrorLabel;
    @UiField Label passwordConfErrorLabel;
    @UiField Image workingSpinner;

    private boolean usernameValid = false;
    private boolean emailValid = false;
    private boolean passwordValid = false;
    private boolean passwordConfirmed = false;

    private Presenter<T> presenter;

    @Inject
    public NewUserFormImpl(StandardPanel wrapper, Messages messages, Resources resources) {
        super(wrapper);
        initWidget(uiBinder.createAndBindUi(this));
        this.messages = messages;
        this.style = resources.css();
        workingSpinner.setVisible(false);
        userNameErrorLabel.setVisible(false);
        emailErrorLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
        passwordConfErrorLabel.setVisible(false);
        wrapper.setContents(this);
    }

    @Override
    public void setWorking(boolean isWorking) {
        workingSpinner.setVisible(isWorking);
        createUser.setEnabled(!isWorking);
        cancel.setEnabled(!isWorking);
    }

    @UiHandler("createUser")
    void onCreateUserClicked(ClickEvent event) {
        if (allValid(true)) {
            presenter.onSubmitClicked();
        } else {
            resetSubmitButton();
        }
    }

    @UiHandler("cancel")
    void onCancelClicked(ClickEvent event) {
        presenter.onCancelClicked();
    }

    @UiHandler("email")
    void onEmailBlur(BlurEvent event) {
        validateEmail();
        resetSubmitButton();
    }

    @UiHandler("username")
    void onUsernameBlur(BlurEvent event) {
        validateUsername();
        resetSubmitButton();
    }

    @UiHandler("password")
    void onPasswordBlur(BlurEvent event) {
        validatePassword();
        resetSubmitButton();
    }

    @UiHandler("passwordConf")
    void onPasswordConfBlur(BlurEvent event) {
        validatePasswordConf();
        resetSubmitButton();
    }

    private void validateEmail() {
        String emailStr = email.getValue().trim();
        if (!FormValidations.isEmail(emailStr)) {
            emailErrorLabel.setText(messages.invalidEmail());
            email.setStyleName(style.textBoxWarning());
            emailErrorLabel.setVisible(true);
            emailValid = false;
        } else {
            emailValid = true;
            email.setStyleName(style.textBox());
            emailErrorLabel.setVisible(false);
        }
    }

    private void validateUsername() {
        String usernameStr = username.getValue().trim();
        if (!FormValidations.isValidUsername(usernameStr)) {
            userNameErrorLabel.setText(messages.invalidUsername());
            username.setStyleName(style.textBoxWarning());
            userNameErrorLabel.setVisible(true);
            usernameValid = false;
        } else {
            usernameValid = true;
            username.setStyleName(style.textBox());
            userNameErrorLabel.setVisible(false);
        }
    }

    private void validatePassword() {
        String passwordStr = password.getValue();
        if (!FormValidations.isValidPassword(passwordStr)) {
            passwordErrorLabel.setText(messages.invalidPassword());
            password.setStyleName(style.textBoxWarning());
            passwordErrorLabel.setVisible(true);
            passwordValid = false;
        } else {
            passwordValid = true;
            password.setStyleName(style.textBox());
            passwordErrorLabel.setVisible(false);
        }
    }

    private void validatePasswordConf() {
        String passwordStr = password.getValue();
        String passwordConfStr = passwordConf.getValue();
        if (!passwordStr.equals(passwordConfStr)) {
            passwordConfErrorLabel.setText(messages.passwordMismatch());
            passwordConf.setStyleName(style.textBoxWarning());
            passwordConfErrorLabel.setVisible(true);
            passwordConfirmed = false;
        } else {
            passwordConfirmed = true;
            passwordConf.setStyleName(style.textBox());
            passwordConfErrorLabel.setVisible(false);
        }
    }

    private void resetSubmitButton() {
        if (allValid()) {
            createUser.setEnabled(true);
        } else {
            createUser.setEnabled(false);
        }
    }

    private boolean allValid() {
        return allValid(false);
    }

    private boolean allValid(boolean refresh) {
        if (refresh) {
            validateEmail();
            validateUsername();
            validatePassword();
            validatePasswordConf();
        }
        return (usernameValid & emailValid & passwordValid & passwordConfirmed);
    }

    @Override
    public NewUserDetails getNewUserDetails() {
        String user = username.getValue().trim();
        String emailStr = email.getValue().trim();
        String passwd = password.getValue();
        return new NewUserDetails(user, emailStr, passwd);
    }

    @Override
    public void setPresenter(Presenter<T> presenter) {
        this.presenter = presenter;
    }

}
