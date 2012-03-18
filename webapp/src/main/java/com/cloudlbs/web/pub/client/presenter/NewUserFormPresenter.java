package com.cloudlbs.web.pub.client.presenter;

import com.cloudlbs.web.core.gwt.ui.BaseAsyncCallback;
import com.cloudlbs.web.core.gwt.ui.Presenter;
import com.cloudlbs.web.i18n.msg.Messages;
import com.cloudlbs.web.pub.client.AppController.HistoryToken;
import com.cloudlbs.web.pub.client.event.ChangeViewEvent;
import com.cloudlbs.web.pub.client.rpc.RPCUserServiceAsync;
import com.cloudlbs.web.pub.client.view.NewUserForm;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class NewUserFormPresenter implements Presenter, NewUserForm.Presenter {

    private RPCUserServiceAsync userService;
    private HandlerManager eventBus;
    private NewUserForm view;
    private Messages messages;

    @Inject
    public NewUserFormPresenter(HandlerManager eventBus, NewUserForm view,
            RPCUserServiceAsync userService, Messages messages) {
        this.eventBus = eventBus;
        this.view = view;
        this.view.setPresenter(this);
        this.userService = userService;
        this.messages = messages;
    }

    @Override
    public void onSubmitClicked() {
        NewUserDetails userDetails = view.getNewUserDetails();
        userService.createUser(userDetails, new BaseAsyncCallback<Boolean>(view, messages) {
            @Override
            public void success(Boolean result) {
                if (!result) {
                    view.alert(messages.unknownError());
                } else {
                    view.alert("Success!");
                }
            }
        });
    }

    @Override
    public void onCancelClicked() {
        eventBus.fireEvent(new ChangeViewEvent(HistoryToken.LOGIN));
    }

    @Override
    public void go(HasWidgets container) {
        view.clearForm();
        container.clear();
        container.add(view.asWidget());
    }

}
