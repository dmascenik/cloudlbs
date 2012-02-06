package com.cloudlbs.web.noauth.client;

import com.cloudlbs.web.core.gwt.Presenter;
import com.cloudlbs.web.noauth.client.command.CancelNewUserCommand;
import com.cloudlbs.web.noauth.client.command.CreateUserCommand;
import com.cloudlbs.web.noauth.client.command.LoginSubmitCommand;
import com.cloudlbs.web.noauth.client.command.NewUserCommand;
import com.cloudlbs.web.noauth.client.event.CancelCreateUserEvent;
import com.cloudlbs.web.noauth.client.event.CreateUserEvent;
import com.cloudlbs.web.noauth.client.event.LoginSubmitEvent;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEvent;
import com.cloudlbs.web.noauth.client.presenter.LoginFormPresenter;
import com.cloudlbs.web.noauth.client.presenter.NewUserFormPresenter;
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

    public static final String HISTORY_LOGIN = "login";
    public static final String HISTORY_NEW_USER = "newuser";

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
    public AppController(HandlerManager eventBus, LoginSubmitCommand loginSubmitCommand, NewUserCommand newUserCommand,
            CancelNewUserCommand cancelNewUserCommand, CreateUserCommand createUserCommand) {
        this.eBus = eventBus;
        History.addValueChangeHandler(this);
        eBus.addHandler(LoginSubmitEvent.TYPE, loginSubmitCommand);
        eBus.addHandler(NewUserRequestEvent.TYPE, newUserCommand);
        eBus.addHandler(CancelCreateUserEvent.TYPE, cancelNewUserCommand);
        eBus.addHandler(CreateUserEvent.TYPE, createUserCommand);
    }

    /**
     * Calls {@link Presenter#go(HasWidgets)} on the presenter for the incoming
     * {@link History} token.
     */
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();
        if (token != null) {
            if (token.equals(HISTORY_LOGIN)) {
                loginFormProvider.get().go(container);
            } else if (token.equals(HISTORY_NEW_USER)) {
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
            History.newItem(HISTORY_LOGIN);
        } else {
            History.fireCurrentHistoryState();
        }
    }

}
