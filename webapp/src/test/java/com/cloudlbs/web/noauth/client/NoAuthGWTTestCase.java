package com.cloudlbs.web.noauth.client;

import com.cloudlbs.web.noauth.client.view.NoAuthTestingGinjector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.junit.client.GWTTestCase;

public abstract class NoAuthGWTTestCase extends GWTTestCase {

    protected NoAuthTestingGinjector injector;
    protected HandlerManager eventBus;

    @Override
    protected void gwtSetUp() throws Exception {
        injector = GWT.create(NoAuthTestingGinjector.class);
        eventBus = injector.getEventBus();
    }

    @Override
    protected void gwtTearDown() throws Exception {
        injector = null;
        eventBus = null;
    }

    @Override
    public String getModuleName() {
        return "com.cloudlbs.web.noauth.NoAuth";
    }

}
