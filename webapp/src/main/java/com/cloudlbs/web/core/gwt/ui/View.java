package com.cloudlbs.web.core.gwt.ui;

import com.cloudlbs.web.core.gwt.ui.wrapper.Wrapper;
import com.google.gwt.user.client.ui.Widget;

public interface View {

    /**
     * Toggles a "working" indicator, if one is present.
     * 
     * @param isWorking
     */
    void setWorking(boolean isWorking);

    /**
     * Displays an alert to the user.
     * 
     * @param msg
     */
    void alert(String msg);

    Widget asWidget();
    Wrapper getWrapper();
}
