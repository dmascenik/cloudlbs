package com.cloudlbs.web.main.client.presenter;

import com.cloudlbs.web.core.gwt.Presenter;
import com.cloudlbs.web.main.client.view.TemporaryView;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class TemporaryViewPresenter implements Presenter {

    private TemporaryView<String> view;

    @Inject
    public TemporaryViewPresenter(TemporaryView<String> view) {
        this.view = view;
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
    }

}
