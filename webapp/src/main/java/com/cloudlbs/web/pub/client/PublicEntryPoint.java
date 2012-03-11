package com.cloudlbs.web.pub.client;

import com.cloudlbs.web.core.gwt.CloudLBSEntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * The entry point class which performs the initial loading of the public
 * application.
 * 
 * @author danmascenik
 */
public class PublicEntryPoint extends CloudLBSEntryPoint {

    private PublicGinjector injector = GWT.create(PublicGinjector.class);

    public void doOnModuleLoad() {
        injector.getAppController().go(RootPanel.get());
    }

}
