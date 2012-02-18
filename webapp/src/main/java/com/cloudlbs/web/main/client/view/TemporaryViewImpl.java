package com.cloudlbs.web.main.client.view;

import com.cloudlbs.web.core.gwt.BaseViewImpl;
import com.cloudlbs.web.core.gwt.ui.wrapper.StandardPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
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

    @Inject
    public TemporaryViewImpl(StandardPanel wrapper) {
        super(wrapper);
        initWidget(uiBinder.createAndBindUi(this));
        wrapper.setContents(this);
    }

}
