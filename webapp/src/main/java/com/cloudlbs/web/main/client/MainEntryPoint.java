package com.cloudlbs.web.main.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * The entry point class which performs the initial loading of the Main
 * application.
 * 
 * @author danmascenik
 */
public class MainEntryPoint implements EntryPoint {

	private MainGinjector injector = GWT.create(MainGinjector.class);

	public void onModuleLoad() {
		injector.getAppController().go(RootPanel.get());
	}

}
