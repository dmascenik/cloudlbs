package com.cloudlbs.web.core.gwt.ui.wrapper;

import com.cloudlbs.web.core.gwt.ui.View;
import com.google.gwt.user.client.ui.Widget;

/**
 * Wrapper views provide consistent user experience around things like alert
 * dialogs and view changes when an asynchronous call is pending.
 * 
 * @author danmascenik
 * 
 */
public interface Wrapper extends View {
    void setContents(Widget contents);
}
