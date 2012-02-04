package com.cloudlbs.web.noauth.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LoginSubmitEventHandler extends EventHandler {
	void onLoginSubmit(LoginSubmitEvent event);
}
