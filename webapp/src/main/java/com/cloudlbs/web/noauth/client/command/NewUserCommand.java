package com.cloudlbs.web.noauth.client.command;

import com.cloudlbs.web.noauth.client.AppController;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEvent;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEventHandler;
import com.google.gwt.user.client.History;

public class NewUserCommand implements NewUserRequestEventHandler {

	@Override
	public void onNewUserRequest(NewUserRequestEvent event) {
		History.newItem(AppController.HISTORY_NEW_USER);
	}

}
