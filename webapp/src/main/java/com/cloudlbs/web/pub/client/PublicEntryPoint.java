package com.cloudlbs.web.pub.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * The entry point class which performs the initial loading of the NoAuth
 * application.
 * 
 * @author danmascenik
 */
public class PublicEntryPoint implements EntryPoint {

	private PublicGinjector injector = GWT.create(PublicGinjector.class);

	public void onModuleLoad() {
		injector.getAppController().go(RootPanel.get());
	}

}
