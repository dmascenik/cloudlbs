package com.cloudlbs.web.core.gwt;

import com.cloudlbs.web.core.gwt.ui.wrapper.Wrapper;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public abstract class BaseViewImpl extends Composite implements View {

    private Wrapper wrapper;

    public BaseViewImpl(Wrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public void setWorking(boolean isWorking) {
        wrapper.setWorking(isWorking);
    }

    @Override
    public void alert(String msg) {
        wrapper.alert(msg);
    }

    @Override
    public Wrapper getWrapper() {
        return wrapper;
    }

    @Override
    public Widget asWidget() {
        return wrapper.asWidget();
    }

}
