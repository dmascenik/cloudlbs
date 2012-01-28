package com.cloudlbs.web.client.composite;

import java.util.List;

import com.cloudlbs.web.client.UserAccountService;
import com.cloudlbs.web.client.UserAccountServiceAsync;
import com.cloudlbs.web.client.dialog.RPCFailureDialog;
import com.cloudlbs.web.client.dialog.UsernameNotAvailableDialog;
import com.cloudlbs.web.shared.dto.UserAccountDTO;
import com.cloudlbs.web.shared.validation.UserFieldsValidator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Dan Mascenik
 * 
 */
public abstract class RegisterUserForm extends Composite {

	private Button btnSubmit;
	private TextBox usernameTextBox;
	private Label usernameErrorLabel;
	private TextBox firstNameTextBox;
	private Label firstNameErrorLabel;
	private TextBox lastNameTextBox;
	private Label lastNameErrorLabel;
	private TextBox emailTextBox;
	private Label emailErrorLabel;
	private PasswordTextBox passwordTextBox;
	private Label passwordErrorLabel;
	private PasswordTextBox confirmPasswordTextBox;
	private Label confirmPasswordErrorLabel;
	private UsernameValidationHandler usernameValidator = new UsernameValidationHandler();
	private PasswordValidationHandler passwordValidator = new PasswordValidationHandler();
	private EmailValidationHandler emailValidator = new EmailValidationHandler();

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final UserAccountServiceAsync userService = GWT
			.create(UserAccountService.class);
	private TextBox displayNameTextBox;
	private Label displayNameErrorLabel;

	public RegisterUserForm() {

		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		verticalPanel.setHeight("696px");

		Label lblRegisterNewUser = new Label("Register New User");
		lblRegisterNewUser.setStyleName("title");
		lblRegisterNewUser
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(lblRegisterNewUser);

		Label registerUserFormTitle = new Label(
				"Please provide some information:");
		registerUserFormTitle
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		registerUserFormTitle.setStyleName("title2");
		verticalPanel.add(registerUserFormTitle);

		Label lblRequiredField = new Label("* Required field");
		lblRequiredField.setStyleName("small");
		verticalPanel.add(lblRequiredField);

		FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(10);
		verticalPanel.add(flexTable);
		flexTable.setWidth("100%");

		Label lblUsername = new Label("Username *");
		lblUsername.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.setWidget(0, 0, lblUsername);

		usernameTextBox = new TextBox();
		usernameTextBox.addBlurHandler(usernameValidator);
		usernameTextBox.addKeyUpHandler(usernameValidator);
		flexTable.setWidget(0, 1, usernameTextBox);
		flexTable.getCellFormatter().setWidth(0, 1, "");

		usernameErrorLabel = new Label("");
		usernameErrorLabel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		usernameErrorLabel.setStyleName("validationError");
		flexTable.setWidget(0, 2, usernameErrorLabel);
		usernameErrorLabel.setWidth("200");

		Label lblUsernamesShouldBe = new Label(
				"Usernames should be at least 6 characters and may not contain spaces or punctuation");
		lblUsernamesShouldBe.setStyleName("small");
		flexTable.setWidget(1, 1, lblUsernamesShouldBe);

		Label lblFirstName = new Label("First Name");
		lblFirstName.setWordWrap(false);
		lblFirstName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.setWidget(2, 0, lblFirstName);

		firstNameTextBox = new TextBox();
		flexTable.setWidget(2, 1, firstNameTextBox);

		firstNameErrorLabel = new Label("");
		firstNameErrorLabel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		firstNameErrorLabel.setStyleName("validationError");
		flexTable.setWidget(2, 2, firstNameErrorLabel);

		Label lblNewLabel = new Label("Last Name");
		lblNewLabel.setWordWrap(false);
		lblNewLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.setWidget(3, 0, lblNewLabel);

		lastNameTextBox = new TextBox();
		flexTable.setWidget(3, 1, lastNameTextBox);

		lastNameErrorLabel = new Label("");
		lastNameErrorLabel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		lastNameErrorLabel.setStyleName("validationError");
		flexTable.setWidget(3, 2, lastNameErrorLabel);

		Label lblDisplayName = new Label("Display Name");
		lblDisplayName
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.setWidget(4, 0, lblDisplayName);

		displayNameTextBox = new TextBox();
		flexTable.setWidget(4, 1, displayNameTextBox);

		displayNameErrorLabel = new Label("");
		displayNameErrorLabel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		displayNameErrorLabel.setStyleName("validationError");
		flexTable.setWidget(4, 2, displayNameErrorLabel);

		Label lblEmail = new Label("Email *");
		lblEmail.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.setWidget(5, 0, lblEmail);

		emailTextBox = new TextBox();
		emailTextBox.addBlurHandler(emailValidator);
		emailTextBox.addKeyUpHandler(emailValidator);
		flexTable.setWidget(5, 1, emailTextBox);
		flexTable.getCellFormatter().setVerticalAlignment(5, 1,
				HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 1,
				HasHorizontalAlignment.ALIGN_LEFT);

		emailErrorLabel = new Label("");
		emailErrorLabel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		emailErrorLabel.setStyleName("validationError");
		flexTable.setWidget(5, 2, emailErrorLabel);

		Label lblNewLabel_1 = new Label(
				"We will email you a link to activate your account");
		lblNewLabel_1.setStyleName("small");
		lblNewLabel_1.setWordWrap(false);
		flexTable.setWidget(6, 1, lblNewLabel_1);
		flexTable.getFlexCellFormatter().setColSpan(6, 1, 2);

		Label lblPassword = new Label("Password *");
		lblPassword.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.setWidget(7, 0, lblPassword);

		passwordTextBox = new PasswordTextBox();
		passwordTextBox.addBlurHandler(passwordValidator);
		passwordTextBox.addKeyUpHandler(passwordValidator);
		flexTable.setWidget(7, 1, passwordTextBox);

		passwordErrorLabel = new Label("");
		passwordErrorLabel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		passwordErrorLabel.setStyleName("validationError");
		flexTable.setWidget(7, 2, passwordErrorLabel);

		Label lblPasswordMustBe = new Label(
				"Password must be at least 6 characters and contain letters and numbers");
		lblPasswordMustBe.setStyleName("small");
		flexTable.setWidget(8, 1, lblPasswordMustBe);

		Label lblConfirmPassword = new Label("Confirm Password *");
		lblConfirmPassword
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		lblConfirmPassword.setWordWrap(false);
		flexTable.setWidget(9, 0, lblConfirmPassword);

		confirmPasswordTextBox = new PasswordTextBox();
		confirmPasswordTextBox.addBlurHandler(passwordValidator);
		confirmPasswordTextBox.addKeyUpHandler(passwordValidator);
		flexTable.setWidget(9, 1, confirmPasswordTextBox);

		confirmPasswordErrorLabel = new Label("");
		confirmPasswordErrorLabel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		confirmPasswordErrorLabel.setStyleName("validationError");
		flexTable.setWidget(9, 2, confirmPasswordErrorLabel);

		btnSubmit = new Button("Submit");
		btnSubmit.setStyleName("gwt-PushButton-up");
		btnSubmit.addClickHandler(new SubmitButtonHandler());
		flexTable.setWidget(10, 1, btnSubmit);
		flexTable.getFlexCellFormatter().setColSpan(1, 1, 2);
		flexTable.getFlexCellFormatter().setColSpan(8, 1, 2);

	}

	class SubmitButtonHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if (!usernameValidator.validate() || !emailValidator.validate()
					|| !passwordValidator.validate()) {
				return;
			}
			String firstName = firstNameTextBox.getValue();
			String lastName = lastNameTextBox.getValue();
			String displayName = displayNameTextBox.getValue();
			boolean invalid = false;
			if (!UserFieldsValidator.isValidText(firstName)) {
				firstNameErrorLabel.setText("Invalid text");
				invalid = true;
			} else {
				firstNameErrorLabel.setText("");
			}
			if (!UserFieldsValidator.isValidText(lastName)) {
				lastNameErrorLabel.setText("Invalid text");
				invalid = true;
			} else {
				lastNameErrorLabel.setText("");
			}
			if (!UserFieldsValidator.isValidText(displayName)) {
				displayNameErrorLabel.setText("Invalid text");
				invalid = true;
			} else {
				displayNameErrorLabel.setText("");
			}
			if (invalid) {
				return;
			}

			btnSubmit.setEnabled(false);
			String username = usernameTextBox.getValue().trim();
//			userService.search("username: " + username,
//					new AsyncCallback<List<UserAccountDTO>>() {
//
//						public void onFailure(Throwable caught) {
//							new RPCFailureDialog() {
//								@Override
//								public void onClose() {
//									btnSubmit.setEnabled(true);
//								}
//							}.center();
//						}
//
//						public void onSuccess(List<UserAccountDTO> result) {
//							if (result.size() > 0) {
//								new UsernameNotAvailableDialog() {
//									@Override
//									public void onClose() {
//										usernameTextBox.setFocus(true);
//										btnSubmit.setEnabled(true);
//									}
//								}.center();
//							} else {
//								createUser();
//							}
//						}
//					});
		}
	}

	/**
	 * Calls the remote service to create the user.
	 */
	private void createUser() {
		UserAccountDTO representation = new UserAccountDTO();
		representation.setUsername(usernameTextBox.getValue().trim());
		representation.setPassword(passwordTextBox.getValue().trim());
		representation.setEmail(emailTextBox.getValue().trim());
		String firstName = firstNameTextBox.getValue();
		String lastName = lastNameTextBox.getValue();
		String displayName = displayNameTextBox.getValue();
		if (firstName != null) {
			representation.setFirstName(firstName.trim());
		}
		if (lastName != null) {
			representation.setLastName(lastName.trim());
		}
		if (displayName != null) {
			representation.setDisplayName(displayName.trim());
		}
		
		userService.create(representation,
				new AsyncCallback<UserAccountDTO>() {

					public void onFailure(Throwable caught) {
						new RPCFailureDialog() {
							@Override
							public void onClose() {
								btnSubmit.setEnabled(true);
							}
						}.center();
					}

					public void onSuccess(UserAccountDTO newUser) {
						onCreateUser(newUser);
					}
				});
	}

	public abstract void onCreateUser(UserAccountDTO newUser);

	class UsernameValidationHandler implements KeyUpHandler, BlurHandler {
		public void onBlur(BlurEvent event) {
			validate();
		}

		public void onKeyUp(KeyUpEvent event) {
			if (usernameTextBox.getValue().trim().length() > 5) {
				validate();
			}
		}

		private boolean validate() {
			if (!UserFieldsValidator
					.isValidUsername(usernameTextBox.getValue())) {
				usernameErrorLabel.setText("Invalid username");
				return false;
			} else {
				usernameErrorLabel.setText("");
			}
			return true;
		}

	}

	class PasswordValidationHandler implements KeyUpHandler, BlurHandler {
		public void onBlur(BlurEvent event) {
			validate();
		}

		public void onKeyUp(KeyUpEvent event) {
			validate();
		}

		private boolean validate() {
			if (UserFieldsValidator.isNull(passwordTextBox.getValue())) {
				passwordErrorLabel.setText("Password is required");
				return false;
			} else {
				passwordErrorLabel.setText("");
			}

			if (!UserFieldsValidator
					.isValidPassword(passwordTextBox.getValue())) {
				passwordErrorLabel.setText("Invalid password");
				return false;
			} else {
				passwordErrorLabel.setText("");
			}

			if (!UserFieldsValidator.isEqual(passwordTextBox.getValue(),
					confirmPasswordTextBox.getValue())) {
				confirmPasswordErrorLabel.setText("Passwords do not match");
				return false;
			} else {
				confirmPasswordErrorLabel.setText("");
			}
			return true;
		}
	}

	class EmailValidationHandler implements KeyUpHandler, BlurHandler {
		public void onBlur(BlurEvent event) {
			validate();
		}

		public void onKeyUp(KeyUpEvent event) {
			if (emailErrorLabel.getText().length() > 0) {
				validate();
			}
		}

		private boolean validate() {
			if (!UserFieldsValidator.isValidEmail(emailTextBox.getValue())) {
				emailErrorLabel.setText("Invalid email address");
				return false;
			} else {
				emailErrorLabel.setText("");
			}
			return true;
		}
	}
}
