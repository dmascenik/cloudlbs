package com.cloudlbs.web.core.gwt.ui.wrapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;

public class StandardPanelImpl extends Composite implements StandardPanel {

    @UiTemplate("StandardPanel.ui.xml")
    interface Binder extends UiBinder<Widget, StandardPanelImpl> {
    }

    private static Binder uiBinder = GWT.create(Binder.class);

    @UiField SimplePanel panel;
    @UiField DialogBox alertBox;
    @UiField Label alertMessage;
    @UiField Button alertCloseButton;

    public StandardPanelImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        alertBox.hide();
    }

    @UiHandler("alertCloseButton")
    public void onAlertCloseButtonClick(ClickEvent click) {
        alertBox.hide();
    }

    @Override
    public void setContents(Widget contents) {
        panel.clear();
        panel.add(contents);
    }

    @Override
    public void setWorking(boolean isWorking) {
        // TODO Auto-generated method stub

    }

    @Override
    public void alert(String msg) {
        alertMessage.setText(msg);
        alertBox.center();
    }

    @Override
    public Wrapper getWrapper() {
        return this;
    }

}
