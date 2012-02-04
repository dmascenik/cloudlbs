package com.cloudlbs.web.noauth.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface CreateUserEventHandler extends EventHandler {
	void onCreateUser(CreateUserEvent event);
}
