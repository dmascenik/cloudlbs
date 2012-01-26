package com.cloudlbs.web.client;

import com.cloudlbs.web.client.composite.RegisterUserForm;
import com.cloudlbs.web.client.composite.RegisterUserSuccess;
import com.cloudlbs.web.shared.dto.UserAccountDTO;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Dan Mascenik
 * 
 */
public class RegisterUserEntryPoint implements EntryPoint {
	private RegisterUserForm registerUserForm;
	private RootPanel rootPanel;
	private VerticalPanel verticalPanel;

	public void onModuleLoad() {
		rootPanel = RootPanel.get();

		verticalPanel = new VerticalPanel();
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		verticalPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		rootPanel.add(verticalPanel, 0, 1);
		verticalPanel.setSize("100%", "100%");

		registerUserForm = new RegisterUserForm() {

			@Override
			public void onCreateUser(UserAccountDTO newUser) {
				displaySuccess(newUser.getEmail());
			}

		};
		verticalPanel.add(registerUserForm);
	}

	private void displaySuccess(String email) {
		RegisterUserSuccess registerUserSuccess = new RegisterUserSuccess(email);
		verticalPanel.add(registerUserSuccess);
		registerUserForm.setVisible(false);
	}

}
