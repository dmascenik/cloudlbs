package com.cloudlbs.web.core.gwt;

import com.cloudlbs.web.core.gwt.ui.wrapper.Wrapper;
import com.google.common.base.Preconditions;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides some default functionality for a view that exists within a wrapper.
 * In particular, the default behavior for showing an alert and for showing a
 * "working" indicator (while an async call is pending) is to simply use
 * whatever the {@link Wrapper} provides.
 * 
 * @see Wrapper
 * @author danmascenik
 * 
 */
public abstract class BaseViewImpl extends Composite implements View {

    private Wrapper wrapper;

    public BaseViewImpl(Wrapper wrapper) {
        Preconditions.checkNotNull(wrapper, "Wrapper cannot be null");
        this.wrapper = wrapper;
    }

    /**
     * Defaults to showing the {@link Wrapper}'s working indicator. This may be
     * overridden to provide a view-specific indicator.
     */
    @Override
    public void setWorking(boolean isWorking) {
        wrapper.setWorking(isWorking);
    }

    /**
     * Defaults to showing the {@link Wrapper}'s style of alert. This may be
     * overridden to provide a view-specific alert.
     */
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
