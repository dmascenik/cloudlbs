package com.cloudlbs.web.pub.client.event;

import com.cloudlbs.web.pub.client.AppController.HistoryToken;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating that the currently displayed view needs to be changed.
 * 
 * @author danmascenik
 * 
 */
public class ChangeViewEvent extends GwtEvent<ChangeViewEventHandler> {
    public static Type<ChangeViewEventHandler> TYPE = new Type<ChangeViewEventHandler>();

    private HistoryToken token;

    public ChangeViewEvent(HistoryToken token) {
        this.token = token;
    }

    public String getHistoryToken() {
        return token.asToken();
    }

    @Override
    public Type<ChangeViewEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ChangeViewEventHandler handler) {
        handler.doChangeView(this);
    }

}
