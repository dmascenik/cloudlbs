package com.cloudlbs.web.pub.client.presenter;

import com.cloudlbs.web.core.gwt.ui.BaseAsyncCallback;
import com.cloudlbs.web.core.gwt.ui.Presenter;
import com.cloudlbs.web.i18n.msg.Messages;
import com.cloudlbs.web.pub.client.event.CancelCreateUserEvent;
import com.cloudlbs.web.pub.client.rpc.RPCUserServiceAsync;
import com.cloudlbs.web.pub.client.view.NewUserForm;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class NewUserFormPresenter implements Presenter, NewUserForm.Presenter<NewUserDetails> {

    private RPCUserServiceAsync userService;
    private HandlerManager eventBus;
    private NewUserForm<NewUserDetails> view;
    private Messages messages;

    @Inject
    public NewUserFormPresenter(HandlerManager eventBus, NewUserForm<NewUserDetails> view,
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
        eventBus.fireEvent(new CancelCreateUserEvent());
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
    }

}
