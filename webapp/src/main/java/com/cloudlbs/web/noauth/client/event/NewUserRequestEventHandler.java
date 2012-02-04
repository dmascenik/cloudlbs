package com.cloudlbs.web.noauth.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface NewUserRequestEventHandler extends EventHandler {
	void onNewUserRequest(NewUserRequestEvent event);
}
