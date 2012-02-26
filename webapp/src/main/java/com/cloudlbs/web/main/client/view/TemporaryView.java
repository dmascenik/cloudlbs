package com.cloudlbs.web.main.client.view;

import com.cloudlbs.web.core.gwt.View;

public interface TemporaryView<T> extends View {

    /*
     * Things the view can ask the presenter to do
     */
    public interface Presenter<T> {
        void logout();
    }

    /*
     * Things the presenter can ask the view to do
     */
    void setPresenter(Presenter<T> presenter);
}
