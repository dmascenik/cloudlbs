package com.cloudlbs.web.client.composite;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * @author Dan Mascenik
 * 
 */
public class RegisterUserSuccess extends Composite {

	public RegisterUserSuccess(String email) {

		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);

		Label lblSuccess = new Label("Success!");
		lblSuccess.setStyleName("title");
		verticalPanel.add(lblSuccess);

		Label lblAnEmailHas = new Label("An email has been sent to " + email
				+ " with a link to activate your account.");
		lblAnEmailHas.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		verticalPanel.add(lblAnEmailHas);
		lblAnEmailHas.setWidth("450px");

		Button btnOk = new Button("Ok");
		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				// TODO return to the home page
				
			}
		});
		verticalPanel.add(btnOk);
		verticalPanel.setCellHorizontalAlignment(btnOk,
				HasHorizontalAlignment.ALIGN_CENTER);
		btnOk.setWidth("92px");
	}

}
