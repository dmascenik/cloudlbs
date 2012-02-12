package com.cloudlbs.web.noauth.client.view;

import com.cloudlbs.web.noauth.shared.model.NewUserDetails;
import com.google.gwt.user.client.ui.Widget;

public interface NewUserForm<T> {

    public interface Presenter<T> {
        void onSubmitClicked();
        void onCancelClicked();
    }

    void setPresenter(Presenter<T> presenter);
    NewUserDetails getNewUserDetails();
    Widget asWidget();
}
