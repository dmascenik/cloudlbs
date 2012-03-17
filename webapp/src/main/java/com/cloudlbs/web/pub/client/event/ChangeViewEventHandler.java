package com.cloudlbs.web.pub.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handles an event that requires changing the currently displayed view.
 * 
 * @author danmascenik
 * 
 */
public interface ChangeViewEventHandler extends EventHandler {
    void doChangeView(ChangeViewEvent event);
}
