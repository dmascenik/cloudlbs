package com.cloudlbs.web.main.client.view;

import com.cloudlbs.web.core.gwt.BaseViewImpl;
import com.cloudlbs.web.core.gwt.ui.wrapper.StandardPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * 
 * @author danmascenik
 * 
 */
public class TemporaryViewImpl<T> extends BaseViewImpl implements TemporaryView<T> {

    @UiTemplate("TemporaryView.ui.xml")
    interface Binder extends UiBinder<Widget, TemporaryViewImpl<?>> {
    }

    private static Binder uiBinder = GWT.create(Binder.class);
    private Presenter<T> presenter;
    
    @UiField Button logoutButton;

    @Inject
    public TemporaryViewImpl(StandardPanel wrapper) {
        super(wrapper);
        initWidget(uiBinder.createAndBindUi(this));
        wrapper.setContents(this);
    }

    @UiHandler("logoutButton")
    void onLogout(ClickEvent event) {
        presenter.logout();
    }

    @Override
    public void setPresenter(Presenter<T> presenter) {
        this.presenter = presenter;
    }

}
