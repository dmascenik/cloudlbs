package com.cloudlbs.web.client.dialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * @author Dan Mascenik
 * 
 */
public abstract class UsernameNotAvailableDialog extends DialogBox {

	private Button btnClose;

	public UsernameNotAvailableDialog() {
		setHTML("Username Not Available");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(10);
		setWidget(horizontalPanel);
		horizontalPanel.setSize("100%", "100%");

		Label lblNewLabel = new Label(
				"Sorry, the username you entered is not available.");
		lblNewLabel.setStyleName("validationError");
		lblNewLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		horizontalPanel.add(lblNewLabel);
		horizontalPanel.setCellVerticalAlignment(lblNewLabel,
				HasVerticalAlignment.ALIGN_MIDDLE);

		btnClose = new Button("Close");
		horizontalPanel.add(btnClose);
		horizontalPanel.setCellVerticalAlignment(btnClose,
				HasVerticalAlignment.ALIGN_BOTTOM);
		horizontalPanel.setCellHorizontalAlignment(btnClose,
				HasHorizontalAlignment.ALIGN_RIGHT);

		btnClose.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				hide();
				onClose();
			}
		});
	}

	public abstract void onClose();

}
