package com.cloudlbs.web.noauth.client.command;

import com.cloudlbs.web.noauth.client.event.LoginSubmitEvent;
import com.cloudlbs.web.noauth.client.event.LoginSubmitEventHandler;

public class LoginSubmitCommand implements LoginSubmitEventHandler {

	@Override
	public void onLoginSubmit(LoginSubmitEvent event) {
		// TODO
		System.out
				.println("Logging in " + event.getCredentials().getUsername());

	}

}
