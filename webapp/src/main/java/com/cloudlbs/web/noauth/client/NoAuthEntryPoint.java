package com.cloudlbs.web.noauth.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The entry point class which performs the initial loading of the NoAuth
 * application.
 * 
 * @author danmascenik
 */
public class NoAuthEntryPoint implements EntryPoint {

	interface Binder extends UiBinder<Widget, NoAuthEntryPoint> {
	}

	@UiField
	SimplePanel leftPanel;

	@UiField
	SimplePanel rightPanel;

	/**
	 * This method sets up the top-level services used by the application.
	 */
	public void onModuleLoad() {
		RootLayoutPanel.get().add(
				GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}

}
