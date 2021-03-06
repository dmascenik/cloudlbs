package com.cloudlbs.web.main.client;

import com.cloudlbs.web.core.gwt.ui.Presenter;
import com.cloudlbs.web.main.client.presenter.TemporaryViewPresenter;
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
 * <li>Create a new <code>HISTORY_xxxx</code> value in this class, and handle it
 * in the {@link #onValueChange(ValueChangeEvent)} method</li>
 * <li><code>@Inject</code> a Command-pattern class for each application-wide
 * event sourced from the new presenter in the constructor</li>
 * </ul>
 * 
 * @author danmascenik
 * 
 */
public class AppController implements Presenter, ValueChangeHandler<String> {

    public static final String HISTORY_DEFAULT = "def";

//    private final HandlerManager eBus;
    private HasWidgets container;

    /*
     * Providers lazy-load when Provider.get() is called
     */
    @Inject private Provider<TemporaryViewPresenter> temporaryViewProvider;

//    @Inject private Provider<NewUserFormPresenter> newUserFormProvider;

    /**
     * Configures the event bus by matching Command-patterned classes with the
     * appropriate event types. This could have been done in the GIN module
     * configuration, but putting it here keeps the app behavior configuration
     * in one place.
     */
    @Inject
    public AppController(HandlerManager eventBus) {

//            , NewUserCommand newUserCommand,
//            CancelNewUserCommand cancelNewUserCommand) {
//        this.eBus = eventBus;
        History.addValueChangeHandler(this);
//        eBus.addHandler(NewUserRequestEvent.TYPE, newUserCommand);
//        eBus.addHandler(CancelCreateUserEvent.TYPE, cancelNewUserCommand);
    }

    /**
     * Calls {@link Presenter#go(HasWidgets)} on the presenter for the incoming
     * {@link History} token.
     */
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();
        if (token != null) {
            if (token.equals(HISTORY_DEFAULT)) {
                temporaryViewProvider.get().go(container);
//            } else if (token.equals(HISTORY_NEW_USER)) {
//                newUserFormProvider.get().go(container);
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
            History.newItem(HISTORY_DEFAULT);
        } else {
            History.fireCurrentHistoryState();
        }
    }

}
