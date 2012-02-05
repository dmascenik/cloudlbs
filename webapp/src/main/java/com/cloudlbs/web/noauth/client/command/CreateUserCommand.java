package com.cloudlbs.web.noauth.client.command;

import com.cloudlbs.web.noauth.client.event.CreateUserEvent;
import com.cloudlbs.web.noauth.client.event.CreateUserEventHandler;

public class CreateUserCommand implements CreateUserEventHandler {

	@Override
	public void onCreateUser(CreateUserEvent event) {
		// TODO
		System.out.println("Creating user  "
				+ event.getUserDetails().getUsername());
	}

}
