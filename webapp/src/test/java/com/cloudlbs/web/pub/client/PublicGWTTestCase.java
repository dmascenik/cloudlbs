package com.cloudlbs.web.pub.client;

import com.cloudlbs.web.pub.client.view.PublicTestingGinjector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.junit.client.GWTTestCase;

public abstract class PublicGWTTestCase extends GWTTestCase {

    protected PublicTestingGinjector injector;
    protected HandlerManager eventBus;

    @Override
    protected void gwtSetUp() throws Exception {
        injector = GWT.create(PublicTestingGinjector.class);
        eventBus = injector.getEventBus();
    }

    @Override
    protected void gwtTearDown() throws Exception {
        injector = null;
        eventBus = null;
    }

    @Override
    public String getModuleName() {
        return "com.cloudlbs.web.pub.Public";
    }

}
