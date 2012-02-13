package com.cloudlbs.web.core.gwt;

import com.cloudlbs.web.core.gwt.ui.wrapper.Wrapper;
import com.google.gwt.user.client.ui.Widget;

public interface View {
    Widget asWidget();
    void setWorking(boolean isWorking);
    void alert(String msg);
    Wrapper getWrapper();
}
