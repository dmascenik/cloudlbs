package com.cloudlbs.web.pub.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class CancelCreateUserEvent extends GwtEvent<CancelCreateUserEventHandler> {

    public static Type<CancelCreateUserEventHandler> TYPE = new Type<CancelCreateUserEventHandler>();

    @Override
    public Type<CancelCreateUserEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CancelCreateUserEventHandler handler) {
        handler.onCancelCreateUser(this);
    }

}
