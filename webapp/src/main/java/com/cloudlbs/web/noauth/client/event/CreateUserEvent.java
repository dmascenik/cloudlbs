package com.cloudlbs.web.noauth.client.event;

import com.cloudlbs.web.noauth.shared.model.NewUserDetails;
import com.google.gwt.event.shared.GwtEvent;

public class CreateUserEvent extends GwtEvent<CreateUserEventHandler> {
	public static Type<CreateUserEventHandler> TYPE = new Type<CreateUserEventHandler>();

	private NewUserDetails userDetails;

	public CreateUserEvent(NewUserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public NewUserDetails getUserDetails() {
		return userDetails;
	}

	@Override
	public Type<CreateUserEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CreateUserEventHandler handler) {
		handler.onCreateUser(this);
	}

}
