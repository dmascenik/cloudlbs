package com.cloudlbs.web.pub.client;

import com.cloudlbs.web.core.gwt.ui.Presenter;
import com.cloudlbs.web.pub.client.command.ChangeViewCommand;
import com.cloudlbs.web.pub.client.event.ChangeViewEvent;
import com.cloudlbs.web.pub.client.presenter.LoginFormPresenter;
import com.cloudlbs.web.pub.client.presenter.NewUserFormPresenter;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * This is the central control point for the GWT module. It configures the
 * application-wide event bus, and the loading of MVP presenters on history
 * token changes (this is how bookmarked "places" are handled).<br/>
 * <br/>
 * 
 * To add a new, bookmarkable view to the app:
 * <ul>
 * <li>Create a view interface, implementation class, and UiBinder XML file</li>
 * <li>Bind the view interface to its implementation in the GIN module
 * configuration</li>
 * <li>Create a presenter class for the view, and <code>@Inject</code> a
 * {@link Provider} for it in this class</li>
 * </ul>
 * 
 * @author danmascenik
 * 
 */
public class AppController implements Presenter, ValueChangeHandler<String> {

    private final HandlerManager eBus;
    private HasWidgets container;

    /*
     * Providers lazy-load when Provider.get() is called
     */
    @Inject private Provider<LoginFormPresenter> loginFormProvider;
    @Inject private Provider<NewUserFormPresenter> newUserFormProvider;

    /**
     * Configures the event bus by matching Command-patterned classes with the
     * appropriate event types. This could have been done in the GIN module
     * configuration, but putting it here keeps the app behavior configuration
     * in one place.
     */
    @Inject
    public AppController(HandlerManager eventBus, ChangeViewCommand changeViewCommand) {
        this.eBus = eventBus;
        History.addValueChangeHandler(this);
        eBus.addHandler(ChangeViewEvent.TYPE, changeViewCommand);
    }

    /**
     * All the history tokens that are handled by the {@link AppController}
     * 
     * @author danmascenik
     * 
     */
    public enum HistoryToken {
        LOGIN("login"), NEW_USER("newuser");

        private String token;

        HistoryToken(String token) {
            this.token = token;
        }
        public String asToken() {
            return this.token;
        }
    }

    /**
     * Calls {@link Presenter#go(HasWidgets)} on the presenter for the incoming
     * {@link History} token.
     */
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();
        if (token != null) {
            if (token.equals(HistoryToken.LOGIN.asToken())) {
                loginFormProvider.get().go(container);
            } else if (token.equals(HistoryToken.NEW_USER.asToken())) {
                newUserFormProvider.get().go(container);
            }
        }
    }

    /**
     * Dispatches to another {@link Presenter}'s
     * {@link Presenter#go(HasWidgets)} method via the
     * {@link #onValueChange(ValueChangeEvent)} method by firing a
     * {@link History} event.
     */
    @Override
    public void go(HasWidgets container) {
        this.container = container;
        if ("".equals(History.getToken())) {
            History.newItem(HistoryToken.LOGIN.asToken());
        } else {
            History.fireCurrentHistoryState();
        }
    }

}
