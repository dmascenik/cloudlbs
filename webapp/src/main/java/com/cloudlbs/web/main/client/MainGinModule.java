package com.cloudlbs.web.main.client;

import com.cloudlbs.web.main.client.view.TemporaryView;
import com.cloudlbs.web.main.client.view.TemporaryViewImpl;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

public class MainGinModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bindViews();
    }

    @Provides
    @Singleton
    HandlerManager getEventBus() {
        return new HandlerManager(null);
    }

    /**
     * Views use generics, so their bindings require TypeLiteral and can become
     * rather verbose.
     */
    protected void bindViews() {
        bind(new TypeLiteral<TemporaryView<String>>() {
        }).to(new TypeLiteral<TemporaryViewImpl<String>>() {
        }).in(Singleton.class);
    }

}
