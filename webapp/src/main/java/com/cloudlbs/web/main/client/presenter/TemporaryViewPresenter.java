package com.cloudlbs.web.main.client.presenter;

import com.cloudlbs.web.core.gwt.Presenter;
import com.cloudlbs.web.main.client.view.TemporaryView;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class TemporaryViewPresenter implements Presenter, TemporaryView.Presenter<String> {

    private TemporaryView<String> view;

    @Inject
    public TemporaryViewPresenter(TemporaryView<String> view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void logout() {
        /*
         * Matches the configured logout filter URL from the Spring Security
         * setup
         */
        Window.Location.replace("logout");
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
    }

}
