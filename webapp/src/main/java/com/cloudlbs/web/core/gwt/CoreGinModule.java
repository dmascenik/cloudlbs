package com.cloudlbs.web.core.gwt;

import com.cloudlbs.web.core.gwt.ui.wrapper.StandardPanel;
import com.cloudlbs.web.core.gwt.ui.wrapper.StandardPanelImpl;
import com.google.gwt.inject.client.AbstractGinModule;

public class CoreGinModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(StandardPanel.class).to(StandardPanelImpl.class);
    }

}
