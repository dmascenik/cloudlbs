package com.cloudlbs.web.noauth.client.command;

import com.cloudlbs.web.noauth.client.AppController;
import com.cloudlbs.web.noauth.client.event.CancelCreateUserEvent;
import com.cloudlbs.web.noauth.client.event.CancelCreateUserEventHandler;
import com.google.gwt.user.client.History;

public class CancelNewUserCommand implements CancelCreateUserEventHandler {

	@Override
	public void onCancelCreateUser(CancelCreateUserEvent event) {
		History.newItem(AppController.HISTORY_LOGIN);
	}

}
