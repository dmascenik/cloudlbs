package com.cloudlbs.web.pub.client.view;

import com.cloudlbs.web.core.gwt.ui.View;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;

public interface NewUserForm<T> extends View {

    /*
     * Things the view can ask the presenter to do
     */
    public interface Presenter<T> {
        void onSubmitClicked();
        void onCancelClicked();
    }

    /*
     * Things the presenter can ask the view to do
     */
    void setPresenter(Presenter<T> presenter);
    NewUserDetails getNewUserDetails();

}
