package com.cloudlbs.web.pub.client.view;

import com.cloudlbs.web.core.gwt.ui.View;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;
import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;

@Singleton
@ImplementedBy(NewUserFormImpl.class)
public interface NewUserForm extends View {

    /*
     * Things the view can ask the presenter to do
     */
    public interface Presenter {
        void onSubmitClicked();
        void onCancelClicked();
    }

    /*
     * Things the presenter can ask the view to do
     */
    void setPresenter(Presenter presenter);
    NewUserDetails getNewUserDetails();

}
