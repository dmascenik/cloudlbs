package com.cloudlbs.web.pub.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class NewUserRequestEvent extends GwtEvent<NewUserRequestEventHandler> {
	public static Type<NewUserRequestEventHandler> TYPE = new Type<NewUserRequestEventHandler>();

	@Override
	public Type<NewUserRequestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NewUserRequestEventHandler handler) {
		handler.onNewUserRequest(this);
	}

}
