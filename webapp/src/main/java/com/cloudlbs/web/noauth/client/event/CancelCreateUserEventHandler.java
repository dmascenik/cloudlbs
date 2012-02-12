package com.cloudlbs.web.noauth.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface CancelCreateUserEventHandler extends EventHandler {
    void onCancelCreateUser(CancelCreateUserEvent event);
}
