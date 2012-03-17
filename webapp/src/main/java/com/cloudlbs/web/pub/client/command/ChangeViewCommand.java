package com.cloudlbs.web.pub.client.command;

import com.cloudlbs.web.pub.client.AppController;
import com.cloudlbs.web.pub.client.event.ChangeViewEvent;
import com.cloudlbs.web.pub.client.event.ChangeViewEventHandler;
import com.google.gwt.user.client.History;

/**
 * Handles a view change event by adding a new item to the history. This is in
 * turn handled by the {@link AppController}, which listens for history change
 * events.
 * 
 * @see AppController#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
 * 
 * @author danmascenik
 * 
 */
public class ChangeViewCommand implements ChangeViewEventHandler {

    @Override
    public void doChangeView(ChangeViewEvent event) {
        History.newItem(event.getHistoryToken());
    }

}
