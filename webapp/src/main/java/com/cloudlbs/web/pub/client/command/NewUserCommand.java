package com.cloudlbs.web.pub.client.command;

import com.cloudlbs.web.pub.client.AppController;
import com.cloudlbs.web.pub.client.event.NewUserRequestEvent;
import com.cloudlbs.web.pub.client.event.NewUserRequestEventHandler;
import com.google.gwt.user.client.History;

public class NewUserCommand implements NewUserRequestEventHandler {

	@Override
	public void onNewUserRequest(NewUserRequestEvent event) {
		History.newItem(AppController.HISTORY_NEW_USER);
	}

}
