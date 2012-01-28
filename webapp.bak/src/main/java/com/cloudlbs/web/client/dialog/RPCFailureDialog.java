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
public abstract class RPCFailureDialog extends DialogBox {

	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	public RPCFailureDialog() {
		setHTML("This is embarrassing...");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(10);
		setWidget(horizontalPanel);
		horizontalPanel.setSize("100%", "100%");

		Label lblNewLabel = new Label(SERVER_ERROR);
		lblNewLabel.setStyleName("validationError");
		lblNewLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		horizontalPanel.add(lblNewLabel);
		lblNewLabel.setWidth("300");
		horizontalPanel.setCellVerticalAlignment(lblNewLabel,
				HasVerticalAlignment.ALIGN_MIDDLE);

		Button btnClose = new Button("Close");
		horizontalPanel.add(btnClose);
		btnClose.setFocus(true);
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
