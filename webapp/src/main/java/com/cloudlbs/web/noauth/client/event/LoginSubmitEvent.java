package com.cloudlbs.web.noauth.client.event;

import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.google.gwt.event.shared.GwtEvent;

public class LoginSubmitEvent extends GwtEvent<LoginSubmitEventHandler> {
	public static Type<LoginSubmitEventHandler> TYPE = new Type<LoginSubmitEventHandler>();

	private LoginCredentials credentials;

	public LoginSubmitEvent(LoginCredentials credentials) {
		this.credentials = credentials;
	}

	public LoginCredentials getCredentials() {
		return credentials;
	}

	@Override
	public Type<LoginSubmitEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginSubmitEventHandler handler) {
		handler.onLoginSubmit(this);
	}

}
