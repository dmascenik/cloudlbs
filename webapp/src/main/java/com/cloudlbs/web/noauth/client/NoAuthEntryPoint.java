package com.cloudlbs.web.noauth.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * The entry point class which performs the initial loading of the NoAuth
 * application.
 * 
 * @author danmascenik
 */
public class NoAuthEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		HandlerManager eventBus = new HandlerManager(null);
		AppController appViewer = new AppController(eventBus);
		appViewer.go(RootPanel.get());
	}

}
