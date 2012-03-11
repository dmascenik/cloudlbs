package com.cloudlbs.web.core.gwt;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Base GWT entry point class that ensures the CSS is injected when the module
 * loads.
 * 
 * @author danmascenik
 * 
 */
public abstract class CloudLBSEntryPoint implements EntryPoint {

    @Override
    public final void onModuleLoad() {
        GWT.<Resources> create(Resources.class).css().ensureInjected();
        doOnModuleLoad();
    }

    public abstract void doOnModuleLoad();

}
